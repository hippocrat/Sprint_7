import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetOrdersListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Order list request returns list of orders")
    public void orderListRequestReturnsListOfOrders() {

        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", is(not(empty())));
    }
}
