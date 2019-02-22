package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import org.slf4j.Logger;

public interface PerformanceLog {
    default void logStart(String label, Logger logger){
        logger.info("|>"+label+"|"+System.nanoTime()+">|");
    }
    default void logEnd(String label, Logger logger){
        logger.info("|<"+label+"|"+System.nanoTime()+"<|");
    }
    PerformanceLog staticLog = new PerformanceLog() {};
}
