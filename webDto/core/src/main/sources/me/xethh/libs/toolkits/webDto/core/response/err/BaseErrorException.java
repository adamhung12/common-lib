package me.xethh.libs.toolkits.webDto.core.response.err;

import me.xethh.libs.toolkits.exceptions.CommonException;
import me.xethh.libs.toolkits.sysMeta.ModuleMeta;
import me.xethh.libs.toolkits.utils.strings.BatchFormatting;

public class BaseErrorException extends CommonException {
    private BaseError baseError;

    public BaseError getBaseError() {
        return baseError;
    }

    public void setBaseError(BaseError baseError) {
        this.baseError = baseError;
    }

    public BaseErrorException(BaseError baseError) {
        super();
        this.baseError = baseError;
    }

    public BaseErrorException(String message, BaseError baseError) {
        super(message);
        this.baseError = baseError;
    }

    public BaseErrorException(String message, Throwable cause, BaseError baseError) {
        super(message, cause);
        this.baseError = baseError;
    }

    public BaseErrorException(Throwable cause, BaseError baseError) {
        super(cause);
        this.baseError = baseError;
    }

    public BaseErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, BaseError baseError) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.baseError = baseError;
    }
}
