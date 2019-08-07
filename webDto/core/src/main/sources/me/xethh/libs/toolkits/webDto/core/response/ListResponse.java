package me.xethh.libs.toolkits.webDto.core.response;

import me.xethh.libs.toolkits.webDto.core.response.Response;
import me.xethh.libs.toolkits.webDto.core.response.err.ResponseError;
import me.xethh.libs.toolkits.webDto.core.response.status.ResponseStatus;

import java.util.List;

public abstract class ListResponse<Data, Err extends ResponseError> extends Response<List<Data>, Err> {
    public ListResponse(ResponseStatus status, List<Data> result, Err error) {
        super(status, result, error);
    }
}
