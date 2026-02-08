/**
 * Orchestrates the complete login flow including validation
 * This is NOT a Page Object - it's a flow coordinator
 */
package com.cro.pages;

import java.io.IOException;

import com.cro.base.BasePage;
import com.cro.settings.PropertiesLoader;
import com.cro.utils.UIActions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

public class LoginPage extends BasePage {

    private static final String ADFS_ACTIVE_DIRECTORY="text=Active Directory";
	private static final String USERNAME = "#userNameInput";
    private static final String PASSWORD = "#passwordInput"; 
    private static final String TENANT_POPUP="#showMultiTenantPopup";
    private static final String USER_ICON=".dropdown-toggle i.lnr.icon-user";    
    private static final String USER_PROFILE_LINK = "text=User Profile";
    private static final String USER_PROFILE_HEADING = "User Profile";
    private static final String CLOSE_BUTTON = "text=Close";
    private  String TENANT_NAME = null;
 

    public LoginPage(UIActions uiActions) {
        super(uiActions);
    }
    
    /**
     * Perform login actions
     * @param user - Username/Email
     * @param pass - Password 
     * Click SignIn using JS way for greater stability
     * Handle tenant selection popup
     */
    
    public void login(String user, String pass) throws IOException {
    	System.out.println(
    	        "[PAGE] LoginPage.login() | pageHash=" +
    	        System.identityHashCode(uiActions) +
    	        " thread=" + Thread.currentThread().getName()
    	    );
    	
    	//clicking on ADFS Active directory
    	uiActions.click(ADFS_ACTIVE_DIRECTORY);
    	    	
    	// Fill username and password using selectors directly
    	uiActions.fill(USERNAME,user );
    	uiActions.fill(PASSWORD, pass);
    	
    	// Click Sign In using JavaScript (workaround for problematic locator)
        submitLoginViaJavaScript();
       
        // Wait for page to load completely
        uiActions.waitForLoadState(LoadState.NETWORKIDLE);
        
        //choose tenant (optional in case of multi-tenant activated for the user)
        uiActions.handleTenantSelection(TENANT_POPUP);
        
      //This wait needed before clicking on user icon, since kendo still loading otherwise the spinner become infinite
        uiActions.waitForAllKendoLoadersComplete();       
    	
    }
    
    /**
     * Validate logged-in user email matches expected email
     * Opens user profile, grabs email, validates, and closes profile
     * @param expectedEmail - Email that should match the logged-in user
     * @return true if email matches, false otherwise
     */
    
    public boolean validateUserEmail(String expectedEmail) {
    	try {
            // Hover on user icon to show dropdown menu
            uiActions.mouseHover(USER_ICON);
            
            // Click User Profile link
            uiActions.click(USER_PROFILE_LINK);
            
            // Wait for User Profile dialog to be visible
            uiActions.waitForElementWithRole(AriaRole.HEADING, USER_PROFILE_HEADING);
            
            // Get email from profile
            String actualEmail = uiActions.getTextByPartialMatch(expectedEmail);
            
            // Close profile dialog
            uiActions.click(CLOSE_BUTTON);
            
            // Validate email match
            boolean isMatch = expectedEmail.trim().equalsIgnoreCase(actualEmail.trim());
            
            System.out.println(
                "[VALIDATION] Expected: " + expectedEmail + 
                " | Actual: " + actualEmail + 
                " | Match: " + isMatch
            );
            
            return isMatch;
            
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to validate user email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Submit login using JavaScript - workaround for unreliable Sign In button locator
     */
    private void submitLoginViaJavaScript() {
        try {
            // Wait for Login object to be available
            uiActions.waitForFunction("typeof Login !== 'undefined' && Login.submitLoginRequest !== undefined");
            
            // Execute login submission
            uiActions.executeScriptAndWaitForNavigation("Login.submitLoginRequest()");
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit login via JavaScript", e);
        }
    } 
}



