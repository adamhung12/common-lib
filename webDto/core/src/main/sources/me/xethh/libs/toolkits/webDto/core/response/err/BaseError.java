package me.xethh.libs.toolkits.webDto.core.response.err;

import me.xethh.libs.toolkits.sysMeta.ModuleMeta;
import me.xethh.libs.toolkits.utils.strings.BatchFormatting;

public class BaseError extends ResponseError{
    private ModuleMeta moduleMeta;
    private String[] inputs;

    public BaseError(ModuleMeta moduleMeta, String errorCode, String errorMessage, String[] inputs) {
        super(errorCode, BatchFormatting.format(errorMessage, inputs));
        this.moduleMeta = moduleMeta;
        this.inputs = inputs;
    }
}
