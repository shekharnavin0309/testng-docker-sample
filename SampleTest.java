package com.example;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest {

    @Test
    public void testAddition() {
        int a = 2, b = 3;
        Assert.assertEquals(a + b, 5, "2 + 3 should equal 5");
    }

    @Test
    public void testString() {
        String hello = "Hello, " + "TestNG!";
        Assert.assertTrue(hello.contains("TestNG"), "String should contain 'TestNG'");
    }
}
