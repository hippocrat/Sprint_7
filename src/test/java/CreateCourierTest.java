import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private final String baseUri = "https://qa-scooter.praktikum-services.ru";
    private final String courierLogin = "belyilotos";
    private final String courierPassword = "1234";

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUri;
    }

    @Test
    @Description("Создание нового курьера, ожидаем код 201")
    public void createNewCourier() {
        String json = createCourierJson(courierLogin, courierPassword, "belyi");
        createCourier(json)
                .then()
                .statusCode(201);
    }

    @Test
    @Description("Попытка создать двух одинаковых курьеров возвращает ошибку 409")
    public void createTwoSameCouriersReturnsError() {
        String json = createCourierJson(courierLogin, courierPassword, "belyi");
        createCourier(json);
        createCourier(json)
                .then()
                .statusCode(409);
    }

    @Test
    @Description("Создание курьера с пустым логином возвращает ошибку 400")
    public void createCourierLoginIsEmptyReturnsError() {
        String json = createCourierJson("", courierPassword, "belyi");
        createCourier(json)
                .then()
                .statusCode(400);
    }

    @Test
    @Description("Создание курьера с пустым паролем возвращает ошибку 400")
    public void createCourierPasswordIsEmptyReturnsError() {
        String json = createCourierJson(courierLogin, "", "belyi");
        createCourier(json)
                .then()
                .statusCode(400);
    }

    @Test
    @Description("Проверка, что после создания нового курьера ответ содержит 'ok: true'")
    public void createNewCourierReturnsTrue() {
        String json = createCourierJson(courierLogin, courierPassword, "belyi");
        createCourier(json)
                .then()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @Description("Создание курьера без логина возвращает ошибку 400")
    public void createCourierWithoutLoginReturnsError() {
        String json = "{\"password\": \"\", \"firstName\": \"belyi\"}";
        createCourier(json)
                .then()
                .statusCode(400);
    }

    @Test
    @Description("Создание курьера без пароля возвращает ошибку 400")
    public void createCourierWithoutPasswordReturnsError() {
        String json = "{\"login\": \"belyilotos\", \"firstName\": \"belyi\"}";
        createCourier(json)
                .then()
                .statusCode(400);
    }

    @After
    public void tearDown() {
        deleteCourierIfExists(courierLogin, courierPassword);
    }

    // Вспомогательные методы со @Step

    @Step("Создание JSON для курьера: login={login}, password={password}, firstName={firstName}")
    private String createCourierJson(String login, String password, String firstName) {
        return String.format("{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}",
                login, password, firstName);
    }

    @Step("Отправка запроса на создание курьера с телом: {json}")
    private Response createCourier(String json) {
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Удаление курьера, если он существует: login={login}")
    private void deleteCourierIfExists(String login, String password) {
        String loginJson = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password);

        Response response = given()
                .body(loginJson)
                .when()
                .post("/api/v1/courier/login");

        if (response.statusCode() == 200 && response.jsonPath().get("id") != null) {
            int id = response.jsonPath().getInt("id");
            given()
                    .when()
                    .delete("/api/v1/courier/" + id)
                    .then()
                    .statusCode(200); // Проверяем успешность удаления
        }
    }
}