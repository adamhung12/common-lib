package me.xethh.libs.mtt.MessageTransferTool.base;

public abstract class Ack extends MDCEntity{
    abstract String getId();
    abstract AckMessage getMessage();
}
