package com.cro.pages.configuration;

import java.io.IOException;

import com.cro.base.BasePage;
import com.cro.utils.UIActions;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

/**
 * Page Object for System Configuration page
 * Handles ALL technical navigation and interactions
 * Business layer doesn't need to know these details
 */
public class SystemConfigurationPage extends BasePage {
	// ========= System Configuration page objects definition ==============
		private static final String CONFIGURATION_LINK = "#systemTab";
		private static final String CONFIGURATION_MENU = "#systemTab >> ul.dropdown_menu";
		private static final String MENU_CONTAINER = "ul.dropdown_menu";
		private static final String LINK_ROUTER = "a[routerlink='/console/systemConfig']";
		private static final String LINK_NAME = "System Configuration";
		private static final String SYSTEM_CONFIG_URL = "**/console/systemConfig";
		private static final String TENANT_POPUP = "#showMultiTenantPopup";
		private static final String LISTS_TAB = "List(s)";
		private static final String COUNTRY_NAME = "Country Name";
		private static final String NEW_OPTIONS = "div[role='dialog']:has(h2:has-text('New Option'))";
		private static final String OPTION_NAME_INPUT = "div:has(> span:has-text('Option Name')) input.k-input-inner[type='text']";
		private static final String SAVEBTN_ON_DIALOG = "Save";
		private static final String SAVEBTN_ON_ACTIONS = "span.leftSideBarActionItemsLabel:has-text('Save')";
		private static final String AVAILABLE_OPTION_CONTAINER =
		        ":has(h2:has-text('Available Options'), " +
		        " h3:has-text('Available Options'), " +
		        " label:has-text('Available Options'), " +
		        " span:has-text('Available Options')) " +
		        " ul[role='listbox'].k-list-ul";

		private static final String AVAILABLE_OPTION_CONTAINER_BY_ROLE="ul[role='listbox'].k-list-ul"; //this is for fallback plan

	public SystemConfigurationPage(UIActions uiActions) throws IOException {
		super(uiActions);
		// Wait for page to load completely, angular and Kendo specific
		uiActions.waitForLoadState(LoadState.NETWORKIDLE);

		// First check if multi-tenant popup displays, this is because existing session
		// after login shows the popup.
		uiActions.handleTenantSelection(TENANT_POPUP);		
	}
	
	// ========== Create page level objects =============
		// list tab
		Locator listsTab = uiActions.getByRole(AriaRole.TAB, LISTS_TAB);
		
		//Country Name label
		Locator countryNameLbl = uiActions.getLocatorByExactTextMatch(COUNTRY_NAME, true);
		
		// NEW_OPTIONS from Action Menu
		Locator newOptionsLbl = uiActions.getLocatorByExactTextMatch(NEW_OPTIONS, true);
		
		// Click Save from Actions Menu
		Locator saveBtnOnActions = uiActions.locator(SAVEBTN_ON_ACTIONS);
		
		//Available options list box container
		Locator availableOptionListBox = uiActions.locator(AVAILABLE_OPTION_CONTAINER).first();
		// Available options list box container fallback strategy
		Locator availableOptionListBox_fallback=  uiActions.locator(AVAILABLE_OPTION_CONTAINER_BY_ROLE).first();

		//=====================================================
	
	//================= Country Management page navigation ==============
	
		public void navigateToCountryManagementPage() throws IOException {
			System.out.println("Success: SystemConfiguration -> navigateToCountryManagementPage (Method)");			
			
			// Navigate to System Configuration page
			navigateToSystemConfiguration();
			/*
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
            // Step 1: Navigate to System Configuration page
            navigateToSystemConfiguration();
            
            // Step 2: Navigate to Lists tab
            navigateToListsTab(listsTab);
            
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

			// Scroll and hover on System tab
			uiActions.mouseHover(CONFIGURATION_LINK);
			
			// 2) Build the menu container then constrain it with :has(...)
			Locator menu = uiActions.locator(MENU_CONTAINER);
			// menu that contains either the routerlink OR visible link text
			Locator sysMenu = uiActions.has(menu, LINK_ROUTER + ", a:has-text('" + LINK_NAME + "')");
			uiActions.waitVisible(sysMenu);

			// 3) Inside this specific menu, click the link (role preferred)
			// Locator sysConfigLink =
			uiActions.getByRoleWithin(sysMenu, AriaRole.LINK, LINK_NAME, true);
			// Fallback if accessible name doesn’t work in your app:
			Locator sysConfigLink = uiActions.within(sysMenu, LINK_ROUTER).first();

			uiActions.clickOnElement(sysConfigLink);

			// 4) Wait for SPA route + page ready
			uiActions.waitForURLRouting(SYSTEM_CONFIG_URL);
		
		} catch (Exception e) {
			throw new RuntimeException("Failed to navigate to System Configuration page", e);
		}
	}


    /*
	 * Navigate to List(s) tab
	 */
	private void navigateToListsTab(Locator listsTab) {
		uiActions.clickOnElement(listsTab);
	}

	

	/*
	 * This will select configurable option from the list
	 * Example: CountryName
	 */
	private void clickConfigurationLabel(Locator option) {
		uiActions.clickOnElement(countryNameLbl);		
	}
	
		
}
