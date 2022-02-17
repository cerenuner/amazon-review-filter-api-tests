package helpers;

import baseSettings.BaseSettings;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import net.minidev.json.JSONObject;
import org.apache.http.HttpStatus;
import helpers.enums.MediaType;

import static org.hamcrest.Matchers.lessThan;

public class ProductOperations extends BaseSettings {

    public static JsonPath getProductById(int productId) {
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .get(getProductsPath() + "/" + productId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath();
    }

    public static int createProductAndReturnProductId(String productName) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("productName", productName);
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .body(userInfo.toJSONString())
                .contentType(MediaType.JSON)
                .post(getUsersPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath().get("data.id");
    }

    public static void deleteProduct(int productId) {
        RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .delete(getProductsPath() + "/" + productId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .and()
                .time(lessThan(getResponseTimeLimit()));
    }
}
