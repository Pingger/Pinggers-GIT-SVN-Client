package info.iskariot.pgsc.util;

/**
 * 
 */

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.*;
import java.util.logging.Formatter;

/**
 * Pinggers Logger Klasse
 * 
 * @author Pingger
 *
 */
@SuppressWarnings("unused")
public class PWLogger
{
	private static final String					   ANSI_CLS		   = PWLogger.ANSI_ESCAPE + "2J";						//$NON-NLS-1$
	private static final String					   ANSI_ESCAPE	   = (char) 27 + "[";									//$NON-NLS-1$
	private static final String					   ANSI_ESCAPE_END = "m";												//$NON-NLS-1$
	private static final String					   ANSI_HOME	   = PWLogger.ANSI_ESCAPE + "H";						//$NON-NLS-1$
	private static final String					   ANSI_RESET	   = PWLogger.ANSI_ESCAPE + "0"							//$NON-NLS-1$
			+ PWLogger.ANSI_ESCAPE_END;
	private static final String					   BG_BLACK		   = "40";												//$NON-NLS-1$
	private static final String					   BG_BLUE		   = "44";												//$NON-NLS-1$
	private static final String					   BG_CYAN		   = "46";												//$NON-NLS-1$
	private static final String					   BG_GREEN		   = "42";												//$NON-NLS-1$
	private static final String					   BG_MAGENTA	   = "45";												//$NON-NLS-1$
	private static final String					   BG_RED		   = "41";												//$NON-NLS-1$
	private static final String					   BG_WHITE		   = "47";												//$NON-NLS-1$
	private static final String					   BG_YELLOW	   = "43";												//$NON-NLS-1$
	private static final String					   FG_BLACK		   = "30";												//$NON-NLS-1$
	private static final String					   FG_BLUE		   = "34";												//$NON-NLS-1$
	private static final String					   FG_CYAN		   = "36";												//$NON-NLS-1$
	private static final String					   FG_GREEN		   = "32";												//$NON-NLS-1$
	private static final String					   FG_MAGENTA	   = "35";												//$NON-NLS-1$
	private static final String					   FG_RED		   = "31";												//$NON-NLS-1$
	private static final String					   FG_WHITE		   = "37";												//$NON-NLS-1$
	private static final String					   FG_YELLOW	   = "33";												//$NON-NLS-1$
	private static final Hashtable<String, Logger> htLoggers	   = new Hashtable<>();
	private static final Lock					   recordLock	   = new ReentrantLock();
	private static int							   recordOffset	   = 0;
	private static final LogRecord[ ]			   records		   = new LogRecord[65536];
	/** The Outputformat of the Timestamps */
	public static final SimpleDateFormat		   sdf			   = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.SSS]");	//$NON-NLS-1$
	private static final Comparator<LogRecord>	   sortRecords	   = (o1, o2) -> {
																	   if(o1 == null || o2 == null)
																	   {
																		   if(o1 != null) { return -1; }
																		   if(o2 != null) { return 1; }
																		   return 0;
																	   }
																	   return Long.compare(o1.getSequenceNumber(),
																			   o2.getSequenceNumber());
																	   
																   };
	
	/**
	 * Sends the ANSI codes to clear Screen and put Cursor to HOME-Location
	 */
	public static void clearScreen()
	{
		System.out.println(PWLogger.ANSI_CLS + PWLogger.ANSI_HOME);
	}
	
	/**
	 * Converts a {@link Throwable} to a {@link String} using a {@link StringBuilder}
	 * 
	 * @param t
	 *            the {@link Throwable} to convert
	 * @param dejaVu
	 *            Any {@link List} to allow registering of Circular References
	 * @param builder
	 *            the {@link StringBuilder} to write the resulting String to
	 * @param linePrefix
	 *            the prefix to write at the begging of each new line
	 * @param lineSuffix
	 *            the suffix to write at the end of each line
	 * @param caption
	 *            the Caption for the {@link Throwable}
	 * 
	 * @see java.lang.Throwable printEnclosedStackTrace(PrintStreamOrWriter, StackTraceElement[], String, String, Set
	 *      <Throwable>)
	 */
	public static void convertThrowable(Throwable t, List<Throwable> dejaVu, StringBuilder builder, String linePrefix,
			String lineSuffix,
			String caption)
	{
		if(dejaVu.contains(t))
		{
			String[ ] toString = t.toString().replace("\r", "").split("\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			for (String line : toString)
			{
				builder.append(linePrefix + "\t[CIRCULAR REFERENCE:" + line + "]" + System.lineSeparator()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		else
		{
			dejaVu.add(t);
			String[ ] toString = t.toString().replace("\r", "").split("\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			for (String line : toString)
			{
				builder.append(linePrefix + line + lineSuffix + System.lineSeparator());
			}
			StackTraceElement[ ] trace = t.getStackTrace();
			for (StackTraceElement traceElement : trace)
			{
				builder.append(linePrefix + "\tat " + traceElement + lineSuffix + System.lineSeparator()); //$NON-NLS-1$
			}
			
			// Print suppressed exceptions, if any
			for (Throwable se : t.getSuppressed())
			{
				PWLogger.convertThrowable(se, dejaVu, builder, linePrefix + "\t", lineSuffix, "Suppressed: "); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			// Print cause, if any
			Throwable ourCause = t.getCause();
			if(ourCause != null)
			{
				PWLogger.convertThrowable(ourCause, dejaVu, builder, linePrefix, lineSuffix, "Caused by: "); //$NON-NLS-1$
			}
			
		}
	}
	
	/**
	 * @return the initial names of all known Loggers
	 */
	public static String[ ] getAllKnownLoggerNames()
	{
		return PWLogger.htLoggers.keySet().toArray(new String[0]);
	}
	
	/**
	 * @return all known Loggers
	 */
	public static Logger[ ] getAllKnownLoggers()
	{
		return PWLogger.htLoggers.values().toArray(new Logger[0]);
	}
	
	/**
	 * Returns the Last 65536 Log-Entries (regardless of LogLevel!) in Order of their timestamps. If less than 65536
	 * Entries were logged, the Array will be filled with <b>null</b> Entries.
	 * 
	 * @return the sorted Array of LogEntries
	 */
	public static LogRecord[ ] getLog()
	{
		LogRecord[ ] log;
		synchronized (PWLogger.records)
		{
			log = PWLogger.records.clone();
		}
		Arrays.sort(log, PWLogger.sortRecords);
		return log;
	}
	
	/**
	 * Wraps the Logger.getLogger(...) method, so that Loggers can be configured from any class or object without having
	 * to fear, that they cause issues with weird duplicates
	 * 
	 * @param LoggerName
	 *            the initial Name of the Logger to get
	 * @return the Logger with the given Name
	 */
	public static synchronized Logger getLogger(String LoggerName)
	{
		if(!PWLogger.htLoggers.containsKey(LoggerName))
		{
			PWLogger.htLoggers.put(LoggerName, Logger.getLogger(LoggerName));
		}
		return PWLogger.htLoggers.get(LoggerName);
	}
	
	/**
	 * Sets up the Logger. Should be called right at the Start of the Application (namely main(args))
	 */
	public static void setupLogger()
	{
		System.out.println(
				"Setting up Pinggers Logger. This Logger uses ANSI-Codes to operate, which are not supported on Windows!"); //$NON-NLS-1$
		Handler h = new ConsoleHandler()
		{
			@Override
			public void publish(LogRecord record)
			{
				if(record == null) { return; }
				setLevel(Logger.getGlobal().getLevel() == null ? Level.INFO : Logger.getGlobal().getLevel());
				PWLogger.recordLock.lock();
				try
				{
					PWLogger.records[PWLogger.recordOffset] = record;
					PWLogger.recordOffset = (PWLogger.recordOffset + 1) % PWLogger.records.length;
				}
				finally
				{
					PWLogger.recordLock.unlock();
				}
				super.publish(record);
			}
		};
		// adapted from here:
		// http://stackoverflow.com/questions/7445658/how-to-detect-if-the-console-does-support-ansi-escape-codes-in-python
		final boolean supportsANSI = !System.getProperty("os.name").toLowerCase().startsWith("windows") //$NON-NLS-1$ //$NON-NLS-2$
				|| System.getenv("ANSICON") != null; //$NON-NLS-1$
		h.setFormatter(new Formatter()
		{
			
			@Override
			public String format(LogRecord record)
			{
				String linePrefix = ""; //$NON-NLS-1$
				if(supportsANSI)
				{
					linePrefix = PWLogger.ANSI_ESCAPE;
					if(record.getLevel().intValue() >= Level.OFF.intValue())
					{
						linePrefix += PWLogger.FG_BLACK + ";" + PWLogger.BG_YELLOW; //$NON-NLS-1$
					}
					else if(record.getLevel().intValue() >= Level.SEVERE.intValue())
					{
						linePrefix += PWLogger.FG_BLACK + ";" + PWLogger.BG_MAGENTA; //$NON-NLS-1$
					}
					else if(record.getLevel().intValue() >= Level.WARNING.intValue())
					{
						linePrefix += PWLogger.FG_RED + ";" + PWLogger.BG_BLACK; //$NON-NLS-1$
					}
					else if(record.getLevel().intValue() >= Level.INFO.intValue())
					{
						linePrefix += PWLogger.FG_WHITE + ";" + PWLogger.BG_BLACK; //$NON-NLS-1$
					}
					else if(record.getLevel().intValue() >= Level.CONFIG.intValue())
					{
						linePrefix += PWLogger.FG_YELLOW + ";" + PWLogger.BG_BLACK; //$NON-NLS-1$
					}
					else
					{
						linePrefix += PWLogger.FG_CYAN + ";" + PWLogger.BG_BLACK; //$NON-NLS-1$
					}
					linePrefix += PWLogger.ANSI_ESCAPE_END;
					
				}
				linePrefix += PWLogger.sdf.format(record.getMillis()) + "\t[" + record.getLoggerName() + "]\t[" //$NON-NLS-1$ //$NON-NLS-2$
						+ record.getLevel().getName() + "]\t"; //$NON-NLS-1$
				StringBuilder sb = new StringBuilder();
				for (String line : record.getMessage().split(System.lineSeparator()))
				{
					sb.append(linePrefix);
					sb.append(line);
					sb.append(supportsANSI ? PWLogger.ANSI_RESET : ""); //$NON-NLS-1$
					sb.append(System.lineSeparator());
				}
				// Fully Log Exceptions
				if(record.getThrown() != null)
				{
					PWLogger.convertThrowable(record.getThrown(), new ArrayList<Throwable>(), sb, linePrefix,
							supportsANSI ? PWLogger.ANSI_RESET : "", ""); //$NON-NLS-1$ //$NON-NLS-2$
				}
				return sb.toString() + (supportsANSI ? PWLogger.ANSI_RESET : ""); //$NON-NLS-1$
			}
		});
		Logger l = Logger.getGlobal();
		while (l.getParent() != null)
		{
			l = l.getParent();
		}
		l.removeHandler(l.getHandlers()[0]);
		l.addHandler(h);
		l.setUseParentHandlers(false);
	}
}