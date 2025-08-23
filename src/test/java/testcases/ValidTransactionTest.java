package testcases;

import base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ValidTransactionTest extends BaseTest {

    @Test
    public void testValidTransaction() {
        //  dynamic API key from Authutil class
        String apiKey = Authutil.getApiKey();

        // Create From Account
        String fromAccountBody = "{ \"owner\": \"Alice\", \"balance\": 500, \"currency\": \"COSMIC_COINS\" }";

        Response fromRes = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("api-key", apiKey)   // 👈 now key is passed here
                .body(fromAccountBody)
                .when()
                .post("/accounts")
                .then()
                .extract().response();

        System.out.println("Create FromAccount Response: " + fromRes.asPrettyString());
        Assert.assertEquals(fromRes.statusCode(), 200, "FromAccount creation failed");
        Integer fromAccountId = fromRes.jsonPath().getInt("account.id");

        // Create To Account
        String toAccountBody = "{ \"owner\": \"Bob\", \"balance\": 200, \"currency\": \"COSMIC_COINS\" }";

        Response toRes = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("api-key", apiKey)   // 👈 pass key here too
                .body(toAccountBody)
                .when()
                .post("/accounts")
                .then()
                .extract().response();

        System.out.println("Create ToAccount Response: " + toRes.asPrettyString());
        Assert.assertEquals(toRes.statusCode(), 200, "ToAccount creation failed");
        Integer toAccountId = toRes.jsonPath().getInt("account.id");

        // Perform Transaction
        String txBody = "{ " +
                "\"fromAccountId\": " + fromAccountId + ", " +
                "\"toAccountId\": " + toAccountId + ", " +
                "\"amount\": 100, " +
                "\"currency\": \"COSMIC_COINS\" " +
                "}";

        Response txRes = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("api-key", apiKey)   // 👈 and here
                .body(txBody)
                .when()
                .post("/transactions")
                .then()
                .extract().response();

        System.out.println("Transaction Response: " + txRes.asPrettyString());

        Assert.assertEquals(txRes.getStatusCode(), 200, "Transaction failed");
        Assert.assertNotNull(txRes.jsonPath().get("transaction.id"), "Transaction ID should not be null");
        Assert.assertTrue(txRes.jsonPath().getBoolean("success"), "Transaction should be successful");
    }
}
