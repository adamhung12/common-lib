package me.xethh.libs.mtt.MessageTransferTool.base;

import org.slf4j.MDC;

import java.util.Map;

public abstract class MDCEntity {
    private Map<String,String> maps;

    public Map<String, String> getMaps() {
        return maps;
    }

    protected MDCEntity(){
        this.maps = MDC.getCopyOfContextMap();
    }
}
