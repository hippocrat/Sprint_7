import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.OrderJson;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> colors;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getColorData() {
        return Arrays.asList(new Object[][] {
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Collections.emptyList()}
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("Создание заказа с разными цветами. Проверка успешного ответа и наличия track")
    public void createOrderWithColorsReturnsSuccess() {
        createOrder(colors)
                .then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Step("Создание заказа с цветами: {colors}")
    private Response createOrder(List<String> colors) {
        OrderJson orderJson = new OrderJson("Eren",
                "Yeger",
                "Liberia, 3rd wall",
                2,
                "+7 800 355 35 35",
                7,
                "2024-07-07",
                "Mikasa, may the force be with you",
                colors
        );

        return given()
                .header("Content-type", "application/json")
                .body(orderJson)
                .when()
                .post("/api/v1/orders");
    }
}