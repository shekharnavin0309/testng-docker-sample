package com.example.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.List;

public class LoginSteps {
    private WebDriver driver;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        // remove headless if you want a visible browser
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
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
        // either absent or not displayed or empty text
        boolean noneVisible = flashes.isEmpty() ||
            flashes.stream().allMatch(f -> !f.isDisplayed() || f.getText().trim().isEmpty());
        Assert.assertTrue(noneVisible, "Expected no flash messages, but found: " + flashes.size());
    }

    @Then("I click on the logout button")
    public void clickLogout() {
        // the logout button appears only on secure area
        driver.findElement(By.cssSelector("a.button.secondary.radius")).click();
    }

    @Then("I should be back on the login page with message containing {string}")
    public void assertLoggedOut(String expected) {
        // verify URL is /login and flash contains expected text
        Assert.assertTrue(driver.getCurrentUrl().endsWith("/login"),
            "Expected to be on /login, but was: " + driver.getCurrentUrl());
        WebElement flash = driver.findElement(By.id("flash"));
        Assert.assertTrue(flash.getText().contains(expected),
            "Expected logout message to contain [" + expected + "], but was: " + flash.getText());
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
