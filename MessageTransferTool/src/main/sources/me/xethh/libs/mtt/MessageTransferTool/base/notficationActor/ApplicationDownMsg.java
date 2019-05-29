package me.xethh.libs.mtt.MessageTransferTool.base.notficationActor;

import me.xethh.libs.mtt.MessageTransferTool.base.MDCEntity;
import me.xethh.libs.mtt.MessageTransferTool.base.receiverActors.HeartBeatEntity;

public class ApplicationDownMsg extends MDCEntity {
    protected HeartBeatEntity heartBeatEntity;

    public HeartBeatEntity getHeartBeatEntity() {
        return heartBeatEntity;
    }

    public void setHeartBeatEntity(HeartBeatEntity heartBeatEntity) {
        this.heartBeatEntity = heartBeatEntity;
    }

    @Override
    public String toString() {
        return "ApplicationDownMsg{" +
                "heartBeatEntity=" + heartBeatEntity +
                '}';
    }
}
