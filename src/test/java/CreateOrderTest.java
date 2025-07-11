import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.OrderJson;

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
    public static Object[][] getColorData() {
        return new Object[][] {
                {"BLACK"},
                {"GREY"},
                {"BLACK", "GREY"},
                {""}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Parameterized test: create order with different colors")
    public void createOrderWithColorsReturnsSuccess() {

        OrderJson orderJson = new OrderJson("Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
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
