package api.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import api.endpoints.UserEndpoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	
	public Logger logger; // for logs
	
	@BeforeClass
	public void setup()
	{
		faker=new Faker();
		userPayload=new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger= LogManager.getLogger(this.getClass());
		
		logger.debug("debugging.....");
		
	}
	
	@Test(priority=1)
	public void testPostUser()
	{
		logger.info("********** Creating user  ***************");
		Response response=UserEndpoints.createUser(userPayload);
		
		
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(),200);
		
		// STATUS CODE VALIDATION
        response.then().statusCode(200);

        // RESPONSE BODY VALIDATION
        response.then().body("message", equalTo("" + userPayload.getId()));

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
		
		logger.info("**********User is creatged  ***************");
			
	}
	
	@Test(priority=2)
	public void testGetUserByName()
	{
		logger.info("********** Reading User Info ***************");
		
		Response response=UserEndpoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("**********User info  is displayed ***************");
		
	}
	
	@Test(priority=3)
	public void testUpdateUserByName()
	{
		logger.info("********** Updating User ***************");
		
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response=UserEndpoints.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().body();
				
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("********** User updated ***************");
		//Checking data after update
		Response responseAfterupdate=UserEndpoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterupdate.getStatusCode(),200);
			
	}
	
	@Test(priority=4)
	public void testDeleteUserByName()
	{
		logger.info("**********   Deleting User  ***************");
		
		Response response=UserEndpoints.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(),200);
		
		logger.info("********** User deleted ***************");
	}


   }
