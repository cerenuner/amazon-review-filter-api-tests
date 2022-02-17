package helpers;

import baseSettings.BaseSettings;
import helpers.enums.Country;
import helpers.enums.Language;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import net.minidev.json.JSONObject;
import org.apache.http.HttpStatus;
import helpers.enums.MediaType;

import java.io.File;
import java.util.HashMap;

import static org.hamcrest.Matchers.lessThan;

public class UserOperations extends BaseSettings {

    public static JsonPath getInfoUserById(int userId) {
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .get(getUsersPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath();
    }

    public static int createUserAndReturnUserId(String name, String email, String password, Language userLanguage, Country userCountry) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("name", name);
        userInfo.put("email", email);
        userInfo.put("password", password);
        userInfo.put("userLanguage", userLanguage);
        userInfo.put("userCountry", userCountry);

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

    public static void updateUserPreferences(int userId, Language language, Country country) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("language", language);
        userInfo.put("country", country);

        RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .body(userInfo.toJSONString())
                .contentType(MediaType.JSON)
                .patch(getUsersPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()));
    }

    public static void deleteUser(int userId) {
        RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .delete(getUsersPath() + "/" + userId)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .and()
                .time(lessThan(getResponseTimeLimit()));
    }
}
