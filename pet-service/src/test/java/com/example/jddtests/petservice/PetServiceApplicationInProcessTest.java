package com.example.jddtests.petservice;

import com.epam.reportportal.junit5.ReportPortalExtension;
import com.example.jddtests.petservice.notification.EmailNotificationRequest;
import com.example.jddtests.petservice.notification.EmailsNotificationResponse;
import com.example.jddtests.petservice.notification.NotificationClient;
import io.restassured.RestAssured;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import javax.sql.DataSource;

import static org.assertj.db.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(ReportPortalExtension.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class PetServiceApplicationInProcessTest {

    @LocalServerPort private int port;
    @Autowired private DataSource dataSource;
    @MockBean private NotificationClient notificationClient;

    @Test
    void testSavePetFlow() {
        var petName = "PET_NAME";

        var req = EmailNotificationRequest.builder().email("some@example.com").text("Hello " + petName).build();
        when(notificationClient.sendEmailNotification(eq(req)))
                .thenReturn(EmailsNotificationResponse.builder().id("1").status("accepted").build());

        RestAssured
                .given().baseUri("http://localhost:" + port)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\"name\": \"" + petName + "\"}")

                .when().post("/pets")

                .then().statusCode(201)
                .body("id", equalTo("1"))
                .body("name", equalTo(petName));

        assertThat(new Table(dataSource, "pet")).hasNumberOfRows(1).row()
                .value().isEqualTo(1L)
                .value().isEqualTo(petName);
        verify(notificationClient, times(1)).sendEmailNotification(eq(req));
        verifyNoMoreInteractions(notificationClient);
    }



}
