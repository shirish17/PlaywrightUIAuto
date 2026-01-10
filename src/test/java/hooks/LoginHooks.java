package hooks;

import io.cucumber.java.Before;

public class LoginHooks {
	
	@Before(order=1)
	public void loginToAppliation() {
		System.out.println("This will login to the application.");
	}

}
