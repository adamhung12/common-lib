package me.xethh.libs.mtt.MessageTransferTool.base.receiverActors;

import me.xethh.libs.mtt.MessageTransferTool.base.MDCEntity;
import me.xethh.libs.mtt.MessageTransferTool.base.Meta;

public class RemoveEntityMessage extends MDCEntity {
    public RemoveEntityMessage(Meta meta) {
        this.meta = meta;
    }

    private Meta meta;

    public Meta getMeta() {
        return meta;
    }
}
