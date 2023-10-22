package com.example.jddtests.petservice;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.responseDefinition;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;
import static com.github.tomakehurst.wiremock.http.RequestMethod.POST;
import static com.github.tomakehurst.wiremock.matching.RequestPatternBuilder.newRequestPattern;
import static io.restassured.RestAssured.given;
import static org.assertj.db.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testcontainers.utility.MountableFile.forClasspathResource;

@Testcontainers
class PetServiceApplicationOutOfProcessTest {

    private static final Network network = Network.newNetwork();
    @Container private static MariaDBContainer maria = new MariaDBContainer("mariadb:latest") {{
        withNetwork(network); withNetworkAliases("database"); withDatabaseName("test"); withPassword("123");
    }};

    @Container private static WireMockContainer mock = new WireMockContainer("wiremock/wiremock:3.1.0")
            .withNetwork(network).withNetworkAliases("mock");
    @Container private static GenericContainer petService = new GenericContainer("pet-service:latest")
            .withNetwork(network).dependsOn(maria, mock).withNetworkAliases("pet-service").withExposedPorts(8080)
            .withCopyFileToContainer(forClasspathResource("application.properties"),
                    "/app/resources/application-t.properties");
    private final WireMock wireMock = new WireMock(mock.getHost(), mock.getPort());

    @Test
    void testSavePetFlow() {
        var petName = "PET_NAME";
        wireMock.register(post("/notifications/email")
                .willReturn(responseDefinition().withStatus(203).withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody("{\"id\": 1, \"status\": \"accepted\"}")));

        given()
                .baseUri("http://localhost:" + petService.getMappedPort(8080))
                .contentType(APPLICATION_JSON)
                .body("{\"name\": \"" + petName + "\"}")

                .when().post("/pets")

                .then().statusCode(201)
                .body("id", equalTo("1"))
                .body("name", equalTo(petName));

        var source = new Source(maria.getJdbcUrl(), maria.getUsername(), maria.getPassword());
        assertThat(new Table(source, "pet")).hasNumberOfRows(1).row()
                .value().isEqualTo(1L)
                .value().isEqualTo(petName);
        wireMock.verifyThat(1, newRequestPattern(POST, urlPathEqualTo("/notifications/email")));
    }




}
