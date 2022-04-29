package com.i3e3.mindlet.global.constant.message;

public enum ErrorMessage {

    DUPLICATE_ID("이미 사용 중인 아이디입니다."),
    PASSWORD_CONTAIN_ID("패스워드에 아이디가 포함되어 있습니다."),
    INVALID_REGISTER_KEY("유효하지 않은 회원가입 키입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
