package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HomePageSteps {
	@Then("the company logo with {string} be visible")
	public void the_company_logo_with_be_visible(String string) {
		System.out.println("the company logo with {string} be visible");
	}
	@When("I click on the {string} logo")
	public void i_click_on_the_logo(String string) {
	    System.out.println("I click on the {string} logo");
	    
	}
	@Then("I should land on the page containing header {string}")
	public void i_should_land_on_the_page_containing_header(String string) {
		System.out.println("I should land on the page containing header {string}");	   
	}
	@Then("the {string}> link should be visible")
	public void the_link_should_be_visible(String string) {
	    System.out.println("the {string}> link should be visible");
	}
	@When("When the user clicks the {string} link")
	public void when_the_user_clicks_the_link (String string) {
	    System.out.println("I click the {string} link");
	}
	@Then("I should land on the {string} page")
	public void i_should_land_on_the_page(String string) {
		System.out.println("I should land on the {string} page");	    
	}

}
