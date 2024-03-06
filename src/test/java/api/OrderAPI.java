package api;

import classes.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderAPI {

    final static String pathOrders = "/api/v1/orders";

    @Step("Send POST request create order to /api/v1/orders")
    public Response sendPostRequestCreateOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step ("Send GET request create courier to /api/v1/orders")
    public Response sendGetRequestListOrder() {
        return given()
                .header("Content-type", "application/json")
                .get(pathOrders);
    }
}
