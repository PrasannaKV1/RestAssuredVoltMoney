package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    @BeforeClass
    public void setup() {
        // Base URI for all APIs
        RestAssured.baseURI = "https://template.postman-echo.com/api/v1";


    }

}
