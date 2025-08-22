package testcases;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Authutil {

    // Method to fetch a fresh API key
    public static String getApiKey() {
        Response authRes = RestAssured.given()
                .get("/auth")   // 🔑 or "/api/v1/auth" based on your server
                .then()
                .extract().response();

        if (authRes.statusCode() != 200) {
            throw new RuntimeException("Auth request failed! Status: " + authRes.statusCode());
        }

        String apiKey = authRes.jsonPath().getString("apiKey");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("Auth API did not return a valid key!");
        }

        System.out.println("Fetched API Key: " + apiKey);
        return apiKey;
    }
}
