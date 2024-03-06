import api.CourierAPI;
import classes.Courier;
import classes.CourierDes;
import classes.Message;
import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    Courier courierCreated = new Courier("testMaria_1", "1234", "testMaria");
    CourierAPI courierAPI = new CourierAPI();
    CourierDes courierDes = new CourierDes();
    Gson gson = new Gson();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierAPI.sendPostRequestCreateCourier(courierCreated);
    }

    @Test
    @DisplayName("Check login courier")
    @Description("Basic login test for /api/v1/courier/login endpoint")
    public void loginCourier() {
        Courier courierLogin = new Courier("testMaria_1", "1234");
        Response response = courierAPI.sendPostRequestLoginCourier(courierLogin);
        response.then().statusCode(SC_OK);
        courierDes = gson.fromJson(response.getBody().asString(), CourierDes.class);
        MatcherAssert.assertThat(courierDes.getId(), notNullValue());
    }

    @Test
    @DisplayName("Check login courier with incorrect login")
    public void loginIncorrectLoginCourier() {
        Courier courierLogin = new Courier("testMaria_100", "1234");
        Response response = courierAPI.sendPostRequestLoginCourier(courierLogin);
        response.then().statusCode(SC_NOT_FOUND);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login courier with incorrect password")
    public void loginIncorrectPasswordCourier() {
        Courier courierLogin = new Courier("testMaria_1", "123456");
        Response response = courierAPI.sendPostRequestLoginCourier(courierLogin);
        response.then().statusCode(SC_NOT_FOUND);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login courier without login")
    public void loginWithoutLoginCourier() {
        Courier courierLogin = new Courier("", "1234");
        Response response = courierAPI.sendPostRequestLoginCourier(courierLogin);
        response.then().statusCode(SC_BAD_REQUEST);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login courier without password")
    public void loginWithoutPasswordCourier() {
        Courier courierLogin = new Courier("testMaria_1", "");
        Response response = courierAPI.sendPostRequestLoginCourier(courierLogin);
        response.then().statusCode(SC_BAD_REQUEST);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для входа"));
    }

    @After
    @DisplayName("Delete courier")
    public void deleteCourier() {
        System.out.println(courierDes.getId());
        if (courierDes.getId() != 0) {
            Response responseDelete = courierAPI.sendDeleteRequestCourier(courierDes.getId());
            responseDelete.then().statusCode(SC_OK);
        }
    }
}
