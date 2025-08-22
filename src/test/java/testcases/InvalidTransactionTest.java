package testcases;

import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidTransactionTest extends BaseTest {

    @Test
    public void testInvalidTransaction() {
        // ✅ Step 1: Get dynamic API key
        String apiKey = Authutil.getApiKey();

        // ✅ Step 2: Invalid request payload (negative amount)
        String requestBody = "{ " +
                "\"fromAccountId\": 1, " +
                "\"toAccountId\": 2, " +
                "\"amount\": -500, " +  // Invalid
                "\"currency\": \"COSMIC_COINS\" " +
                "}";

        // ✅ Step 3: Perform transaction request with invalid payload
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("api-key", apiKey) // pass API key
                .body(requestBody)
                .when()
                .post("/transactions")
                .then()
                .extract().response();

        System.out.println("Invalid Transaction Response: " + response.asPrettyString());

        // ✅ Step 4: Assertions
        Assert.assertEquals(response.getStatusCode(), 400, "Expected 400 Bad Request!");
        Assert.assertNotNull(response.jsonPath().get("message"), "Error message should be present for invalid request");
    }
}
