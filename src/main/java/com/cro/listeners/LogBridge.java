
package com.cro.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

public final class LogBridge {

    private static final Logger LOG = LogManager.getLogger(LogBridge.class);

    private static boolean inScenario() {
        return ScenarioContext.getScenarioName() != null;
    }

    private static void extent(String msg) {
        if (!inScenario()) return;   // MUST NOT call Extent outside scenario
        try {
            ExtentCucumberAdapter.addTestStepLog(msg);
        } catch (Throwable t) {
            // Ignore: Extent not active or shutting down
        }
    }

    public static void info(String msg) {
        LOG.info(msg);
        extent(msg);
    }

    public static void warn(String msg) {
        LOG.warn(msg);
        extent("[WARN] " + msg);
    }

    public static void error(String msg) {
        LOG.error(msg);
        extent("[ERROR] " + msg);
    }

    public static void step(String msg) {
        extent(msg);
    }
}
