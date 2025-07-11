import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.OrderJson;

import java.util.Arrays;
import java.util.Collection;
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
                {Arrays.asList("")}
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Parameterized test: create order with different colors")
    public void createOrderWithColorsReturnsSuccess() {

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

        given()
                .header("Content-type", "application/json")
                .body(orderJson)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .assertThat()
                .body("track", notNullValue());
    }

}
