import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Create new courier")
    public void createNewCourier() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create two same couriers returns error 409")
    public void createTwoSameCouriersReturnsError() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("Create courier when login is empty returns error 400")
    public void createCourierLoginIsEmptyReturnsError() {
        String json = "{\"login\": \"\", \"password\": \"1234\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create courier when password is empty returns error 400")
    public void createCourierPasswordIsEmptyReturnsError() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create new courier returns true")
    public void createNewCourierReturnsTrue() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Create courier without login returns error 400")
    public void createCourierWithoutLoginReturnsError() {
        String json = "{\"password\": \"\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Create courier without password returns error 400")
    public void createCourierWithoutPasswordReturnsError() {
        String json = "{\"login\": \"belyilotos\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400);
    }



    @After
    @DisplayName("Delete created courier")
    public void tearDown() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\"}";

        Response response =
                given()
                        .baseUri("https://qa-scooter.praktikum-services.ru")
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");

        int id = response.jsonPath().getInt("id");

        given()
                .baseUri("https://qa-scooter.praktikum-services.ru")
                .when()
                .delete("/api/v1/courier/" + id);

    }
}
