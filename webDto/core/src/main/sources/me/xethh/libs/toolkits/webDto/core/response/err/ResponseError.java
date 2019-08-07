package me.xethh.libs.toolkits.webDto.core.response.err;

public class ResponseError {
    private String errCode;
    private String message;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
