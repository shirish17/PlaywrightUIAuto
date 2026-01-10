package hooks;

import io.cucumber.java.Before;

public class DBHooks {
	
	@Before(order=2)
	public void connectToDB() {
		System.out.println("This will connect to application database.");
	}

}
