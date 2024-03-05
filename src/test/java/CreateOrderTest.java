import api.OrderAPI;
import classes.Track;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.notNullValue;
@AllArgsConstructor
@RunWith(Parameterized.class)
public class CreateOrderTest {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Number rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    @Parameterized.Parameters
    public static Object[][] getOrderDataTest() {
       String[] arrayAllColor = {"BLACK", "GREY"};
       String[] arrayBlackColor = {"BLACK"};
       String[] arrayGreyColor = {"GREY"};
       String[] arrayWithoutColor = {};
        return new Object[][]{
                {"Ivan", "Ivanov", "Mira, 145", "5", "+7 800 355 35 33", 5, "2024-06-06", "Test 1", arrayGreyColor},
                {"Petrov", "Petr", "Lenina, 11", "4", "+7 800 355 35 34", 3, "2024-07-07", "Test 2", arrayBlackColor},
                {"Vasileva", "Olga", "Pushkina, 67", "3", "+7 800 355 35 35", 4, "2024-08-08", "Test 3", arrayWithoutColor},
                {"Petrov", "Petr", "Lenina, 11", "4", "+7 800 355 35 34", 3, "2024-07-07", "Test 2", arrayAllColor},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check create order")
    @Description("Basic create test for /api/v1/orders endpoint")
    public void createOrder() {
        OrderAPI orderAPI = new OrderAPI();
        Response response = orderAPI.sendPostRequestCreateOrder(getOrderDataTest());
        response.then().statusCode(201);
        Track track = response.body().as(Track.class);
        MatcherAssert.assertThat(track.getTrack(), notNullValue());
    }
}
