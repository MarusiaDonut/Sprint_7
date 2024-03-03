import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check list order include track")
    @Description("Basic create test for /api/v1/orders endpoint")
    public void getListOrderTest() {
        Response response = sendGetRequestListOrder();
        response.then().statusCode(200);
        MatcherAssert.assertThat(response, notNullValue());
    }

    @Step ("Send GET request create courier to /api/v1/orders")
    public Response sendGetRequestListOrder() {
        return given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
    }
}
