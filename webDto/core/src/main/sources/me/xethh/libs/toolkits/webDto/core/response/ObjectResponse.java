package me.xethh.libs.toolkits.webDto.core.response;

import me.xethh.libs.toolkits.webDto.core.response.Response;
import me.xethh.libs.toolkits.webDto.core.response.err.ResponseError;
import me.xethh.libs.toolkits.webDto.core.response.status.ResponseStatus;

public abstract class ObjectResponse<Data, Err extends ResponseError> extends Response<Data, Err> {
    public ObjectResponse(ResponseStatus status, Data result, Err error) {
        super(status, result, error);
    }
}
