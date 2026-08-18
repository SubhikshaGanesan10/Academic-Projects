package com.ecinemax.config;

import com.ecinemax.entity.AppUser;
import com.ecinemax.entity.Movie;
import com.ecinemax.entity.MovieStatus;
import com.ecinemax.entity.Screen;
import com.ecinemax.entity.Seat;
import com.ecinemax.entity.SeatStatus;
import com.ecinemax.entity.Showtime;
import com.ecinemax.entity.ShowtimeSeat;
import com.ecinemax.entity.UserRole;
import com.ecinemax.repository.MovieRepository;
import com.ecinemax.repository.ScreenRepository;
import com.ecinemax.repository.SeatRepository;
import com.ecinemax.repository.ShowtimeRepository;
import com.ecinemax.repository.ShowtimeSeatRepository;
import com.ecinemax.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// CommandLineRunner's run() method executes once, automatically, right after
// the application starts up. We use it here to load sample data so Phase 2
// has real movies to display - only if the database is currently empty, so
// restarting the app doesn't keep re-inserting duplicates.
@Component
public class DataSeeder implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeSeatRepository showtimeSeatRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(MovieRepository movieRepository, ShowtimeRepository showtimeRepository,
                       ScreenRepository screenRepository, SeatRepository seatRepository,
                       ShowtimeSeatRepository showtimeSeatRepository,
                       UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
        this.screenRepository = screenRepository;
        this.seatRepository = seatRepository;
        this.showtimeSeatRepository = showtimeSeatRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();

        if (movieRepository.count() > 0) {
            return;
        }

        List<Movie> nowShowing = List.of(
                new Movie("Uncharted", "Action, Adventure", 116, LocalDate.of(2022, 2, 18),
                        "Tom Holland, Mark Wahlberg", "Ruben Fleischer", "Avi Arad, Ari Arad",
                        "Drake is recruited by Sullivan in a race against corrupt billionaire Santiago Moncada and mercenary leader Jo Braddock to locate the fabled treasure of the Magellan expedition.",
                        6.3, "eHp3MbsCbMg", "/posters/uncharted_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("Brave", "Animation, Adventure, Family", 93, LocalDate.of(2012, 6, 22),
                        "Kelly Macdonald, Billy Connolly", "Mark Andrews, Brenda Chapman", "Katherine Sarafian",
                        "A headstrong young princess defies an age-old custom, setting in motion a chain of events that unleash unintended peril on her kingdom.",
                        7.1, "TEHWDA_6e3M", "/posters/brave_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("The Batman: Unmask the Truth", "Action, Crime, Drama", 176, LocalDate.of(2022, 3, 4),
                        "Robert Pattinson, Zoe Kravitz", "Matt Reeves", "Dylan Clark",
                        "When a sadistic serial killer begins murdering key political figures in Gotham, Batman is forced to investigate the city's hidden corruption.",
                        7.8, "mqqft2x_Aa4", "/posters/batman_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("SpiderMan: No Way Home", "Action, Adventure, Fantasy", 148, LocalDate.of(2021, 12, 17),
                        "Tom Holland, Zendaya", "Jon Watts", "Kevin Feige, Amy Pascal",
                        "With his identity revealed, Peter Parker asks Doctor Strange for help, but a spell gone wrong brings villains from other worlds.",
                        8.2, "JfVOs4VSpmA", "/posters/nowayhome_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("Joker", "Crime, Drama, Thriller", 122, LocalDate.of(2019, 10, 4),
                        "Joaquin Phoenix, Robert De Niro", "Todd Phillips", "Todd Phillips, Bradley Cooper",
                        "A mentally troubled stand-up comedian embarks on a downward spiral that leads to the creation of an iconic villain.",
                        8.4, "t433PEQGErc", "/posters/joker_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("The Lorax", "Animation, Comedy, Family", 86, LocalDate.of(2012, 3, 2),
                        "Danny DeVito, Ed Helms", "Chris Renaud, Kyle Balda", "Chris Meledandri",
                        "A 12-year-old boy searches for the one thing that will make him popular with a girl he likes, and learns the story of the Lorax.",
                        6.5, "dNMBVVdrMKo", "/posters/thelorax_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("Life of Pi", "Adventure, Drama, Fantasy", 127, LocalDate.of(2012, 11, 21),
                        "Suraj Sharma, Irrfan Khan", "Ang Lee", "Gil Netter, Ang Lee",
                        "A young man survives a disaster at sea and is stranded on a lifeboat with a Bengal tiger.",
                        7.9, "3mMN693-F3U", "/posters/lifeofpi_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("Jumanji: Welcome to the Jungle", "Action, Adventure, Comedy", 119, LocalDate.of(2017, 12, 20),
                        "Dwayne Johnson, Kevin Hart", "Jake Kasdan", "Matt Tolmach",
                        "Four teenagers are sucked into a magical video game, and the only way they can escape is to work together to finish the game.",
                        6.9, "2QKg5SZ_35I", "/posters/jumanji_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("The Meg", "Action, Sci-Fi, Thriller", 113, LocalDate.of(2018, 8, 10),
                        "Jason Statham, Li Bingbing", "Jon Turteltaub", "Lorenzo di Bonaventura",
                        "A deep-sea submersible pilot must save a group of scientists from an enormous prehistoric shark thought to be extinct.",
                        5.6, "udm5jUA-2bs", "/posters/themeg_poster.jpg", MovieStatus.NOW_SHOWING),
                new Movie("Avatar 2: The Way of Water", "Action, Adventure, Fantasy", 192, LocalDate.of(2022, 12, 16),
                        "Sam Worthington, Zoe Saldana", "James Cameron", "James Cameron, Jon Landau",
                        "Jake Sully and his family fight to stay together as they navigate the dangers of Pandora's oceans.",
                        7.6, "NZrX_ES93JA", "/posters/avatar2_poster.jpg", MovieStatus.NOW_SHOWING)
        );

        List<Movie> comingSoon = List.of(
                new Movie("Us", "Horror, Mystery, Thriller", 116, LocalDate.of(2019, 3, 22),
                        "Lupita Nyong'o, Winston Duke", "Jordan Peele", "Jordan Peele, Ian Cooper",
                        "A family's serene beach vacation turns to chaos when their doppelgangers appear and begin to terrorize them.",
                        6.8, "hNCmb-4oXJA", "/posters/us_poster.jpg", MovieStatus.COMING_SOON),
                new Movie("Morbius", "Action, Adventure, Fantasy", 104, LocalDate.of(2022, 4, 1),
                        "Jared Leto, Matt Smith", "Daniel Espinosa", "Avi Arad, Matt Tolmach",
                        "A biochemist afflicted with a rare blood disease attempts to cure himself, inadvertently transforming into a vampire.",
                        5.2, "SQK-QxxtE8Y", "/posters/morbius_poster.jpg", MovieStatus.COMING_SOON),
                new Movie("Pitch Perfect", "Comedy, Music, Romance", 112, LocalDate.of(2012, 9, 28),
                        "Anna Kendrick, Rebel Wilson", "Jason Moore", "Paul Brooks, Max Handelman",
                        "Becca, a freshman at Barden University, is cajoled into joining The Bellas, her school's all-girls singing group.",
                        7.1, "8dItOM6eYXY", "/posters/pitchperfect_poster.jpg", MovieStatus.COMING_SOON),
                new Movie("Skyscraper", "Action, Thriller", 102, LocalDate.of(2018, 7, 13),
                        "Dwayne Johnson, Neve Campbell", "Rawson Marshall Thurber", "Beau Flynn, Dwayne Johnson",
                        "An FBI agent turned security expert must save his family from the tallest, safest building in the world, now on fire.",
                        5.7, "t9QePUT-Yt8", "/posters/skyscraper_poster.jpg", MovieStatus.COMING_SOON),
                new Movie("Dune", "Action, Adventure, Drama", 155, LocalDate.of(2021, 10, 22),
                        "Timothee Chalamet, Rebecca Ferguson", "Denis Villeneuve", "Mary Parent, Denis Villeneuve",
                        "A noble family becomes embroiled in a war for control over the galaxy's most valuable asset while its heir becomes troubled by visions of a dark future.",
                        8.0, "n9xhJrPXop4", "/posters/dune_poster.jpg", MovieStatus.COMING_SOON)
        );

        movieRepository.saveAll(nowShowing);
        movieRepository.saveAll(comingSoon);

        // One screen, 6 rows (A-F) x 8 seats, matching the seat map that was
        // already hardcoded into the original buytickets.html.
        Screen screen = screenRepository.save(new Screen("Screen 1", 6, 8));
        List<Seat> seats = new ArrayList<>();
        for (char row = 'A'; row < 'A' + screen.getTotalRows(); row++) {
            for (int seatNumber = 1; seatNumber <= screen.getSeatsPerRow(); seatNumber++) {
                seats.add(new Seat(screen, String.valueOf(row), seatNumber));
            }
        }
        seats = seatRepository.saveAll(seats);

        // Give each now-showing movie a few showtimes over the next 3 days,
        // so Phase 2's showtimes grid has real data to display. Every
        // showtime gets one AVAILABLE ShowtimeSeat row per physical seat -
        // that's what buytickets.html's seat map is actually built from.
        List<LocalTime> dailyTimes = List.of(LocalTime.of(13, 15), LocalTime.of(16, 45), LocalTime.of(19, 30));
        LocalDate today = LocalDate.now();

        List<ShowtimeSeat> allShowtimeSeats = new ArrayList<>();
        for (Movie movie : nowShowing) {
            for (int dayOffset = 0; dayOffset < 3; dayOffset++) {
                LocalDate showDate = today.plusDays(dayOffset);
                for (LocalTime time : dailyTimes) {
                    Showtime showtime = showtimeRepository.save(new Showtime(movie, screen, showDate, time));

                    for (Seat seat : seats) {
                        allShowtimeSeats.add(new ShowtimeSeat(showtime, seat, SeatStatus.AVAILABLE));
                    }
                }
            }
        }
        // One batch call instead of one save() per row (10 movies x 3 days x
        // 3 times x 48 seats = 4320 rows) - saveAll still issues one INSERT
        // per row, but as a single transaction instead of thousands of tiny
        // ones, which is far faster over a real network connection.
        showtimeSeatRepository.saveAll(allShowtimeSeats);
    }

    // Public registration only ever creates CUSTOMER accounts, so an ADMIN
    // account has to be created some other way - here, seeded directly.
    private void seedAdminUser() {
        if (userRepository.existsByEmail("admin@ecinemax.com")) {
            return;
        }

        AppUser admin = new AppUser(
                "Admin", "User", "admin@ecinemax.com",
                passwordEncoder.encode("Admin123!"),
                null, null, UserRole.ADMIN
        );
        userRepository.save(admin);
    }
}
