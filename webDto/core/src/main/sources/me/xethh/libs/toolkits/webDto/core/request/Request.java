package me.xethh.libs.toolkits.webDto.core.request;

import me.xethh.libs.toolkits.webDto.core.WebBaseEntity;

public abstract class Request<Data> extends WebBaseEntity {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
