package com.github.rafaellbarros.ifood.mp.resource;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;


@QuarkusTest
public class PratoResourceTest {

    // TODO: REFACTORING TO TESTCONTAINERS
    // @Test
    public void testPratoResourceEndpoint() {
        String body = given()
                .when().get("/pratos")
                .then()
                .statusCode(200).extract().asString();

        System.out.println(body);
    }

}