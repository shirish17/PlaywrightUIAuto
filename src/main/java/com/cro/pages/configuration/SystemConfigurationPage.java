package com.cro.pages.configuration;

import java.io.IOException;

import com.cro.base.BasePage;
import com.cro.utils.UIActions;
import com.microsoft.playwright.options.LoadState;

/**
 * Page Object for System Configuration page
 * Handles ALL technical navigation and interactions
 * Business layer doesn't need to know these details
 */
public class SystemConfigurationPage extends BasePage {
	// Navigation selectors
    private static final String SYSTEM_TAB = "#systemTab";
    private static final String SYSTEM_CONFIG_MENU = "ul.dropdown_menu:has(a[routerlink='/console/systemConfig'])";
    private static final String SYSTEM_CONFIG_LINK = "a[routerlink='/console/systemConfig']";
    private static final String SYSTEM_CONFIG_URL = "**/console/systemConfig";
    private static final String TENANT_POPUP="#showMultiTenantPopup";

	public SystemConfigurationPage(UIActions uiActions) throws IOException {
		super(uiActions);
		// Wait for page to load completely, angular and Kendo specific
		uiActions.waitForLoadState(LoadState.NETWORKIDLE);

		// First check if multi-tenant popup displays, this is because existing session
		// after login shows the popup.
		uiActions.handleTenantSelection(TENANT_POPUP);		
	}
	
	//========================================
	
	//================= Country Management page navigation ==============
	
		public void navigateToCountryManagementPage() throws IOException {
			System.out.println("Success: SystemConfiguration -> navigateToCountryManagementPage (Method)");			
			/*
			// Navigate to System Configuration page
			navigateToSystemConfiguration();

			// Navigate to List(s) tab
			navigateToListsTab(listsTab);
			
			//select an option from the list, here country name
			clickConfigurationLabel(countryNameLbl);
			*/
		}
	
	/**
     * BUSINESS-LEVEL METHOD
     * Add country and activate it
     * Hides all navigation complexity from business layer
     * 
     * @param countryName - Name of the country
     */
    public void addCountryAndActivate(String countryName) {
        try {
            System.out.println("[PAGE] Starting add country flow for: " + countryName);
            
            // Wait for page to load completely
            uiActions.waitForLoadState(LoadState.NETWORKIDLE);
            
          //First check if multi-tenant popup displays, this is because existing session after login shows the popup.
            uiActions.handleTenantSelection(TENANT_POPUP);
            
            
            // Step 1: Navigate to System Configuration page
            navigateToSystemConfiguration();
            
            // Step 2: Navigate to Lists tab
            //navigateToListsTab();
            
            // Step 3: Click Country Name
            //clickCountryName();
            
            // Step 4: Add the country
            //addCountry(countryName);
            
            System.out.println("[PAGE] ✅ Completed add country flow for: " + countryName);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to add and activate country: " + countryName, e);
        }
    }
 // ==================== PRIVATE NAVIGATION METHODS ====================
    // These are hidden from business layer - they're implementation details

    /**
     * Navigate to System Configuration page
     */
    private void navigateToSystemConfiguration() {
        try {
            System.out.println("[PAGE] Navigating to System Configuration page");
            Thread.sleep(5000);
            // Scroll and hover on System tab
            uiActions.mouseHover(SYSTEM_TAB); 
            Thread.sleep(10000);
            
         // Wait for dropdown menu and get locator
            uiActions.waitAndGetLocator(SYSTEM_CONFIG_MENU);   
            
            Thread.sleep(10000);
            // Click System Configuration link
            uiActions.click(SYSTEM_CONFIG_LINK);          
            
            
            // Wait for URL change (SPA navigation)
            uiActions.waitForURLRouting(SYSTEM_CONFIG_URL);            
            
            
            // Wait for page ready
            uiActions.waitForKendoAngularPageReady();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to System Configuration page", e);
        }
    }

	public void navigateToTab(String tabName) {
		// TODO Auto-generated method stub
		
	}

	

	public String generateCountryName(String countryTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
