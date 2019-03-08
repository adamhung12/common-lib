package me.xethh.libs.toolkits.stopWatchEx;

import java.util.List;

public interface StopWatchEx {
    StopWatchEx start();
    StopWatchEx stop();
    Long nano();
    Long milis();
    Long seconds();
    Long minutes();
    Long hours();
    Long days();

    StopWatchEx addLoggers(StopWatchExLogger logger);
    List<StopWatchExLogger> getLoggers();
    default StopWatchEx log(){
        getLoggers().forEach(log->log.log(this));
        return this;
    }

    static StopWatchEx get(){
        return new StopWatchExImpl();
    }
}
