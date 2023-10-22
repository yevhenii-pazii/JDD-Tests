package com.example.jddtests.petservice.pet;

import com.epam.reportportal.junit5.ReportPortalExtension;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ReportPortalExtension.class)

@Testcontainers
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetRepositoryTest {

    @ServiceConnection @Container
    static MariaDBContainer<?> maria = new MariaDBContainer("mariadb:latest") {
      //any additional configuration goes here
    };

    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private PetRepository petRepository;

    @Test
    @Sql("FindFirstByName.sql")
    void testFindFirstByName() {
        Pet pet = petRepository.findFirstByName("Some name");
        assertThat(pet).isNotNull().usingRecursiveComparison().isEqualTo(new Pet(2L, "Some name"));
    }


    @Test
    void notNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(petRepository).isNotNull();
    }

}