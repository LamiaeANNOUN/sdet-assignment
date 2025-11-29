package com.bloomberg.fxdeals.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Rollback(true)
class DealApiRestAssuredTest {

    @LocalServerPort
    private int port;

    private static final String API_PATH = "/api/deals/import";

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private byte[] loadFile(String name) throws IOException {
        return new ClassPathResource(name).getInputStream().readAllBytes();
    }

    @Test
    void testImportDeals_Success() throws IOException {
        given()
                .multiPart("file", "deals_success.csv", loadFile("deals_success.csv"))
                .when()
                .post(API_PATH)
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("success", everyItem(is(true)));
    }

    @Test
    void testImportDeals_PartialSuccess() throws IOException {
        given()
                .multiPart("file", "deals_partial.csv", loadFile("deals_partial.csv"))
                .when()
                .post(API_PATH)
                .then()
                .statusCode(207)
                .body("$", hasSize(3))
                .body("success", hasItems(true, false));
    }

    @Test
    void testImportDeals_ValidationError() throws IOException {
        given()
                .multiPart("file", "deals_invalid.csv", loadFile("deals_invalid.csv"))
                .when()
                .post(API_PATH)
                .then()
                .statusCode(400)
                .body("$", hasSize(3))
                .body("success", everyItem(is(false)));
    }

}
