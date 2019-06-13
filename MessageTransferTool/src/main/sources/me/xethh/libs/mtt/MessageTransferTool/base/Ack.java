package me.xethh.libs.mtt.MessageTransferTool.base;

public abstract class Ack extends MDCEntity{
    public abstract String getId();
    public abstract AckMessage getMessage();
}
