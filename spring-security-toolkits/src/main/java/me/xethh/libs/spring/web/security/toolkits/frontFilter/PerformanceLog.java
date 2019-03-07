package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import org.slf4j.Logger;
import org.slf4j.MDC;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;

public interface PerformanceLog {
    default void logStart(String label, Logger logger){
        logger.info("|>"+label+"|"+ MDC.get(TRANSACTION_HEADER) +"|"+System.nanoTime()+">|");
    }
    default void logEnd(String label, Logger logger){
        logger.info("|<"+label+"|"+ MDC.get(TRANSACTION_HEADER) +"|"+System.nanoTime()+"<|");
    }
    PerformanceLog staticLog = new PerformanceLog() {};
}
