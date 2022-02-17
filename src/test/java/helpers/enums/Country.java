package helpers.enums;

public enum Country {

    EN("England"),
    DE("Germany"),
    IT("Italy");

    private final String country;

    Country(final String country) {
        this.country = country;
    }

    public String get() {
        return country;
    }

}
