package com.example.jddtests.petservice.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EmailsNotificationResponse {
    private String id;
    private String status;
}
