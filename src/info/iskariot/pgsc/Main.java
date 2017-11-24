/**
 * 
 */
package info.iskariot.pgsc;

import java.util.Arrays;

import info.iskariot.pgsc.userInterface.Overview;
import info.iskariot.pgsc.util.PWLogger;
import javafx.application.Application;

/**
 * @author Pingger Shikkoken
 *
 */
public class Main
{
	private static String[ ] launchArgs;
	
	/**
	 * @return the launchArgs used to start the JVM
	 */
	public static String[ ] getLaunchArgs()
	{
		return Main.launchArgs.clone();
	}
	
	/**
	 * @param args
	 *            the Arguments used to start the JVM
	 * @throws InterruptedException
	 *             When something unexcpted happens
	 */
	public static void main(String[ ] args) throws InterruptedException
	{
		Main.launchArgs = args.clone();
		PWLogger.setupLogger();
		PWLogger.getLogger("Main").info("Starting");
		PWLogger.getLogger("Main").info("Startup Arguments: " + Arrays.toString(Main.launchArgs));
		Application.launch(Overview.class, args);
		PWLogger.getLogger("Main").info("Exit");
	}
}
