package me.xethh.libs.mtt.MessageTransferTool.base;

public abstract class Msg<Data> extends MDCEntity{
    protected String id;
    protected Data data;
    protected Meta meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
