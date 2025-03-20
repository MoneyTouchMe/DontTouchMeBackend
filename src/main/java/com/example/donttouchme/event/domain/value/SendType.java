package com.example.donttouchme.event.domain.value;

public enum SendType { //감사장 연락처 종류
    EMAIL("이메일"), PHONE("문자");

    private final String value;

    SendType(String value) {
        this.value = value;
    }

    public static SendType toEnum(String sendType) {
        for (SendType type : values()) {
            if (type.value.equals(sendType))
                return type;
        }
        return null;
    }

    public String toString() {
        return value;
    }
}
