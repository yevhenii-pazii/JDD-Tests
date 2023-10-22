package com.example.jddtests.petservice.pet;


import com.epam.reportportal.junit5.ReportPortalExtension;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(ReportPortalExtension.class)

@WebMvcTest(PetController.class)
class PetControllerTest {
    @MockBean private PetService petService;
    @Autowired private MockMvc mockMvc;

    @Test
    void createPet() throws Exception {
        when(petService.save(any())).thenReturn(new Pet(1L, "test pet"));

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":  \"test pet\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("test pet")));

        RestAssuredMockMvc
                .given().mockMvc(mockMvc)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"name\": \"test pet\"}")
                .when()
                    .post("/pets")
                .then()
                    .statusCode(201)
                    .body("id", is("1"))
                    .body("name", is("test pet"));
    }
}