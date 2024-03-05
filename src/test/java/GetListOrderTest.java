import api.OrderAPI;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOrderTest {
    OrderAPI orderAPI = new OrderAPI();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check list order include track")
    @Description("Basic create test for /api/v1/orders endpoint")
    public void getListOrderTest() {
        Response response = orderAPI.sendGetRequestListOrder();
        response.then().statusCode(SC_OK);
        MatcherAssert.assertThat(response, notNullValue());
    }
}
