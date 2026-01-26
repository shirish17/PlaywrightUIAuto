package hooks;
 
import java.io.IOException;

import com.cro.listeners.ScenarioContext;
import com.cro.playwright.BrowserManager;
import com.cro.settings.PropertiesLoader;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
 
public class ScenarioHooks {
 
	@Before
	public void before(Scenario scenario) throws IOException {		
		String browser = PropertiesLoader.effectiveBrowserCached();
	    BrowserManager.initBrowser(browser);
	    BrowserManager.createContext(); 
	}
 
	@BeforeStep
	public void beforeStep() {
		ScenarioContext.markStepStart();
	}
 
	@AfterStep
	public void afterStep(Scenario scenario) {
		//long ms = ScenarioContext.stepDuration();
		//LogBridge.step("Step completed in " + ms + " ms");
		//ScenarioContext.clearStepTiming();
		//LogBridge.step("Step completed");
	}
 
	@After
	public void after(Scenario scenario) {
	    BrowserManager.closeContext();
	    BrowserManager.closePlaywright();
	} 

}