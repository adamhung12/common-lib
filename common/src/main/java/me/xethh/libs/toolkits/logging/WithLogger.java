package me.xethh.libs.toolkits.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface WithLogger {
    default Logger logger(){
        return LoggerFactory.getLogger(this.getClass());
    }
}
