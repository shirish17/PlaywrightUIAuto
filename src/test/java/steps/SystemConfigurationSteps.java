/**
 * Step definitions for System Configuration features
 *
 */
package steps;

import java.io.IOException;

import com.cro.pages.configuration.SystemConfigurationPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class SystemConfigurationSteps {	
	
	private final SystemConfigurationPage systemConfigPage;
	private String generatedCountryName;
	
	// Picocontainer injects SystemConfigurationPage
    public SystemConfigurationSteps(SystemConfigurationPage systemConfigPage) {
        this.systemConfigPage = systemConfigPage;
        //CLI display message for debugging
        System.out.println(
            "[STEPS] SystemConfigurationSteps | pageHash=" +
            System.identityHashCode(systemConfigPage) +
            " thread=" + Thread.currentThread().getName()
        );
    }
	
    @When("the user adds a country named {string} and activates it")
    public void the_user_adds_a_country_named_and_activates_it(String countryName) {
        System.out.println("[STEP] Adding country with template: " + countryName);
        // All navigation is hidden in page layer - business doesn't care HOW
        systemConfigPage.addCountryAndActivate(countryName);        
        System.out.println("[STEP] ✅ Country added: " + countryName);
    }
    
    @Given("the user is on the country management page")
    public void theUserIsOnTheCountryManagementPage() throws IOException {
        // If navigation is required
    	systemConfigPage.navigateToCountryManagementPage();
    }

	
	
}
