package me.xethh.libs.toolkits.stopWatchEx;

import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public interface StopWatchEx {
    StopWatchEx start();
    StopWatchEx stop();
    StopWatchEx reset();
    Long nano();
    default Long miliSecond() {
        return nano()/1000000;
    }

    default Long seconds() {
        return miliSecond()/1000;
    }

    default Long minutes() {
        return seconds()/60;
    }

    default Long hours() {
        return seconds()/60;
    }

    default Long days() {
        return hours()/24;
    }

    static StopWatchEx get(){
        return new StopWatchExImpl();
    }

    Supplier<Supplier<Long>> longProvider = ()->{
        AtomicLong longProvider = new AtomicLong();
        return (Supplier<Long>) () -> longProvider.incrementAndGet();
    };

    static StopWatchEx get(Logger logger, String label){
        return new StopWatchExWithStatusLogImpl(logger, longProvider.get(), label);
    }
}
