package helpers.enums;

public enum Language {

    EN("English"),
    DE("German"),
    IT("Italian");

    private final String language;

    Language(final String language) {
        this.language = language;
    }

    public String get() {
        return language;
    }

}
