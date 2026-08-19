package com.ecinemax.config;

import com.ecinemax.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// Releases seats from abandoned checkouts. Split from BookingService itself
// so "what to do" (BookingService.cancelExpiredPendingBookings) stays
// separate from "when to do it" (this class).
@Component
public class PendingBookingCleanupTask {

    private static final Logger log = LoggerFactory.getLogger(PendingBookingCleanupTask.class);

    private final BookingService bookingService;

    public PendingBookingCleanupTask(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Runs every 5 minutes, starting 5 minutes after the app boots.
    @Scheduled(fixedRateString = "${app.booking.cleanup-interval-ms:300000}")
    public void releaseExpiredBookings() {
        int cancelled = bookingService.cancelExpiredPendingBookings();
        if (cancelled > 0) {
            log.info("Released seats from {} expired pending booking(s)", cancelled);
        }
    }
}
