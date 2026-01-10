package hooks;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

public class GlobalHooks {
	
	@BeforeAll
	public static void loadConfig() {
		System.out.println("This is to load environment specific property file. Example: dev, val etc");		
	}	
		
	@BeforeAll
	public static void loggingConfig() {
		System.out.println("This is to load log4j2 logging configuration file");
	}
	
	@BeforeAll
	public static void extentReportConfig() {
		System.out.println("This is to load Extent Report configuration file");
	}
	
	@AfterAll
	public static void globalTeardown() {
		System.out.println("Cleaning up resources including env, log4j2 and Extent Report.");
		System.out.println("Order will figure out during actual implementation.");
	}
	

}
