package com.flashtix.common.utils;

import com.flashtix.common.enums.Role;
import com.flashtix.entity.Event;
import com.flashtix.entity.TicketType;
import com.flashtix.entity.User;
import com.flashtix.repository.EventRepository;
import com.flashtix.repository.TicketTypeRepository;
import com.flashtix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInit implements CommandLineRunner {

        private final UserRepository userRepository;
        private final EventRepository eventRepository;
        private final TicketTypeRepository ticketTypeRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                initializeAdminAccount();
                initializeEvents();
        }

        private void initializeAdminAccount() {
                String adminEmail = "admin@flashtix.com";

                if (!userRepository.existsByEmail(adminEmail)) {
                        User admin = User.builder()
                                        .email(adminEmail)
                                        .password(passwordEncoder.encode("Admin@123"))
                                        .role(Role.ADMIN)
                                        .build();

                        userRepository.save(admin);
                        log.info("Admin account created successfully");
                        log.info("Email: {}", adminEmail);
                        log.info("Password: Admin@123");
                        log.info("Please change the password after first login");
                } else {
                        log.info("Admin account already exists");
                }
        }

        private void initializeEvents() {
                if (eventRepository.count() == 0) {
                        LocalDateTime now = LocalDateTime.now();

                        Event event1 = Event.builder()
                                        .title("Summer Music Festival 2026")
                                        .location("Central Park, New York")
                                        .startTime(now.plusDays(30))
                                        .endTime(now.plusDays(30).plusHours(8))
                                        .bannerUrl(
                                                        "https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768364031/6a7377d9-2f58-45d0-a186-c3ba9ee8ce83.png")
                                        .build();

                        Event event2 = Event.builder()
                                        .title("Tech Innovation Summit")
                                        .location("Silicon Valley Convention Center")
                                        .startTime(now.plusDays(45))
                                        .endTime(now.plusDays(47))
                                        .bannerUrl(
                                                        "https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363998/657b9588-d1e9-4eb5-8247-0e2d18f8cf40.png")
                                        .build();

                        Event event3 = Event.builder()
                                        .title("International Food Festival")
                                        .location("Downtown Market Square")
                                        .startTime(now.plusDays(60))
                                        .endTime(now.plusDays(60).plusHours(10))
                                        .bannerUrl(
                                                        "https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363984/d5b57b18-19ec-4f3b-9ba3-cbaf2d0bdb08.png")
                                        .build();

                        Event event4 = Event.builder()
                                        .title("Art & Design Expo 2026")
                                        .location("Metropolitan Art Gallery")
                                        .startTime(now.plusDays(75))
                                        .endTime(now.plusDays(77))
                                        .bannerUrl(
                                                        "https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363980/1ef7f3ee-3cf3-4375-b9db-77e12f677b88.png")
                                        .build();

                        Event event5 = Event.builder()
                                        .title("Sports Championship Finals")
                                        .location("National Stadium")
                                        .startTime(now.plusDays(90))
                                        .endTime(now.plusDays(90).plusHours(6))
                                        .bannerUrl(
                                                        "https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363932/a3cbf01d-77ca-4843-80be-1f3865a848b8.png")
                                        .build();

                        Event event6 = Event.builder()
                                        .title("Comedy Night Live Show")
                                        .location("The Grand Theater")
                                        .startTime(now.plusDays(105))
                                        .endTime(now.plusDays(105).plusHours(3))
                                        .bannerUrl(
                                                        "https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363930/70ee4961-ed31-4691-b623-91348b9fb6e0.png")
                                        .build();

                        eventRepository.save(event1);
                        eventRepository.save(event2);
                        eventRepository.save(event3);
                        eventRepository.save(event4);
                        eventRepository.save(event5);
                        eventRepository.save(event6);

                        log.info("Successfully initialized 6 events");

                        // Create ticket types for each event
                        createTicketsForEvent(event1, 150.0, 75.0, 50.0);
                        createTicketsForEvent(event2, 500.0, 250.0, 200.0);
                        createTicketsForEvent(event3, 80.0, 45.0, 35.0);
                        createTicketsForEvent(event4, 120.0, 65.0, 50.0);
                        createTicketsForEvent(event5, 200.0, 120.0, 90.0);
                        createTicketsForEvent(event6, 100.0, 60.0, 45.0);

                        log.info("Successfully initialized ticket types for all events");
                } else {
                        log.info("Events already exist in the database");
                }
        }

        private void createTicketsForEvent(Event event, Double vipPrice, Double generalPrice, Double earlyBirdPrice) {
                TicketType vipTicket = TicketType.builder()
                                .name("VIP")
                                .event(event)
                                .price(vipPrice)
                                .initialQuantity(100)
                                .remainingQuantity(100)
                                .build();

                TicketType generalTicket = TicketType.builder()
                                .name("General Admission")
                                .event(event)
                                .price(generalPrice)
                                .initialQuantity(500)
                                .remainingQuantity(500)
                                .build();

                TicketType earlyBirdTicket = TicketType.builder()
                                .name("Early Bird")
                                .event(event)
                                .price(earlyBirdPrice)
                                .initialQuantity(200)
                                .remainingQuantity(200)
                                .build();

                ticketTypeRepository.save(vipTicket);
                ticketTypeRepository.save(generalTicket);
                ticketTypeRepository.save(earlyBirdTicket);
        }
}
