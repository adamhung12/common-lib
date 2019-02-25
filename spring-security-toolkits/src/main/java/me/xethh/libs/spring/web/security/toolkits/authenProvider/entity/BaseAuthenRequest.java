package me.xethh.libs.spring.web.security.toolkits.authenProvider.entity;

public abstract class BaseAuthenRequest implements AuthenRequest{
    protected AuthenOperation.RequestMeta metaInfo = new AuthenOperation.RequestMeta();
    protected OperationType operationType;

    public BaseAuthenRequest(OperationType operationType) {
        this.operationType = operationType;
    }

    public OperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
    @Override
    public OperationType operationType() {
        return OperationType.Authen;
    }

    @Override
    public AuthenOperation.RequestMeta getMetaInfo() {
        return metaInfo;
    }

    @Override
    public void setMetaInfo(AuthenOperation.RequestMeta metaInfo) {
        this.metaInfo = metaInfo;
    }
}

