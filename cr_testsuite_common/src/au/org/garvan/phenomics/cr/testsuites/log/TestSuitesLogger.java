package au.org.garvan.phenomics.cr.testsuites.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TestSuitesLogger<T> {

	private Logger logger = null;

	private static Map<String, TestSuitesLogger<?>> loggers = new HashMap<String, TestSuitesLogger<?>>();

	private TestSuitesLogger(Class<T> c) {
		this.logger = Logger.getLogger(c);
	}

	public static TestSuitesLogger<?> getLogger(Class<?> c) {
		String className = c.getName();
		TestSuitesLogger<?> logger = loggers.get(className);
		if (logger == null) {
			synchronized (TestSuitesLogger.class) {
				logger = loggers.get(className);
				if (logger == null) {
					logger = new TestSuitesLogger<>(c);
					loggers.put(className, logger);
				}
			}
		}
		return logger;
	}

	public boolean isFatalLoggable() {
		return logger.isEnabledFor(Level.FATAL);
	}

	public boolean isErrorLoggable() {
		return logger.isEnabledFor(Level.ERROR);
	}

	public boolean isWarnLoggable() {
		return logger.isEnabledFor(Level.WARN);
	}

	public boolean isInfoLoggable() {
		return logger.isEnabledFor(Level.INFO);
	}

	public boolean isDebugLoggable() {
		return logger.isEnabledFor(Level.DEBUG);
	}

	public boolean isTraceLoggable() {
		return logger.isEnabledFor(Level.TRACE);
	}

	public void fatal(String message) {
		logger.fatal(message);
	}

	public void fatal(String message, Throwable t) {
		logger.fatal(message, t);
	}

	public void error(String message) {
		logger.error(message);
	}

	public void error(String message, Throwable t) {
		logger.error(message, t);
	}

	public void warn(String message) {
		logger.warn(message);
	}

	public void warn(String message, Throwable t) {
		logger.warn(message, t);
	}

	public void info(String message) {
		logger.info(message);
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void trace(String message) {
		logger.trace(message);
	}

	public static void setUpLogger(String level) {
		Properties properties = new Properties();
		properties.put("log4j.rootLogger", level + ", Console");
		properties.put("log4j.appender.Console",
				"org.apache.log4j.ConsoleAppender");
		properties.put("log4j.appender.Console.layout",
				"org.apache.log4j.PatternLayout");
		properties.put("log4j.appender.Console.layout.ConversionPattern",
				"%-5p %c{1} %x - %m%n");
		PropertyConfigurator.configure(properties);
	}

	public static void setUpLoggerFromProperties() throws IOException {
		String path = "LogConfig.properties";
		Properties properties = new Properties();
		properties.load(TestSuitesLogger.class.getClassLoader()
				.getResourceAsStream(path));
		synchronized (TestSuitesLogger.class) {
			PropertyConfigurator.configure(properties);
			loggers.clear();
		}
	}
}
