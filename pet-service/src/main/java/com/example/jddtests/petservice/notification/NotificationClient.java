package com.example.jddtests.petservice.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "notification-service", url = "${notification.service.url}")
public interface NotificationClient {

    @PostMapping("/notifications/email")
    EmailsNotificationResponse sendEmailNotification(@RequestBody EmailNotificationRequest body);
}
