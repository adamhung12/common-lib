package me.xethh.libs.toolkits.webDto.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class WebBaseEntity {
    private String id;
    private Date start;
    private Date end;
}
