package me.xethh.libs.toolkits.webDto.core.response;

import lombok.Getter;
import lombok.Setter;
import me.xethh.libs.toolkits.webDto.core.WebBaseEntity;
import me.xethh.libs.toolkits.webDto.core.response.status.ResponseStatus;

@Setter
@Getter
public class Response<Data, Err> extends WebBaseEntity {
    private ResponseStatus status;
    private Data result;
    private Err error;
}
