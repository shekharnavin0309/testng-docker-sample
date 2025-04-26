package com.example.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginSteps {
    private WebDriver driver;
    private Path chromeProfileDir;

    @Before
    public void setUp() throws IOException {
        WebDriverManager.chromedriver().setup();

        // create unique profile directory to avoid conflicts
        chromeProfileDir = Files.createTempDirectory("chrome-profile-");
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        Map<String, String> logs = new HashMap<>();
        logs.put("driver", "ALL");
        logs.put("browser","ALL");

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
            "--headless=new",
            "--user-data-dir=" + chromeProfileDir.toAbsolutePath(),
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--remote-allow-origins=*"
        );

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Given("I open the login page")
    public void openLoginPage() {
        driver.get("https://the-internet.herokuapp.com/login");
    }

    @When("I enter username {string} and password {string}")
    public void enterCredentials(String user, String pass) {
        WebElement u = driver.findElement(By.id("username"));
        WebElement p = driver.findElement(By.id("password"));
        u.clear();
        p.clear();
        u.sendKeys(user);
        p.sendKeys(pass);
    }

    @When("I click on the login button")
    public void clickLogin() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Then("I should see a success message containing {string}")
    public void assertSuccessMessage(String expected) {
        WebElement flash = driver.findElement(By.id("flash"));
        String text = flash.getText();
        Assert.assertTrue(text.contains(expected),
            "Expected success to contain [" + expected + "], but was: " + text);
    }

    @Then("I should see an error message containing {string}")
    public void assertErrorMessage(String expected) {
        WebElement flash = driver.findElement(By.id("flash"));
        String text = flash.getText();
        Assert.assertTrue(text.contains(expected),
            "Expected error to contain [" + expected + "], but was: " + text);
    }

    @When("I refresh the page")
    public void refreshPage() {
        driver.navigate().refresh();
    }

    @Then("I should not see any error or success message")
    public void assertNoFlash() {
        List<WebElement> flashes = driver.findElements(By.id("flash"));
        boolean noneVisible = flashes.isEmpty() ||
            flashes.stream().allMatch(f -> !f.isDisplayed() || f.getText().trim().isEmpty());
        Assert.assertTrue(noneVisible, "Expected no flash messages, but found: " + flashes.size());
    }

    @Then("I click on the logout button")
    public void clickLogout() {
        driver.findElement(By.cssSelector("a.button.secondary.radius")).click();
    }

    @Then("I should be back on the login page with message containing {string}")
    public void assertLoggedOut(String expected) {
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/login"),
            "Expected to be on /login, but was: " + driver.getCurrentUrl());
        WebElement flash = driver.findElement(By.id("flash"));
        Assert.assertTrue(flash.getText().contains(expected),
            "Expected logout message to contain [" + expected + "], but was: " + flash.getText());
    }

    @After
    public void tearDown() throws IOException {
        if (driver != null) {
            driver.quit();
        }
        if (chromeProfileDir != null) {
            Files.walk(chromeProfileDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}
