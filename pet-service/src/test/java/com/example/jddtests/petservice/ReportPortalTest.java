package com.example.jddtests.petservice;

import com.epam.reportportal.annotations.TestCaseId;
import com.epam.reportportal.annotations.attribute.Attribute;
import com.epam.reportportal.annotations.attribute.Attributes;
import com.epam.reportportal.junit5.ReportPortalExtension;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.service.ReportPortal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Date;

@Slf4j
@ExtendWith(ReportPortalExtension.class)
public class ReportPortalTest {

    @Test
    @TestCaseId("Jira ID")
    @Attributes(
            attributes = @Attribute(key = "key1", value = "1")
    )
    void test() {
        ReportPortal.emitLog(
                "some message", LogLevel.INFO.name(), new Date(),
                new File(getClass().getResource("/Face-smile.svg.png").getFile()));
        log.info("Some");
    }
}


