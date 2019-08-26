package me.xethh.libs.toolkits.aspectInterface;

import me.xethh.libs.toolkits.logging.WithLogger;
import org.aspectj.lang.ProceedingJoinPoint;

public abstract class Aspect implements WithLogger {
    public abstract Object executeTask(ProceedingJoinPoint joinPoint) throws Throwable;
    public abstract Object execute(ProceedingJoinPoint joinPoint) throws Throwable;
}
