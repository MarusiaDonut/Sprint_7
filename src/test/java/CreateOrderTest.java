import classes.CourierDes;
import classes.Track;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.hamcrest.CoreMatchers.notNullValue;

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

    public CreateOrderTest(String firstName, String lastName, String address, String metroStation,
                           String phone, Number rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
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
        Response response = sendPostRequestCreateOrder();
        response.then().statusCode(201);
        Track track = response.body().as(Track.class);
        MatcherAssert.assertThat(track.getTrack(), notNullValue());
    }

    @Step ("Send POST request create order to /api/v1/orders")
    public Response sendPostRequestCreateOrder() {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(getOrderDataTest())
                .when()
                .post("/api/v1/orders");
    }
}
