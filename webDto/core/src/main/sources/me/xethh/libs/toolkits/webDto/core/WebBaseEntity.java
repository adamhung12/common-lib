package me.xethh.libs.toolkits.webDto.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class WebBaseEntity {
    private String id;
    @JsonIgnore
    private MetaEntity meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MetaEntity getMeta() {
        return meta;
    }

    public void setMeta(MetaEntity meta) {
        this.meta = meta;
    }
}
