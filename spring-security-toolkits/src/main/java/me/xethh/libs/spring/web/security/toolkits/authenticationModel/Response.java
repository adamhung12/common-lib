package me.xethh.libs.spring.web.security.toolkits.authenticationModel;

public interface Response<ResponseData> {
    ResponseData getData();
    void setData(ResponseData data);
}
