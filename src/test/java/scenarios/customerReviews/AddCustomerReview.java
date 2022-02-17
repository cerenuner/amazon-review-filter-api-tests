package scenarios.customerReviews;

import baseSettings.BaseSettings;
import helpers.commonOperations.RandomCustomerData;
import helpers.enums.Country;
import helpers.enums.Language;
import io.restassured.path.json.JsonPath;
import net.minidev.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import helpers.ProductOperations;
import helpers.ReviewOperations;
import helpers.UserOperations;

import java.io.File;

public class AddCustomerReview extends BaseSettings {

    private File testImage = new File(getTestImagePath());
    private File testVideo = new File(getTestVideoPath());
    int userId, productId, germanReviewId, englishReviewId, italianReviewId;
    int ratingGerman = 5, ratingEnglish = 3, ratingItalian = 2;

    @BeforeMethod
    public void beforeMethod() {
        userId = UserOperations.createUserAndReturnUserId(RandomCustomerData.getRandomName(), RandomCustomerData.getRandomCustomerEmail(), "Test@1", Language.EN, Country.DE);
        productId = ProductOperations.createProductAndReturnProductId("Test Product");
        germanReviewId = ReviewOperations.createReviewAndReturnReviewIdWithAttachment(userId, productId, ratingGerman, "Test headline German", testVideo, "besten Testdaten der Welt");
        englishReviewId = ReviewOperations.createReviewAndReturnReviewIdWithAttachment(userId, productId, ratingEnglish, "Test headline English", testImage, "best test data in the world");
        italianReviewId = ReviewOperations.createReviewAndReturnReviewIdWithoutAttachment(userId, productId, ratingItalian, "Test headline Italian", "i migliori dati di test al mondo");
    }

    @Test(description = "Test-SCN-01 checking if reviews are listed as user's preferences")
    public void reviewShouldBeListedBySelectedCountryAndLanguage() {
        JsonPath allReviews = ReviewOperations.getAllReviewsByProductId(productId);

        //Asserting italian review is not showing to user (requirement: AMZ-CMT-002)
        Assert.assertNull(allReviews.getJsonObject(String.valueOf(italianReviewId)));
        Assert.assertNotNull(allReviews.getJsonObject(String.valueOf(germanReviewId)));
        Assert.assertNotNull(allReviews.getJsonObject(String.valueOf(englishReviewId)));

        //Asserting product rating
        Assert.assertEquals(ReviewOperations.getReviewRatingByProductId(productId), (ratingEnglish + ratingGerman + ratingItalian) / 3);

        JSONObject germanReview = allReviews.getJsonObject(String.valueOf(germanReviewId));
        JSONObject englishReview = allReviews.getJsonObject(String.valueOf(englishReviewId));

        //Asserting review data
        Assert.assertEquals(germanReview.get("headLine"), "Test headline German");
        Assert.assertEquals(germanReview.get("review"), "besten Testdaten der Weltn");
        Assert.assertEquals(germanReview.get("rating"), ratingGerman);
        Assert.assertEquals(englishReview.get("headLine"), "Test headline English");
        Assert.assertEquals(englishReview.get("review"), "best test data in the world");
        Assert.assertEquals(englishReview.get("rating"), ratingEnglish);
    }

    @Test(description = "Test-SCN-01 checking if reviews are filtered as user's preferences")
    public void reviewShouldBeFilteredBySelectedCountryAndLanguage() {
        JsonPath germanReviews = ReviewOperations.filterReviewsByLanguage(productId, Language.DE);
        JsonPath englishReviews = ReviewOperations.filterReviewsByLanguage(productId, Language.EN);

        //Asserting filter returns correct language (requirement: AMZ-CMT-001)
        Assert.assertNotNull(germanReviews.getJsonObject(String.valueOf(germanReviewId)));
        Assert.assertNull(germanReviews.getJsonObject(String.valueOf(englishReviewId)));
        Assert.assertNotNull(englishReviews.getJsonObject(String.valueOf(englishReviewId)));
        Assert.assertNull(englishReviews.getJsonObject(String.valueOf(germanReviewId)));

        JSONObject germanReview = germanReviews.getJsonObject(String.valueOf(germanReviewId));
        JSONObject englishReview = englishReviews.getJsonObject(String.valueOf(englishReviewId));

        //Asserting review data
        Assert.assertEquals(germanReview.get("headLine"), "Test headline German");
        Assert.assertEquals(germanReview.get("review"), "besten Testdaten der Weltn");
        Assert.assertEquals(germanReview.get("rating"), ratingGerman);
        Assert.assertEquals(englishReview.get("headLine"), "Test headline English");
        Assert.assertEquals(englishReview.get("review"), "best test data in the world");
        Assert.assertEquals(englishReview.get("rating"), ratingEnglish);
    }

}
