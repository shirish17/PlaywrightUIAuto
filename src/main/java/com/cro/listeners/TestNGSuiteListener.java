package com.cro.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class TestNGSuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("[SUITE-START] " + suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("[SUITE-END] " + suite.getName());
    }

}
