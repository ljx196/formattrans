package edu.uestc.transformation.nlp.nlpforehandle.Latex;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author: tjc
 * @create: 2020/9/22
 * @description:
 */

public class LogFactory {
    private static final Logger PERFORMANCE_LOGGER = getLogger("PerformanceLog");

    public LogFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    public static Logger getLogger(String loggerName) {
        return LogManager.getLogger(loggerName);
    }

    public static void logPerformance(String method, long time) {
        PERFORMANCE_LOGGER.info("[{}] : {}", new Object[]{method, time});
    }
}
