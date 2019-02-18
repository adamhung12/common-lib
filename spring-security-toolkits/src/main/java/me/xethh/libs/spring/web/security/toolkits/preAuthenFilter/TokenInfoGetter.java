package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import me.xethh.utils.wrapper.Tuple2;

import java.util.Date;

public interface TokenInfoGetter {
    Tuple2<String, Date> getTokenInfo(String token);
}
