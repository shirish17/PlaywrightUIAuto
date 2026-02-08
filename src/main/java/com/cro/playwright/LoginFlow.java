/*
 * IMP: LoginFlow → Business logic + session handling

This is where your role + session JSON logic belongs.
 */
package com.cro.playwright;

import java.io.IOException;

import com.cro.pages.LoginPage;

public class LoginFlow {

    private final LoginPage loginPage;

    public LoginFlow(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    public void performLogin(String role, String user, String password) throws IOException {
    	System.out.println(
    	        "[FLOW] LoginFlow invoked | role=" + role +
    	        " user=" + user +
    	        " thread=" + Thread.currentThread().getName()
    	    );
    	// Step 1: Perform login
        loginPage.login(user, password);
        
     // Step 2: Validate email matches logged-in user
        boolean isValidEmail = loginPage.validateUserEmail(user);
        if (!isValidEmail) {
            throw new RuntimeException(
                "Email validation failed! Expected: " + user + 
                " but got different email in user profile."
            );
        }
        System.out.println("[FLOW] Login and email validation successful for: " + user);
    }
}

