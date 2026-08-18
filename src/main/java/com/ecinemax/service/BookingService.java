package com.ecinemax.service;

import com.ecinemax.dto.BookingDto;
import com.ecinemax.dto.BookingItemDto;
import com.ecinemax.dto.CreateBookingRequest;
import com.ecinemax.dto.CreateShowtimeRequest;
import com.ecinemax.dto.PaymentRequest;
import com.ecinemax.dto.ScreenDto;
import com.ecinemax.dto.SeatDto;
import com.ecinemax.dto.SeatMapDto;
import com.ecinemax.dto.ShowtimeDto;
import com.ecinemax.dto.TicketItemRequest;
import com.ecinemax.dto.TicketTypeDto;
import com.ecinemax.entity.AppUser;
import com.ecinemax.entity.Booking;
import com.ecinemax.entity.BookingItem;
import com.ecinemax.entity.BookingStatus;
import com.ecinemax.entity.Movie;
import com.ecinemax.entity.Screen;
import com.ecinemax.entity.Seat;
import com.ecinemax.entity.SeatStatus;
import com.ecinemax.entity.Showtime;
import com.ecinemax.entity.ShowtimeSeat;
import com.ecinemax.entity.TicketType;
import com.ecinemax.repository.BookingItemRepository;
import com.ecinemax.repository.BookingRepository;
import com.ecinemax.repository.MovieRepository;
import com.ecinemax.repository.ScreenRepository;
import com.ecinemax.repository.SeatRepository;
import com.ecinemax.repository.ShowtimeRepository;
import com.ecinemax.repository.ShowtimeSeatRepository;
import com.ecinemax.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final BookingRepository bookingRepository;
    private final BookingItemRepository bookingItemRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;

    public BookingService(ShowtimeRepository showtimeRepository, ShowtimeSeatRepository showtimeSeatRepository,
                           BookingRepository bookingRepository, BookingItemRepository bookingItemRepository,
                           UserRepository userRepository, MovieRepository movieRepository,
                           ScreenRepository screenRepository, SeatRepository seatRepository) {
        this.showtimeRepository = showtimeRepository;
        this.showtimeSeatRepository = showtimeSeatRepository;
        this.bookingRepository = bookingRepository;
        this.bookingItemRepository = bookingItemRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
        this.seatRepository = seatRepository;
    }

    public List<TicketTypeDto> getTicketTypes() {
        return Arrays.stream(TicketType.values())
                .map(type -> new TicketTypeDto(type, type.getPrice()))
                .toList();
    }

    public SeatMapDto getSeatMap(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found: " + showtimeId));

        List<SeatDto> seats = showtimeSeatRepository.findByShowtimeIdOrderBySeat_RowLabelAscSeat_SeatNumberAsc(showtimeId)
                .stream()
                .map(ss -> new SeatDto(ss.getSeat().getId(), ss.getSeat().getRowLabel(), ss.getSeat().getSeatNumber(), ss.getStatus()))
                .toList();

        return new SeatMapDto(showtime.getId(), showtime.getMovie().getId(), showtime.getMovie().getTitle(),
                showtime.getShowDate(), showtime.getShowTime(), seats);
    }

    // @Transactional: if anything fails partway through (e.g. a seat turns
    // out to be unavailable), every change made so far in this method is
    // rolled back - we never end up with a half-created booking.
    @Transactional
    public BookingDto createBooking(CreateBookingRequest request, String userEmail) {
        AppUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime not found"));

        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()
                || request.getTicketItems() == null || request.getTicketItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Select at least one seat and one ticket");
        }

        int totalTickets = request.getTicketItems().stream()
                .mapToInt(TicketItemRequest::getQuantity)
                .sum();

        if (totalTickets != request.getSeatIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Number of seats selected (" + request.getSeatIds().size() + ") must match number of tickets (" + totalTickets + ")");
        }

        // First pass: check every requested seat is actually available before
        // changing anything, so a single unavailable seat can't leave us with
        // a booking that only partially reserved its seats.
        List<ShowtimeSeat> showtimeSeats = new ArrayList<>();
        for (Long seatId : request.getSeatIds()) {
            ShowtimeSeat showtimeSeat = showtimeSeatRepository.findByShowtimeIdAndSeatId(request.getShowtimeId(), seatId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat " + seatId + " does not belong to this showtime"));

            if (showtimeSeat.getStatus() != SeatStatus.AVAILABLE) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Seat " + seatId + " is already booked");
            }
            showtimeSeats.add(showtimeSeat);
        }

        BigDecimal subtotal = request.getTicketItems().stream()
                .map(item -> item.getTicketType().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Booking booking = new Booking(user, showtime, generateBookingReference(), subtotal, subtotal);
        booking = bookingRepository.save(booking);

        for (TicketItemRequest item : request.getTicketItems()) {
            BigDecimal lineTotal = item.getTicketType().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            BookingItem bookingItem = new BookingItem(booking, item.getTicketType(), item.getQuantity(), item.getTicketType().getPrice(), lineTotal);
            bookingItemRepository.save(bookingItem);
        }

        for (ShowtimeSeat showtimeSeat : showtimeSeats) {
            showtimeSeat.setStatus(SeatStatus.BOOKED);
            showtimeSeat.setBooking(booking);
            showtimeSeatRepository.save(showtimeSeat);
        }

        return toDto(booking);
    }

    @Transactional
    public BookingDto submitPayment(Long bookingId, PaymentRequest request, String userEmail) {
        Booking booking = getOwnedBooking(bookingId, userEmail);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This booking has already been paid");
        }

        if (request.getMethod() == null || isBlank(request.getCardholderName())
                || request.getCardNumber() == null || request.getCardNumber().replaceAll("\\s", "").length() < 4
                || isBlank(request.getExpiry()) || isBlank(request.getCvv())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment details are incomplete");
        }

        // Mock payment: no real gateway is contacted. If the details above
        // pass basic validation, the "payment" always succeeds. Only the
        // last 4 digits of the card number are kept - never the full number,
        // expiry, or CVV.
        String digitsOnly = request.getCardNumber().replaceAll("\\s", "");
        String last4 = digitsOnly.substring(digitsOnly.length() - 4);

        booking.setPaymentMethod(request.getMethod());
        booking.setMaskedCardLast4(last4);
        booking.setTransactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        booking.setPaidAt(java.time.LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);
        return toDto(booking);
    }

    public BookingDto getBooking(Long bookingId, String userEmail) {
        return toDto(getOwnedBooking(bookingId, userEmail));
    }

    public List<BookingDto> getBookingsForUser(String userEmail) {
        AppUser user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return bookingRepository.findByUserIdOrderByBookingDateTimeDesc(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Admin: schedule a new showtime and generate one AVAILABLE ShowtimeSeat
    // row per seat in the chosen screen - the same pattern DataSeeder uses
    // for the initial seed data.
    @Transactional
    public ShowtimeDto createShowtime(CreateShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Screen not found"));

        Showtime showtime = showtimeRepository.save(new Showtime(movie, screen, request.getShowDate(), request.getShowTime()));

        List<Seat> seats = seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(screen.getId());
        List<ShowtimeSeat> showtimeSeats = seats.stream()
                .map(seat -> new ShowtimeSeat(showtime, seat, SeatStatus.AVAILABLE))
                .toList();
        showtimeSeatRepository.saveAll(showtimeSeats);

        return new ShowtimeDto(showtime.getId(), showtime.getShowDate(), showtime.getShowTime());
    }

    public List<ScreenDto> getScreens() {
        return screenRepository.findAll().stream()
                .map(screen -> new ScreenDto(screen.getId(), screen.getName()))
                .toList();
    }

    // Admin: every booking across every customer, newest first.
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .sorted((a, b) -> b.getBookingDateTime().compareTo(a.getBookingDateTime()))
                .map(booking -> {
                    BookingDto dto = toDto(booking);
                    dto.setCustomerName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName());
                    return dto;
                })
                .toList();
    }

    private Booking getOwnedBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This booking does not belong to you");
        }
        return booking;
    }

    private BookingDto toDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setBookingReference(booking.getBookingReference());
        dto.setStatus(booking.getStatus());
        dto.setMovieTitle(booking.getShowtime().getMovie().getTitle());
        dto.setShowDate(booking.getShowtime().getShowDate());
        dto.setShowTime(booking.getShowtime().getShowTime());

        List<String> seatLabels = showtimeSeatRepository.findByBookingId(booking.getId()).stream()
                .map(ss -> ss.getSeat().getRowLabel() + ss.getSeat().getSeatNumber())
                .toList();
        dto.setSeatLabels(seatLabels);

        List<BookingItemDto> items = bookingItemRepository.findByBookingId(booking.getId()).stream()
                .map(item -> new BookingItemDto(item.getTicketType(), item.getQuantity(), item.getUnitPrice(), item.getLineTotal()))
                .toList();
        dto.setItems(items);

        dto.setSubtotal(booking.getSubtotal());
        dto.setTotal(booking.getTotal());
        dto.setPaymentMethod(booking.getPaymentMethod());
        dto.setMaskedCardLast4(booking.getMaskedCardLast4());
        dto.setTransactionRef(booking.getTransactionRef());
        dto.setPaidAt(booking.getPaidAt());

        return dto;
    }

    private String generateBookingReference() {
        return "BK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
