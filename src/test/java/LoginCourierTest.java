import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginCourierTest {

    private final String baseUri = "https://qa-scooter.praktikum-services.ru";
    private final String courierLogin = "belyilotos";
    private final String courierPassword = "1234";

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUri;
        createCourier(courierLogin, courierPassword, "belyi");
    }

    @Test
    @Description("Успешный логин курьера, код 200")
    public void loginAsCourierReturnsSuccess() {
        Response response = loginCourier(courierLogin, courierPassword);
        validateStatusCode(response, 200);
    }

    @Test
    @Description("Логин с пустым логином возвращает 400")
    public void loginAsCourierWithEmptyLoginReturnsError() {
        Response response = loginCourier("", courierPassword);
        validateStatusCode(response, 400);
    }

    @Test
    @Description("Логин с пустым паролем возвращает 400")
    public void loginAsCourierWithEmptyPasswordReturnsError() {
        Response response = loginCourier(courierLogin, "");
        validateStatusCode(response, 400);
    }

    @Test
    @Description("Логин с несуществующим логином возвращает 404")
    public void loginAsCourierWithWrongLoginReturnsError() {
        Response response = loginCourier("wrongLogin", courierPassword);
        validateStatusCode(response, 404);
    }

    @Test
    @Description("Логин с неверным паролем возвращает 404")
    public void loginAsCourierWithWrongPasswordReturnsError() {
        Response response = loginCourier(courierLogin, "wrongPassword");
        validateStatusCode(response, 404);
    }

    @Test
    @Description("Логин без логина возвращает 400")
    public void loginAsCourierWithoutLoginReturnsError() {
        String json = "{\"password\": \"" + courierPassword + "\"}";
        Response response = sendLoginRequest(json);
        validateStatusCode(response, 400);
    }

    @Test
    @Description("Логин без пароля возвращает 400")
    public void loginAsCourierWithoutPasswordReturnsError() {
        String json = "{\"login\": \"" + courierLogin + "\"}";
        Response response = sendLoginRequest(json);
        validateStatusCode(response, 400);
    }

    @Test
    @Description("Логин несуществующего курьера возвращает текст ошибки")
    public void loginAsCourierNotExistReturnsErrorText() {
        Response response = loginCourier("notExist", courierPassword);
        response.then()
                .statusCode(404)
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Успешный логин возвращает id")
    public void loginAsCourierReturnsId() {
        Response response = loginCourier(courierLogin, courierPassword);
        response.then()
                .statusCode(200)
                .assertThat()
                .body("id", notNullValue());
    }

    @After
    public void tearDown() {
        deleteCourierIfExists(courierLogin, courierPassword);
    }

    // Вспомогательные методы

    @Step("Создание курьера с login={login}, password={password}, firstName={firstName}")
    private void createCourier(String login, String password, String firstName) {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}",
                login, password, firstName);

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(anyOf(is(201), is(409))); // 409 если курьер уже существует
    }

    @Step("Логин курьера с login={login}, password={password}")
    private Response loginCourier(String login, String password) {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password);
        return sendLoginRequest(json);
    }

    @Step("Отправка POST-запроса логина с телом: {json}")
    private Response sendLoginRequest(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Проверка, что статус-код ответа равен {expectedStatus}")
    private void validateStatusCode(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Удаление курьера с login={login}")
    private void deleteCourierIfExists(String login, String password) {
        Response response = loginCourier(login, password);

        if (response.statusCode() == 200 && response.jsonPath().get("id") != null) {
            int id = response.jsonPath().getInt("id");

            given()
                    .when()
                    .delete("/api/v1/courier/" + id)
                    .then()
                    .statusCode(200);
        }
    }
}
