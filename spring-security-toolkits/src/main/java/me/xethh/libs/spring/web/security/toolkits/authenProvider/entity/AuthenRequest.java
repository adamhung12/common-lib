package me.xethh.libs.spring.web.security.toolkits.authenProvider.entity;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import me.xethh.libs.spring.web.security.toolkits.authenticationModel.Request;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operationType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthenOperation.AuthenRequest.class, name = "Authen")
})
public interface AuthenRequest extends Request {
    enum OperationType {
        Authen;

        @Override
        public String toString() {
            return String.format("OperationType{%s}", this.name());
        }

    }

    AuthenOperation.RequestMeta getMetaInfo();
    void setMetaInfo(AuthenOperation.RequestMeta metaInfo);
    void setOperationType(OperationType operationType);
    OperationType operationType();
}
