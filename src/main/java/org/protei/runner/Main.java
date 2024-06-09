package org.protei.runner;

import org.testng.TestNG;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String testSuiteDirectory = System.getProperty("user.dir") + "/testngXml/";
        TestNG testng = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add(testSuiteDirectory + "LoginTest.xml");
        suites.add(testSuiteDirectory + "MainPageTest.xml");
        testng.setTestSuites(suites);
        testng.run();
    }
}