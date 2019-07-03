package me.xethh.libs.toolkits.stopWatchEx;

import me.xethh.libs.toolkits.cache.LRUCache;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class StopWatchExWithLoggerFactory {
    public StopWatchExWithLoggerFactory(Logger logger, Supplier<StopWatchExWithStatusLogImpl> stopWatchExSupplier){
        this(logger, stopWatchExSupplier, 5,5);
    }
    public StopWatchExWithLoggerFactory(Logger logger, Supplier<StopWatchExWithStatusLogImpl> stopWatchExSupplier, int init, int maxEntry){
        this.builder = stopWatchExSupplier;
        cache = new LRUCache.LRUMap<>(init, maxEntry);
    }

    private Supplier<StopWatchExWithStatusLogImpl> builder;
    private Logger logger;
    public StopWatchEx getNewOne(){
        return builder.get();
    }

    private LRUCache.LRUMap<String, StopWatchEx> cache;
    public static StopWatchExWithLoggerFactory getWithLabel(Logger logger, Supplier<StopWatchExWithStatusLogImpl> builder, int init, int maxEntry){
        return new StopWatchExWithLoggerFactory(logger, builder, init, maxEntry);
    }
    public static StopWatchExWithLoggerFactory getWithLabel(Logger logger, Supplier<StopWatchExWithStatusLogImpl> builder){
        return new StopWatchExWithLoggerFactory(logger, builder);
    }
}
