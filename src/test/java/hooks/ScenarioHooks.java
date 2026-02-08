package hooks;

import java.io.IOException;
import java.nio.file.Path;

import com.cro.extentreporting.ExtentReportMetada;
import com.cro.listeners.ScenarioContext;
import com.cro.playwright.BrowserInfo;
import com.cro.playwright.BrowserManager;
import com.cro.playwright.LoginFlow;
import com.cro.playwright.RoleResolver;
import com.cro.playwright.SessionManager;
import com.cro.settings.PathManager;
import com.cro.settings.PropertiesLoader;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class ScenarioHooks {
    
    private final LoginFlow loginFlow;
    
    public ScenarioHooks(LoginFlow loginFlow) {
        this.loginFlow = loginFlow;
    }
    
    @Before(order = 0)
    public void before(Scenario scenario) throws IOException {
        
        String browser = PropertiesLoader.effectiveBrowserCached();
        String role = RoleResolver.resolve(scenario);
        String username = PropertiesLoader.getUsernameForRole(role);
        String password = PropertiesLoader.getPasswordForRole(role);
        int pageTimeout = PropertiesLoader.getPageTimeout();
        
        ExtentReportMetada.put("User [Role: " + role + "]", username);
        
        // 🚀 Ensure browser for THIS scenario thread
        BrowserManager.initBrowser(browser);
        BrowserInfo.captureOnce(BrowserManager.getBrowserVersion());
        
        // =========================
        // SESSION CREATION (THREAD SAFE)
        // =========================
        Path sessionPath = SessionManager.getOrCreateSession(role, username, () -> {
            
            // Create context for login
            BrowserManager.createContext();
            
            try {
                BrowserManager.getPage().navigate(
                    PropertiesLoader.loadCached().getProperty("base.url"),
                    new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                        .setTimeout(pageTimeout)
                );
                
                // Perform login
                loginFlow.performLogin(role, username, password);
                
                // Save session
                BrowserManager.getContext().storageState(
                    new BrowserContext.StorageStateOptions()
                        .setPath(PathManager.sessionDir().resolve(role + "_" + username + ".json"))
                );
                
                // ⭐ CRITICAL: Close context after saving session
                BrowserManager.closeContext();
                
            } catch (Exception e) {
                throw new RuntimeException("Session creation failed", e);
            }
        });
        
        // =========================
        // FRESH CONTEXT FOR SCENARIO WITH SESSION
        // =========================
        BrowserManager.createContext(sessionPath);
        
        // ⭐⭐⭐ CRITICAL FIX: Navigate to app to ACTIVATE the session ⭐⭐⭐
        try {
            String baseUrl = PropertiesLoader.loadCached().getProperty("base.url");
            
            BrowserManager.getPage().navigate(
                baseUrl,
                new Page.NavigateOptions()
                    .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                    .setTimeout(pageTimeout)
            );
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to navigate with loaded session", e);
        }
        
        System.out.println(
            "[HOOK] Thread=" + Thread.currentThread().getName() +
            " Browser=" + BrowserManager.getBrowserVersion()
        );
    }
    
    @BeforeStep
    public void beforeStep() {
        ScenarioContext.markStepStart();
    }
    
    @AfterStep
    public void afterStep(Scenario scenario) {
        // future: step-level logging / screenshots
    }
    
    @After
    public void after(Scenario scenario) {
        BrowserManager.closeContext(); // ✔ per-scenario only
    }
}