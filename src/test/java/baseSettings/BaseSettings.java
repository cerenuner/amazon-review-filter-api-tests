package baseSettings;

import io.restassured.RestAssured;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Properties;

public class BaseSettings {

    private static Properties properties;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        setTestProperties();
        setEnvironmentForTest();
        for (ITestNGMethod method : context.getAllTestMethods()) {
            method.setRetryAnalyzer(new RetryAnalyzer());
        }
    }

    //TODO:add this one as config file
    protected void setTestProperties() {
        properties = new Properties();
        properties.put("baseURI", "https://www.amazon.de/");
        properties.put("usersPath", "users");
        properties.put("createReviewPath", "create-review");
        properties.put("getCustomerReviewsPath", "product-reviews");
        properties.put("getProductsPath", "products");
        properties.put("filterReviews", "filterReviews");
        properties.put("responseTimeLimit", "5000");
        properties.put("accessToken", "YOUR_ACCESS_TOKEN");
        System.getenv().forEach((key, value) -> properties.setProperty(key, value));
    }

    public static void setEnvironmentForTest() {
        RestAssured.reset();
        RestAssured.baseURI = properties.getProperty("baseURI");
    }

    public static String getAccessToken() {
        return "Bearer " + properties.getProperty("accessToken");
    }

    public static String getReviewsPath() {
        return properties.getProperty("createReviewPath");
    }

    public static String getProductsPath() {
        return properties.getProperty("getProductsPath");
    }

    public static String getUsersPath() {
        return properties.getProperty("usersPath");
    }

    public static String getTestImagePath() {
        return properties.getProperty("src/test/java/testFiles/testImage.png");
    }

    public static String getTestVideoPath() {
        return properties.getProperty("src/test/java/testFiles/testVideo.mp4");
    }

    public static Long getResponseTimeLimit() {
        return Long.parseLong(properties.getProperty("responseTimeLimit"));
    }


}
