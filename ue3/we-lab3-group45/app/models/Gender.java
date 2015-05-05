package models;

import play.i18n.Messages;

/**
 * Created by Elisabeth on 03.05.2014.
 */
public enum Gender {
    FEMALE(Messages.get("female")),
    MALE(Messages.get("male"));

    String text;

    Gender(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
