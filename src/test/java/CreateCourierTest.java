import classes.Courier;
import classes.Message;
import classes.Status;
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

public class CreateCourierTest {
    Courier courier = new Courier("testMaria12", "1234", "testMaria");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check create new courier")
    @Description("Basic create test for /api/v1/courier endpoint")
    public void createCourier() {
        Response response = sendPostRequestCreateCourier(courier);
        response.then().statusCode(201);
        Status status = response.body().as(Status.class);
        MatcherAssert.assertThat(status.isOk(), equalTo(true));
    }

   @Test
   @DisplayName("Check create new courier without login")
   public void createCourierWithoutLogin() {
       Courier courier = new Courier("", "1234", "testMaria");
       Response response = sendPostRequestCreateCourier(courier);
       response.then().statusCode(400);
       Message message = response.body().as(Message.class);
       MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
   }

    @Test
    @DisplayName("Check create new courier without password")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier("testMaria100", "", "testMaria");
        Response response = sendPostRequestCreateCourier(courier);
        response.then().statusCode(400);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check create new same courier")
    public void createSameCourier() {
        Response response = sendPostRequestCreateCourier(courier);
        response.then().statusCode(409);
        Message message = response.body().as(Message.class);
        MatcherAssert.assertThat(message.getMessage(), equalTo("Этот логин уже используется"));
    }

    @Step ("Send POST request create courier to /api/v1/courier")
    public Response sendPostRequestCreateCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }
}
