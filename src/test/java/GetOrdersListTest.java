import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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
    @Description("Запрос списка заказов. Проверка, что возвращается непустой список заказов")
    public void orderListRequestReturnsListOfOrders() {
        Response response = getOrdersList();
        validateOrdersList(response);
    }

    @Step("Отправка GET-запроса на получение списка заказов")
    private Response getOrdersList() {
        return given()
                .when()
                .get("/api/v1/orders");
    }

    @Step("Проверка, что в ответе код 200 и непустой список заказов")
    private void validateOrdersList(Response response) {
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", is(not(empty())));
    }
}