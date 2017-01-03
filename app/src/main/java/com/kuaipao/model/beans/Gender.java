package com.kuaipao.model.beans;

/**
 * Created by pingfu on 16-8-11.
 */
public enum Gender {
    MALE(1, "m"),

    FEMALE(2, "f");

    private final int sex;

    private final String sexName;

    Gender(int sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }

    public int getSex() {
        return sex;
    }

    public String getSexName() {
        return sexName;
    }
}
