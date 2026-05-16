package api.test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import org.testng.annotations.Test;
import io.restassured.response.Response;

public class PostRequest {

    @Test
    public void testPostUser()
    {

        HashMap requestBody = new HashMap();
        requestBody.put("id", 101);
        requestBody.put("username", "testuser");
        requestBody.put("firstName", "Test");
        requestBody.put("lastName", "User");
        requestBody.put("email", "user123@gmail.com");
        requestBody.put("password", "password123");
        
       

        Response response =

        given()
            .contentType("application/json")
            .body(requestBody)

        .when()
            .post("https://petstore.swagger.io/v2/user");



        // STATUS CODE VALIDATION
        response.then().statusCode(200);

        // RESPONSE BODY VALIDATION
        response.then().body("message", equalTo("101"));

        // RESPONSE TIME VALIDATION
        response.then().time(lessThan(3000L));

        // CONTENT TYPE VALIDATION
        response.then().contentType("application/json");

        // HEADER VALIDATION
        response.then().header("Server", notNullValue());

        // PRINT RESPONSE
        response.then().log().all();

        // ID VALIDATION
        String responseId =
                response.jsonPath().getString("message");

        System.out.println("Created User ID: " + responseId);

    }
}