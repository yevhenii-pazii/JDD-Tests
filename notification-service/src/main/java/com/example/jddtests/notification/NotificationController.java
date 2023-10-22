package com.example.jddtests.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    @PostMapping("/email")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EmailsNotificationResponse email(@RequestBody EmailNotificationRequest notification) {
        System.out.println("got a request");
        return EmailsNotificationResponse.builder().id(UUID.randomUUID().toString()).status("accepted").build();
    }
}
