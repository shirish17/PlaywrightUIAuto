/*
 * Picocontainer will inject required dependency at class level of Pageprovider.
 * This is utility class which will perform all the relevant UI actions on the application pages.
 */
package com.cro.utils;

import java.io.IOException;

import com.cro.playwright.PageProvider;
import com.cro.settings.PropertiesLoader;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class UIActions {

	private final PageProvider pageProvider;
	private final int ElementTimeout;
	private final int PageTimeout;

	public UIActions(PageProvider pageProvider) throws IOException {
		this.pageProvider = pageProvider;
		this.PageTimeout = PropertiesLoader.getPageTimeout();
		this.ElementTimeout = PropertiesLoader.getElementTimeout();
	}

	// Page resolved ONLY when needed (after @Before)
	private Page page() {
		return pageProvider.get();
	}

	// ==================== LOCATOR CREATION METHODS ====================

	/**
	 * Create locator by CSS selector or any Playwright selector
	 * 
	 * @param selector - CSS selector, text=, has-text, etc.
	 * @return Locator
	 */
	public Locator locator(String selector) {
		return page().locator(selector);
	}

	/**
	 * Create locator by text content (exact or partial match)
	 * 
	 * @param text - Text to search for
	 * @return Locator
	 */
	public Locator getLocatorByPartialTextMatch(String text) {
		return page().getByText(text);
	}

	/**
	 * Create locator by text content with exact match option
	 * 
	 * @param text  - Text to search for
	 * @param exact - If true, requires exact match
	 * @return Locator
	 */
	public Locator getLocatorByExactTextMatch(String text, boolean exact) {
		return page().getByText(text, new Page.GetByTextOptions().setExact(exact));
	}

	/**
	 * Create locator by ARIA role
	 * 
	 * @param role - ARIA role
	 * @return Locator
	 */
	public Locator getByRole(AriaRole role) {
		return page().getByRole(role);
	}

	/**
	 * Create locator by ARIA role with options
	 * 
	 * @param role    - ARIA role
	 * @param options - GetByRole options
	 * @return Locator
	 */
	public Locator getByRole(AriaRole role, Page.GetByRoleOptions options) {
		return page().getByRole(role, options);
	}

	/**
	 * Create locator by ARIA role with name (convenience method)
	 * 
	 * @param role - ARIA role
	 * @param name - Accessible name
	 * @return Locator
	 */
	public Locator getByRole(AriaRole role, String name) {
		return page().getByRole(role, new Page.GetByRoleOptions().setName(name));
	}

	/**
	 * Create locator by ARIA role with name and exact match
	 * 
	 * @param role  - ARIA role
	 * @param name  - Accessible name
	 * @param exact - If true, requires exact match
	 * @return Locator
	 */
	public Locator getByRole(AriaRole role, String name, boolean exact) {
		return page().getByRole(role, new Page.GetByRoleOptions().setName(name).setExact(exact));
	}

	// ============= UI Standard actions ==================

	/**
	 * Click on element by selector with wait
	 * 
	 * @param selector - CSS selector, text selector or any other selector strategy
	 *                 provided by playwright
	 */
	public void click(String selector) {
		try {
			Locator locator = page().locator(selector).first();
			waitForVisible(locator, ElementTimeout);
			locator.click();
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to click element: " + selector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error clicking element: " + selector, e);
		}
	}

	/*
	 * This method will click directly on the element by considering locator
	 * strategy
	 */
	private void clickOnElement(Locator element) {
		try {
			waitForVisible(element, ElementTimeout);
			element.click();
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to click element: " + element, e);
		} catch (Exception e) {
			throw new RuntimeException("Error clicking element: " + element, e);
		}

	}

	/**
	 * Fill input field by selector
	 * 
	 * @param selector - CSS selector for the input field
	 * @param value    - Text to fill
	 */
	public void fill(String selector, String value) {
		try {
			Locator locator = page().locator(selector);
			waitForVisible(locator, ElementTimeout);
			locator.fill(value);
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to fill element: " + selector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error filling element: " + selector, e);
		}
	}

	/**
	 * Get text from element
	 * 
	 * @param selector - CSS selector
	 * @return Element text content
	 */
	public String getText(String selector) {
		try {
			Locator locator = page().locator(selector);
			waitForVisible(locator, ElementTimeout);
			return locator.textContent();
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to get text from element: " + selector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error getting text from element: " + selector, e);
		}
	}

	/**
	 * Select option from dropdown by visible text
	 * 
	 * @param selector   - CSS selector for dropdown
	 * @param optionText - Visible text of option to select
	 */
	public void selectByText(String selector, String optionText) {
		try {
			Locator locator = page().locator(selector);
			waitForVisible(locator, ElementTimeout);
			locator.selectOption(optionText);
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to select option from: " + selector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error selecting option from: " + selector, e);
		}
	}

	/*
	 * This method will hover on the element
	 */
	public void mouseHover(String selector) {
		try {
			Locator locator = page().locator(selector).first();
			waitForVisible(locator, ElementTimeout);
			locator.scrollIntoViewIfNeeded();
			locator.hover();
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to hover on element: " + selector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error while hover on element: " + selector, e);
		}

	}

	// =========== ADDED METHODS FOR LOGIN EMAIL VALIDATION ===========
	/**
	 * Wait for element with specific ARIA role to be visible
	 * 
	 * @param role - ARIA role
	 * @param name - Accessible name
	 */
	public void waitForElementWithRole(AriaRole role, String name) {
		try {
			Locator locator = page().getByRole(role, new Page.GetByRoleOptions().setName(name));
			locator.waitFor(
					new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(ElementTimeout));
		} catch (TimeoutError e) {
			throw new RuntimeException(
					"Element with role " + role + " and name '" + name + "' not visible within timeout", e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for element with role", e);
		}
	}

	/**
	 * Get text by partial text match
	 * 
	 * @param text - Partial text to search for
	 * @return Text content of the element
	 */
	public String getTextByPartialMatch(String text) {
		try {
			Locator locator = page().getByText(text);
			waitForVisible(locator, ElementTimeout);
			String content = locator.textContent();
			return content != null ? content.trim() : "";
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to get text for partial match: " + text, e);
		} catch (Exception e) {
			throw new RuntimeException("Error getting text by partial match: " + text, e);
		}
	}

	/**
	 * Get text by exact text match
	 * 
	 * @param text - Exact text to search for
	 * @return Text content of the element
	 */
	public String getTextByExactMatch(String text) {
		try {
			Locator locator = page().getByText(text, new Page.GetByTextOptions().setExact(true));
			waitForVisible(locator, ElementTimeout);
			String content = locator.textContent();
			return content != null ? content.trim() : "";
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to get text for exact match: " + text, e);
		} catch (Exception e) {
			throw new RuntimeException("Error getting text by exact match: " + text, e);
		}
	}

	/**
	 * Check if element with role exists and is visible
	 * 
	 * @param role - ARIA role
	 * @param name - Accessible name
	 * @return true if visible, false otherwise
	 */
	public boolean isVisibleByRole(AriaRole role, String name) {
		try {
			Locator locator = page().getByRole(role, new Page.GetByRoleOptions().setName(name));
			return locator.isVisible();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Click element by ARIA role
	 * 
	 * @param role - ARIA role
	 * @param name - Accessible name
	 */
	public void clickByRole(AriaRole role, String name) {
		try {
			Locator locator = page().getByRole(role, new Page.GetByRoleOptions().setName(name));
			waitForVisible(locator, ElementTimeout);
			locator.click();
		} catch (TimeoutError e) {
			throw new RuntimeException("Failed to click element with role " + role + " and name: " + name, e);
		} catch (Exception e) {
			throw new RuntimeException("Error clicking element by role", e);
		}
	}

	// =========== unique method to select tenant in case of multi tenant
	// application ===============
	/*
	 * This method will look for the tenant on the 'Choose Account' popup and based
	 * on provided tenant name it will click the relevant 'Choose' button
	 */
	public void selectTenant(String tenantPopup, String tenantName) {
		try {

			// Multi-tenant popup
			Locator chooseAccountPopup = page().locator(tenantPopup);

			// Locate the tenant name in span tag
			Locator tenantSpan = chooseAccountPopup.locator("span",
					new Locator.LocatorOptions().setHasText(tenantName));

			if (tenantSpan.count() == 0) {
				throw new RuntimeException("Tenant not found in popup: " + tenantName);
			}

			// 2️⃣ Go up to the divWrap container
			Locator tenantRow = tenantSpan.locator("xpath=ancestor::div[contains(@class,'divWrap')]");

			// 3️⃣ Click Choose inside that row
			Locator chooseBtn = tenantRow.locator("button", new Locator.LocatorOptions().setHasText("Choose"));

			clickOnElement(chooseBtn);

		} catch (Exception e) {
			throw new RuntimeException("Error in choosing tenant name: " + tenantName, e);
		}

	}
	
	/**
     * Handle tenant selection if multi-tenant popup appears
	 * @param tenantPopup 
     */
    public void handleTenantSelection(String tenantPopup) throws IOException {
    	String TENANT_NAME=PropertiesLoader.getTenantName();
    	if(isVisible(tenantPopup)) {
    		System.out.println("[INFO] Tenant popup visible");
    		selectTenant(tenantPopup,TENANT_NAME);
    	}    	
    }

	// =========== unique method to click on element using java script, needed if
	// other locator strategies are not stable ====================
	/**
	 * Execute JavaScript and wait for URL to change (for scripts that trigger
	 * navigation)
	 * 
	 * @param script - JavaScript code to execute
	 */
	public void executeScriptAndWaitForNavigation(String script) {
		try {
			// Get current URL to detect navigation
			String currentUrl = page().url();

			// Execute the script
			page().evaluate(script);

			// Wait for URL to change (indicates navigation happened)
			page().waitForURL(url -> !url.equals(currentUrl), new Page.WaitForURLOptions().setTimeout(PageTimeout));

		} catch (TimeoutError e) {
			throw new RuntimeException("Navigation did not complete after script execution within timeout", e);
		} catch (Exception e) {
			throw new RuntimeException("Failed to execute script and wait for navigation: " + script, e);
		}
	}

	// ============= UI wait strategy ==================
	/**
	 * Wait for element to be visible and return the locator Useful when you need to
	 * chain further actions on the locator
	 * 
	 * @param selector - CSS selector
	 * @return Locator after waiting for visibility
	 */
	public Locator waitAndGetLocator(String selector) {
		try {
			Locator locator = page().locator(selector);
			locator.waitFor(
					new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(ElementTimeout));
			return locator;
		} catch (TimeoutError e) {
			throw new RuntimeException("Element not visible within timeout: " + selector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for element: " + selector, e);
		}
	}

	/*
	 * Playwright waits for a client-side route change (SPA navigation) in an
	 * Angular app. It pauses your test until the page’s current URL matches the
	 * pattern you pass in. It works for both full navigations (page reloads) and
	 * SPA route changes (like Angular Router changes) that update window.location
	 * without reloading the page. By default it will wait up to 30 seconds
	 * (configurable via timeout)Global pattern matches with ** /console/systemConfig means: “match any URL that
	 * ends with /console/systemConfig regardless of the leading path/domain.”	 * 
	 * e.g., https://example.com/app/console/systemConfig ✅ e.g.,
	 * http://localhost:4200/console/systemConfig ✅
	 * 
	 */
	public void waitForURLRouting(String urlMatchPattern){
		try {
		    page().waitForURL(
		        urlMatchPattern,
		        new Page.WaitForURLOptions().setTimeout(PageTimeout) // 60 seconds
		    );
		}catch (TimeoutError e) {
			throw new RuntimeException("Function did not return matching URL with pattern: " + urlMatchPattern, e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for function to match pattern: " + urlMatchPattern, e);
		}
		
	}

	/**
	 * Wait for JavaScript function to return true
	 * 
	 * @param script - JavaScript expression that should return true
	 */
	public void waitForFunction(String script) {
		try {
			page().waitForFunction(script, null, new Page.WaitForFunctionOptions().setTimeout(ElementTimeout));
		} catch (TimeoutError e) {
			throw new RuntimeException("Function did not return true within timeout: " + script, e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for function: " + script, e);
		}
	}

	/**
	 * Wait for page to reach specified load state
	 * 
	 * @param loadState - The load state to wait for
	 */
	public void waitForLoadState(LoadState loadState) {
		try {
			page().waitForLoadState(loadState, new Page.WaitForLoadStateOptions().setTimeout(PageTimeout));
		} catch (TimeoutError e) {
			throw new RuntimeException("Page did not reach load state: " + loadState, e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for load state: " + loadState, e);
		}
	}

	/**
	 * Wait for element to be visible
	 * 
	 * @param locator - The Playwright locator
	 * @param timeout - Timeout in milliseconds
	 * @return The same locator after waiting
	 * @throws TimeoutError if element is not visible within timeout
	 */
	private Locator waitForVisible(Locator locator, int timeout) {
		try {
			locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(timeout));
			return locator;
		} catch (TimeoutError e) {
			throw new TimeoutError("Element not visible within " + timeout + "for the element:" + locator);
		}
	}

	// ============= UI Element visibility strategy ==================
	/**
	 * Check if element is visible within timeout Waits for element to appear before
	 * checking
	 * 
	 * @param selector - CSS selector
	 * @param timeout  - Time to wait in milliseconds
	 * @return true if visible within timeout, false otherwise
	 */
	public boolean isVisible(String selector) {
		try {
			page().locator(selector).waitFor(
					new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(ElementTimeout));
			return true;
		} catch (TimeoutError e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	

	// ============= Kendo strategies ==================

	/**
	 * Wait for Kendo loading mask to disappear
	 */
	public void waitForKendoLoadingComplete() {
		try {
			page().waitForSelector(".k-loading-mask",
					new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(ElementTimeout));
		} catch (TimeoutError e) {
			// Loading mask might not appear, continue
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for Kendo loading", e);
		}
	}

	/**
	 * Wait for all Kendo loaders to disappear Comprehensive wait - checks multiple
	 * loader types Use this for complex operations or page loads
	 */
	public void waitForAllKendoLoadersComplete() {
		String loaderSelector = ".k-loading-mask, .k-i-loading, .k-busy, .k-loading-image";
		try {
			// Wait for any loader to appear (with short timeout)
			try {
				page().locator(loaderSelector).first().waitFor(new Locator.WaitForOptions()
						.setState(WaitForSelectorState.ATTACHED).setTimeout(ElementTimeout));
			} catch (TimeoutError e) {
				// No loader appeared - that's fine
			}

			// Now wait for all loaders to be hidden
			page().waitForSelector(loaderSelector,
					new Page.WaitForSelectorOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(ElementTimeout));

		} catch (TimeoutError e) {
			// Loaders might not appear, continue
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for all Kendo loaders", e);
		}
	}

	/**
	 * Wait for Kendo grid to fully load Waits for loading mask + grid content to
	 * appear
	 * 
	 * @param gridSelector - Selector for the specific grid
	 */
	public void waitForKendoGridLoaded(String gridSelector) {
		try {
			// Wait for loading to complete
			waitForAllKendoLoadersComplete();

			// Wait for grid to be visible
			page().locator(gridSelector).waitFor(
					new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(ElementTimeout));

			// Wait for grid rows or no-records message
			page().waitForSelector(gridSelector + " tbody tr, " + gridSelector + " .k-grid-norecords",
					new Page.WaitForSelectorOptions().setTimeout(ElementTimeout));

		} catch (TimeoutError e) {
			throw new RuntimeException("Kendo grid did not load: " + gridSelector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for Kendo grid", e);
		}
	}

	/**
	 * Wait for Kendo dropdown/combobox to be ready
	 * 
	 * @param dropdownSelector - Selector for the dropdown
	 */
	public void waitForKendoDropdownReady(String dropdownSelector) {
		try {
			// Wait for loaders
			waitForAllKendoLoadersComplete();

			// Wait for dropdown to be visible and enabled
			Locator dropdown = page().locator(dropdownSelector);
			dropdown.waitFor(
					new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(ElementTimeout));

			// Wait for dropdown to be enabled (not disabled)
			page().waitForCondition(() -> !dropdown.isDisabled(),
					new Page.WaitForConditionOptions().setTimeout(ElementTimeout));

		} catch (TimeoutError e) {
			throw new RuntimeException("Kendo dropdown not ready: " + dropdownSelector, e);
		} catch (Exception e) {
			throw new RuntimeException("Error waiting for Kendo dropdown", e);
		}
	}

	/**
	 * Wait for Angular to stabilize Use after navigation or major state changes
	 */
	public void waitForAngularStable() {
		try {
			page().evaluate("() => new Promise(resolve => {" + "  if (window.getAllAngularTestabilities) {"
					+ "    const testabilities = window.getAllAngularTestabilities();"
					+ "    const count = testabilities.length;" + "    let doneCount = 0;"
					+ "    testabilities.forEach(t => {" + "      t.whenStable(() => {" + "        doneCount++;"
					+ "        if (doneCount === count) resolve();" + "      });" + "    });" + "  } else {"
					+ "    resolve();" + "  }" + "})");
		} catch (Exception e) {
			// Angular might not be available, continue
		}
	}

	/**
	 * Comprehensive wait for Kendo Angular page Use after page navigation or major
	 * operations Combines Angular stability + Kendo loaders
	 */
	public void waitForKendoAngularPageReady() {
		try {
			// Wait for Angular to stabilize
			waitForAngularStable();

			// Wait for all Kendo loaders
			waitForAllKendoLoadersComplete();

			// Small buffer for final rendering
			page().waitForTimeout(ElementTimeout);

		} catch (Exception e) {
			throw new RuntimeException("Error waiting for Kendo Angular page", e);
		}
	}
	
	 

}
