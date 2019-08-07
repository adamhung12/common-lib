package me.xethh.libs.toolkits.webDto.core.general.sort;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author xethhung
 * date 5/7/2018
 */
public class Sorting {
    @JsonProperty("field")
    private String fieldName;
    private SortDirection direction;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }
}
