package helpers;

import baseSettings.BaseSettings;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import net.minidev.json.JSONObject;
import org.apache.http.HttpStatus;
import helpers.enums.Language;
import helpers.enums.MediaType;

import java.io.File;

import static org.hamcrest.Matchers.lessThan;

public final class ReviewOperations extends BaseSettings {

    public static JsonPath getReviewById(int reviewId) {
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .get(getReviewsPath() + "/" + reviewId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath();
    }

    public static JsonPath getAllReviewsByProductId(int productId) {
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .get(getReviewsPath() + "/" + productId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath();
    }

    public static JsonPath getReviewRatingByProductId(int productId) {
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .get(getReviewsPath() + "/" + productId + "/rating")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath().get("data.rating");
    }

    public static JsonPath filterReviewsByLanguage(int productId, Language language) {
        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .get(getReviewsPath() + "/" + productId + "/" + language)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath();
    }

    public static int createReviewAndReturnReviewIdWithAttachment(int userId, int productId, int rating, String headline, File productAttachment, String review) {
        JSONObject reviewInfo = new JSONObject();
        reviewInfo.put("user_id", userId);
        reviewInfo.put("product_id", productId);
        reviewInfo.put("rating", rating);
        reviewInfo.put("headline", headline);
        reviewInfo.put("productAttachment", productAttachment);
        reviewInfo.put("review", review);

        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .body(reviewInfo.toJSONString())
                .contentType(MediaType.JSON)
                .post(getReviewsPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath().get("data.id");
    }

    public static int createReviewAndReturnReviewIdWithoutAttachment(int userId, int productId, int rating, String headline, String review) {
        JSONObject reviewInfo = new JSONObject();
        reviewInfo.put("user_id", userId);
        reviewInfo.put("product_id", productId);
        reviewInfo.put("rating", rating);
        reviewInfo.put("headline", headline);
        reviewInfo.put("review", review);

        return RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .body(reviewInfo.toJSONString())
                .contentType(MediaType.JSON)
                .post(getReviewsPath())
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .and()
                .time(lessThan(getResponseTimeLimit()))
                .extract().jsonPath().get("data.id");
    }

    public static void updateReview(int reviewId, String rating, String headline, File photo, File video, String review) {
        JSONObject userInfo = new JSONObject();
        userInfo.put("rating", rating);
        userInfo.put("headline", headline);
        userInfo.put("photo", photo);
        userInfo.put("video", video);
        userInfo.put("review", review);

        RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .body(userInfo.toJSONString())
                .contentType(MediaType.JSON)
                .patch(getReviewsPath() + "/" + reviewId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()));
    }

    public static void deleteReview(int reviewId) {
        RestAssured
                .given()
                .header("Authorization", getAccessToken())
                .contentType(MediaType.JSON)
                .delete(getReviewsPath() + "/" + reviewId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .time(lessThan(getResponseTimeLimit()));
    }

}
