package com.mjc.school.service.exceptions;

public enum ExceptionErrorCodes {
    NEWS_DOES_NOT_EXIST(Constants.ERROR_CODE_000001, "News with id %d does not exist"),
    AUTHOR_DOES_NOT_EXIST(Constants.ERROR_CODE_000002, "Author id does not exist. Author id is: %s"),
    TAG_DOES_NOT_EXIST(Constants.ERROR_CODE_000003, "Tag with id %d does not exist"),
    TITLE_LENGTH_IS_WRONG(Constants.ERROR_CODE_000004, "Title length is wrong. Title field should have length of value from 5 to 30. News title is %s"),
    CONTENT_LENGTH_IS_WRONG(Constants.ERROR_CODE_000005, "Content length is wrong. Content field should have length of value from 5 to 255. News content is %s"),
    AUTHOR_NAME_LENGTH_IS_WRONG(Constants.ERROR_CODE_000006, "Author name length is wrong. Author name field should have length of value from 3 to 15. Author name is %s"),
    VALIDATION_EXCEPTION(Constants.ERROR_CODE_000007, "Validation failed %s");

    private final String errorMessage;

    ExceptionErrorCodes(String errorCode, String message) {
        this.errorMessage = "ERROR_CODE: " + errorCode + " ERROR_MESSAGE: " + message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static class Constants {
        public static final String ERROR_CODE_000001 = "000001";
        public static final String ERROR_CODE_000002 = "000002";
        public static final String ERROR_CODE_000003 = "000003";
        public static final String ERROR_CODE_000004 = "000004";
        public static final String ERROR_CODE_000005 = "000005";
        public static final String ERROR_CODE_000006 = "000006";
        public static final String ERROR_CODE_000007 = "000007";
    }
}
