package api;

import classes.Courier;
import classes.CourierDes;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAPI {
    @Step("Send POST request create courier to /api/v1/courier")
    public Response sendPostRequestCreateCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step ("Send POST request login courier to /api/v1/courier/login")
    public Response sendPostRequestLoginCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Send DELETE request courier to /api/v1/courier/id")
    public Response sendDeleteRequestCourier(int id) {
        return given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id);
    }
}
