// src/test/java/com/example/runners/CucumberTestRunner.java
package com.example.runners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
    features = "src/test/resources/features",
    glue     = "com.example.stepdefs",
    plugin   = { "pretty", "html:target/cucumber-report.html" }
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests { }
