package me.xethh.libs.toolkits.webDto.core.request;

import lombok.Getter;
import lombok.Setter;
import me.xethh.libs.toolkits.webDto.core.WebBaseEntity;

public abstract class Request<Data> extends WebBaseEntity {
    @Setter
    @Getter
    private Data data;
}
