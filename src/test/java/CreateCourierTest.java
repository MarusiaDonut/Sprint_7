import api.CourierAPI;
import classes.Courier;
import classes.CourierDes;
import classes.Message;
import classes.Status;
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

public class CreateCourierTest {
    Courier courierCreated = new Courier("testMaria_2", "1234", "testMaria");
    Courier courierLogin = new Courier("testMaria_2", "1234");
    CourierAPI courierAPI = new CourierAPI();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check create new courier")
    @Description("Basic create test for /api/v1/courier endpoint")
    public void createCourier() {
        Response response = courierAPI.sendPostRequestCreateCourier(courierCreated);
        response.then().statusCode(SC_CREATED);
        Status status = response.body().as(Status.class);
        MatcherAssert.assertThat(status.isOk(), equalTo(true));
    }

   @Test
   @DisplayName("Check create new courier without login")
   public void createCourierWithoutLogin() {
       Courier courier = new Courier("", "1234", "testMaria");
       Response response = courierAPI.sendPostRequestCreateCourier(courier);
       response.then().statusCode(SC_BAD_REQUEST);
       Message message = response.body().as(Message.class);
       MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
   }

    @Test
    @DisplayName("Check create new courier without password")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier("testMaria100", "", "testMaria");
        Response response = courierAPI.sendPostRequestCreateCourier(courier);
        response.then().statusCode(SC_BAD_REQUEST);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check create new same courier")
    public void createSameCourier() {
        courierAPI.sendPostRequestCreateCourier(courierCreated);
        Response response = courierAPI.sendPostRequestCreateCourier(courierCreated);
        response.then().statusCode(SC_CONFLICT);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Этот логин уже используется"));
    }

    @After
    @DisplayName("Delete courier")
    public void deleteCourier() {
        Gson gson = new Gson();
        Response responseLogin = courierAPI.sendPostRequestLoginCourier(courierLogin);
        CourierDes courierDes = gson.fromJson(responseLogin.getBody().asString(), CourierDes.class);
        if (courierDes.getId() != 0) {
            Response responseDelete = courierAPI.sendDeleteRequestCourier(courierDes.getId());
            responseDelete.then().statusCode(SC_OK);
        }
    }

}
