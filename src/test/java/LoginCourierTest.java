import classes.Courier;
import classes.CourierDes;
import classes.Message;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    Courier courier = new Courier("testMaria13", "1234", "testMaria");

    @Test
    @DisplayName("Check login courier")
    @Description("Basic login test for /api/v1/courier/login endpoint")
    public void loginCourier() {
        Courier courier = new Courier("testMaria13", "1234");
        sendPostRequestCreateCourier();
        Response response = sendPostRequestLoginCourier(courier);
        response.then().statusCode(200);
        CourierDes courierDes = response.body().as(CourierDes.class);
        MatcherAssert.assertThat(courierDes.getId(), notNullValue());
    }

    @Test
    @DisplayName("Check login courier with incorrect login")
    public void loginIncorrectLoginCourier() {
        Courier courier = new Courier("testMaria100", "1234", "testMaria");
        sendPostRequestCreateCourier();
        Response response = sendPostRequestLoginCourier(courier);
        response.then().statusCode(404);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login courier with incorrect password")
    public void loginIncorrectPasswordCourier() {
        Courier courier = new Courier("testMaria13", "123456", "testMaria");
        sendPostRequestCreateCourier();
        Response response = sendPostRequestLoginCourier(courier);
        response.then().statusCode(404);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login courier without login")
    public void loginWithoutLoginCourier() {
        Courier courier = new Courier("", "1234", "testMaria");
        sendPostRequestCreateCourier();
        Response response = sendPostRequestLoginCourier(courier);
        response.then().statusCode(400);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login courier without password")
    public void loginWithoutPasswordCourier() {
        Courier courier = new Courier("testMaria13", "", "testMaria");
        sendPostRequestCreateCourier();
        Response response = sendPostRequestLoginCourier(courier);
        response.then().statusCode(400);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для входа"));
    }

    @Step ("Send POST request create courier to /api/v1/courier")
    public void sendPostRequestCreateCourier() {
        given()
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
}
