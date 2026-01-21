
package com.cro.listeners;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberEventListener implements EventListener {

    private static final Logger LOG = LogManager.getLogger(CucumberEventListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {

        publisher.registerHandlerFor(TestCaseStarted.class, event -> {
            String name = event.getTestCase().getName()
                               .replaceAll("[^a-zA-Z0-9-_]", "_");

            ScenarioContext.init(name);
            Thread.currentThread().setName("SC-" + name + "-" + Thread.currentThread().getId());
            LogBridge.info("=== START SCENARIO: " + name + " ===");
        });

        publisher.registerHandlerFor(TestStepFinished.class, event -> {
            if (event.getResult().getError() != null) {
                LogBridge.error("Step failed: " + event.getResult().getError().toString());
            }
        });

        publisher.registerHandlerFor(TestCaseFinished.class, event -> {
            String name = ScenarioContext.getScenarioName();
            LogBridge.info("=== END SCENARIO: " + name + " ===");

            // IMPORTANT: clear only AFTER all Extent logs are done
            ScenarioContext.clear();
        });

        // 🔥 EXTREMELY IMPORTANT:
        // Never call LogBridge (Extent) here!
        // Extent is already shut down at this event.
        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            LOG.info("[RUN-FINISHED] status=" + event.getResult().getStatus());
        });
    }
}
