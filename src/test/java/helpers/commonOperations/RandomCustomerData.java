package helpers.commonOperations;

import baseSettings.BaseSettings;

import java.util.Random;

public class RandomCustomerData extends BaseSettings {

    public static String getRandomCustomerEmail() {
        return getRandomStringValue(10) + "@amazon.com";
    }

    public static String getRandomName() {
        return "Test User " + getRandomStringValue(5);
    }

    private static String getRandomStringValue(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
