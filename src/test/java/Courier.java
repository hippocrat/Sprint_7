import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Courier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createNewCourier() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\", \"firstName\": \"belyi\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then()
                .statusCode(201);
    }
}
