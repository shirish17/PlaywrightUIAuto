package runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",  // Correct path
    glue = {"steps"},                          // Your step package
    plugin = {"pretty"}
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {}

