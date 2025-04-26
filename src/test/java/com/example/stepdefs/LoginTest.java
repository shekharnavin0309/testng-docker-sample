package com.example.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class LoginTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void validLogin() {
        driver.get("https://the-internet.herokuapp.com/login");
        driver.findElement(By.id("username"))
              .sendKeys("tomsmith");
        driver.findElement(By.id("password"))
              .sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']"))
              .click();

        WebElement flash = driver.findElement(By.id("flash"));
        assertTrue(flash.getText().contains("You logged into a secure area!"),
                   "Login failed or message changed");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
