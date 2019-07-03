package me.xethh.libs.toolkits.stopWatchEx;

import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

public class StopWatchExWithStatusLogImpl implements StopWatchEx, StartStopLabeling {
    SimpleDateFormat sdf = DateFormatBuilder.Format.ISO8601.getFormatter();
    private Logger logger;
    private String labelName;
    private Supplier<Long> sequence;
    private Long start,end;

    public StopWatchExWithStatusLogImpl(Logger logger, Supplier<Long> sequence, String labelName){
        this.labelName = labelName;
        this.sequence = sequence;
        this.logger = logger;
    }
    public StopWatchEx start() {
        logger.info(startLabel());
        start = System.nanoTime();
        return this;
    }

    public StopWatchEx stop() {
        end = System.nanoTime();
        logger.info(stopLabel());
        return this;
    }

    @Override
    public StopWatchEx reset() {
        start=0l;
        end = 0l;
        return this;
    }

    @Override
    public Long nano() {
        return end-start;
    }


    @Override
    public String getLabel() {
        return labelName;
    }

    @Override
    public String startLabel() {
        return String.format("|>>%s>|%s|%09d|%d", labelName,sdf.format(new Date()), sequence, start);
    }

    @Override
    public String stopLabel() {
        return String.format("|<<%s<|%s|%09d|%d", labelName,sdf.format(new Date()), sequence, end);
    }
}
