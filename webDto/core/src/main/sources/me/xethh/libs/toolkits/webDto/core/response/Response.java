package me.xethh.libs.toolkits.webDto.core.response;

import me.xethh.libs.toolkits.webDto.core.WebBaseEntity;
import me.xethh.libs.toolkits.webDto.core.response.err.ResponseError;
import me.xethh.libs.toolkits.webDto.core.response.status.ResponseStatus;

public class Response<Data, Err extends ResponseError> extends WebBaseEntity {
    public Response(ResponseStatus status, Data result, Err error) {
        this.status = status;
        this.result = result;
        this.error = error;
    }

    private ResponseStatus status;
    private Data result;
    private Err error;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Data getResult() {
        return result;
    }

    public void setResult(Data result) {
        this.result = result;
    }

    public Err getError() {
        return error;
    }

    public void setError(Err error) {
        this.error = error;
    }
}
