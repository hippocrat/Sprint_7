import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\", \"firstName\": \"belyi\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Login as courier returns 200")
    public void loginAsCourierReturnsSuccess() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Login with empty login returns 400")
    public void loginAsCourierWithEmptyLoginReturnsError() {
        String json = "{\"login\": \"\", \"password\": \"1234\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login with empty password returns 400")
    public void loginAsCourierWithEmptyPasswordReturnsError() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login as courier with wrong login returns 404")
    public void loginAsCourierWithWrongLoginReturnsError() {
        String json = "{\"login\": \"wrongLogin\", \"password\": \"1234\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Login as courier with wrong password returns 404")
    public void loginAsCourierWithWrongPasswordReturnsError() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"wrongPassword\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Login without login returns 400")
    public void loginAsCourierWithoutLoginReturnsError() {
        String json = "{\"password\": \"1234\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login without password returns 400")
    public void loginAsCourierWithoutPasswordReturnsError() {
        String json = "{\"login\": \"belyilotos\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Login as courier which is not exist returns error text")
    public void loginAsCourierNotExistReturnsErrorText() {
        String json = "{\"login\": \"notExist\", \"password\": \"1234\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat()
                .body("message",equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login as courier returns id")
    public void loginAsCourierReturnsId() {
        String json = "{\"login\": \"belyilotos\", \"password\": \"1234\"}";

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
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
