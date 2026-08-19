package com.ecinemax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableScheduling turns on Spring's @Scheduled annotation support, used by
// PendingBookingCleanupTask to release seats from abandoned checkouts.
@EnableScheduling
@SpringBootApplication
public class EcinemaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcinemaxApplication.class, args);
	}

}
