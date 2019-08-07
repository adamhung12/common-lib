package me.xethh.libs.toolkits.webDto.core.response;

import me.xethh.libs.toolkits.webDto.core.general.page.PagedObject;
import me.xethh.libs.toolkits.webDto.core.response.err.ResponseError;
import me.xethh.libs.toolkits.webDto.core.response.status.ResponseStatus;

public abstract class PagingResponse<Data, Err extends ResponseError> extends ObjectResponse<PagedObject<Data>, Err> {
    public PagingResponse(ResponseStatus status, PagedObject<Data> result, Err error) {
        super(status, result, error);
    }
}
