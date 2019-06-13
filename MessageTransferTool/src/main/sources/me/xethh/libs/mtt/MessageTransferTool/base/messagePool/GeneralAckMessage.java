package me.xethh.libs.mtt.MessageTransferTool.base.messagePool;

import me.xethh.libs.mtt.MessageTransferTool.base.Ack;
import me.xethh.libs.mtt.MessageTransferTool.base.AckMessage;

public class GeneralAckMessage extends Ack {
    private String id;
    private AckMessage message;

    public GeneralAckMessage(String id, AckMessage message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public AckMessage getMessage() {
        return message;
    }
}
