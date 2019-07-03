package me.xethh.libs.toolkits.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WithLoggerImpl implements WithLogger{
    @Override
    public Logger logger(){
        return LoggerFactory.getLogger(this.getClass());
    }
}
