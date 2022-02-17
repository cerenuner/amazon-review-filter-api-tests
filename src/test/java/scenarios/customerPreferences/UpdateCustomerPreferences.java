package scenarios.customerPreferences;

import baseSettings.BaseSettings;
import helpers.ProductOperations;
import helpers.ReviewOperations;
import helpers.UserOperations;
import helpers.commonOperations.RandomCustomerData;
import helpers.enums.Country;
import helpers.enums.Language;
import io.restassured.path.json.JsonPath;
import net.minidev.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

public class UpdateCustomerPreferences extends BaseSettings {

    int userId, productId, germanReviewId, englishReviewId, italianReviewId;
    int ratingGerman = 5, ratingEnglish = 3, ratingItalian = 2;

    @BeforeMethod
    public void beforeMethod() {

        userId = UserOperations.createUserAndReturnUserId(RandomCustomerData.getRandomName(), RandomCustomerData.getRandomCustomerEmail(), "Test@1", Language.EN, Country.DE);
        productId = ProductOperations.createProductAndReturnProductId("Test Product");
        germanReviewId = ReviewOperations.createReviewAndReturnReviewIdWithoutAttachment(userId, productId, ratingGerman, "Test headline German", "besten Testdaten der Welt");
        englishReviewId = ReviewOperations.createReviewAndReturnReviewIdWithoutAttachment(userId, productId, ratingEnglish, "Test headline English", "best test data in the world");
        italianReviewId = ReviewOperations.createReviewAndReturnReviewIdWithoutAttachment(userId, productId, ratingItalian, "Test headline Italian", "i migliori dati di test al mondo");
    }

    @Test(description = "Test-SCN-02 checking if reviews are listed correctly when user updates country")
    public void reviewsShouldBeListedByUpdatedCountry() {
        //Changing user's country
        UserOperations.updateUserPreferences(userId, Language.EN, Country.IT);

        JsonPath allReviews = ReviewOperations.getAllReviewsByProductId(productId);

        //Asserting italian review is not showing to user (requirement: AMZ-CMT-002)
        Assert.assertNull(allReviews.getJsonObject(String.valueOf(germanReviewId)));
        Assert.assertNotNull(allReviews.getJsonObject(String.valueOf(italianReviewId)));
        Assert.assertNotNull(allReviews.getJsonObject(String.valueOf(englishReviewId)));

        //Asserting product rating
        Assert.assertEquals(ReviewOperations.getReviewRatingByProductId(productId), (ratingEnglish + ratingGerman + ratingItalian) / 3);

        JSONObject italianReview = allReviews.getJsonObject(String.valueOf(italianReviewId));
        JSONObject englishReview = allReviews.getJsonObject(String.valueOf(englishReviewId));

        //Asserting review data
        Assert.assertEquals(italianReview.get("headLine"), "Test headline Italian");
        Assert.assertEquals(italianReview.get("review"), "i migliori dati di test al mondo");
        Assert.assertEquals(italianReview.get("rating"), ratingItalian);
        Assert.assertEquals(englishReview.get("headLine"), "Test headline English");
        Assert.assertEquals(englishReview.get("review"), "best test data in the world");
        Assert.assertEquals(englishReview.get("rating"), ratingEnglish);
    }

    @Test(description = "Test-SCN-02 checking if reviews are filtered as user's updated preferences")
    public void reviewShouldBeFilteredBySelectedCountryAndLanguage() {
        //Changing user's country
        UserOperations.updateUserPreferences(userId, Language.EN, Country.IT);

        JsonPath italianReviews = ReviewOperations.filterReviewsByLanguage(productId, Language.IT);
        JsonPath englishReviews = ReviewOperations.filterReviewsByLanguage(productId, Language.EN);

        //Asserting filter returns correct language (requirement: AMZ-CMT-002)
        Assert.assertNotNull(italianReviews.getJsonObject(String.valueOf(italianReviewId)));
        Assert.assertNull(italianReviews.getJsonObject(String.valueOf(englishReviewId)));
        Assert.assertNotNull(englishReviews.getJsonObject(String.valueOf(englishReviewId)));
        Assert.assertNull(englishReviews.getJsonObject(String.valueOf(italianReviewId)));

        JSONObject italianReview = italianReviews.getJsonObject(String.valueOf(italianReviewId));
        JSONObject englishReview = englishReviews.getJsonObject(String.valueOf(englishReviewId));

        //Asserting review data
        Assert.assertEquals(italianReview.get("headLine"), "Test headline Italian");
        Assert.assertEquals(italianReview.get("review"), "i migliori dati di test al mondo");
        Assert.assertEquals(italianReview.get("rating"), ratingItalian);
        Assert.assertEquals(englishReview.get("headLine"), "Test headline English");
        Assert.assertEquals(englishReview.get("review"), "best test data in the world");
        Assert.assertEquals(englishReview.get("rating"), ratingEnglish);
    }

}
