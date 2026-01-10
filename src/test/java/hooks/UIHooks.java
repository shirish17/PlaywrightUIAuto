package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class UIHooks {
	
	@Before(order=0)
	public void playwrightSetup() {
		System.out.println("This is to initialize playwright, context and page for browser interaction.");
	}
	
	@After
	public void tearDown() {
		System.out.println("cleaning up playwright and database related resources.");
		
	}

}
