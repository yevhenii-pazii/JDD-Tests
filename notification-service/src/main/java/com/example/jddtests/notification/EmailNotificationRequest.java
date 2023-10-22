package com.example.jddtests.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EmailNotificationRequest {
    private String email;
    private String text;
}
