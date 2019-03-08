package me.xethh.libs.toolkits.stopWatchEx;

import org.apache.commons.lang3.time.StopWatch;

import java.util.LinkedList;
import java.util.List;

public class StopWatchExImpl implements StopWatchEx{
    private List<StopWatchExLogger> loggers = new LinkedList<>();
    private StopWatch stopWatch = new StopWatch();

    @Override
    public StopWatchEx start() {
        stopWatch.start();
        return this;
    }

    @Override
    public StopWatchEx stop() {
        stopWatch.stop();
        return this;
    }

    @Override
    public Long nano() {
        return stopWatch.getNanoTime();
    }

    @Override
    public Long milis() {
        return stopWatch.getTime();
    }

    @Override
    public Long seconds() {
        return milis()/1000;
    }

    @Override
    public Long minutes() {
        return seconds()/60;
    }

    @Override
    public Long hours() {
        return seconds()/60;
    }

    @Override
    public Long days() {
        return hours()/24;
    }

    @Override
    public StopWatchEx addLoggers(StopWatchExLogger logger) {
        this.loggers.add(logger);
        return this;
    }

    @Override
    public List<StopWatchExLogger> getLoggers() {
        return loggers;
    }

    @Override
    public StopWatchEx log() {
        loggers.forEach(x->x.log(this));
        return this;
    }
}
