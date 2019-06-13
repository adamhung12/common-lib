package me.xethh.libs.mtt.MessageTransferTool.commons;

public class MutableTuple2<V1,V2> extends Tuple2<V1,V2>{

    public MutableTuple2(V1 v1, V2 v2) {
        super(v1,v2);
    }

    public void setV1(V1 v1) {
        this.v1 = v1;
    }

    public void setV2(V2 v2) {
        this.v2 = v2;
    }


}
