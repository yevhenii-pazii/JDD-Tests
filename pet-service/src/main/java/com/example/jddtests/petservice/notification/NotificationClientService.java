package com.example.jddtests.petservice.notification;

import com.example.jddtests.petservice.pet.Pet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationClientService {

    private final NotificationClient notificationClient;

    public void notifyClinic(Pet pet) {
        System.out.println("send a request");
        notificationClient.sendEmailNotification(
                EmailNotificationRequest.builder().email("some@example.com").text("Hello " + pet.getName()).build());
    }
}
