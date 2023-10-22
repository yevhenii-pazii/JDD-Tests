package com.example.jddtests.petservice.notification;

import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.MatchType;
import org.mockserver.model.Header;
import org.mockserver.model.JsonBody;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@ExtendWith(ReportPortalExtension.class)

@Testcontainers
@RestClientTest(NotificationClient.class)
@ImportAutoConfiguration(classes = {FeignAutoConfiguration.class})
class NotificationClientTest {
    @Container private static MockServerContainer mockServer =
            new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    @DynamicPropertySource static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("notification.service.url", mockServer::getEndpoint);
    }

    @Autowired private NotificationClient notificationClient;
    private final MockServerClient mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());

    @Test
    void testSendEmailNotification() {
        mockServerClient.when(
                request().withMethod("POST").withPath("/notifications/email")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Accept", ".*")
                        .withBody(new JsonBody("{\"text\": \"Hello\", \"email\": \"some@example.com\"}"))
        ).respond(
                response().withStatusCode(202).withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"1\", \"status\": \"accepted\"}"));
        var result = notificationClient.sendEmailNotification(
                EmailNotificationRequest.builder().email("some@example.com").text("Hello").build());
        assertThat(result)
                .isEqualTo(EmailsNotificationResponse.builder().id("1").status("accepted").build());
        mockServerClient.verify(request().withMethod("POST").withPath("/notifications/email"), VerificationTimes.once());
    }




}