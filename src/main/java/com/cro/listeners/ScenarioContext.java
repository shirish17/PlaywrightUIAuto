package com.cro.listeners;

import org.apache.logging.log4j.ThreadContext;

public final class ScenarioContext {

	private static final ThreadLocal<String> scenarioNameTL = new ThreadLocal<>();
    private static final ThreadLocal<Long> stepStart = new ThreadLocal<>();

    // --- Scenario name handling ---
    public static void init(String scenarioName) {
        scenarioNameTL.set(scenarioName);
        ThreadContext.put("scenario", scenarioName);
    }

    public static String getScenarioName() {
        return scenarioNameTL.get();
    }

    public static void clear() {
        scenarioNameTL.remove();
        ThreadContext.remove("scenario");
        clearStepTiming();
    }

    // --- Step timing handling ---
    public static void markStepStart() {
        stepStart.set(System.currentTimeMillis());
    }

    public static long stepDuration() {
        Long start = stepStart.get();
        return (start == null) ? -1L : System.currentTimeMillis() - start;
    }

    public static void clearStepTiming() {
        stepStart.remove();
    }
    
}