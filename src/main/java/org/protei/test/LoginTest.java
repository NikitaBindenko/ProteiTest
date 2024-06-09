package org.protei.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Тесты страницы авторизции
 */
public class LoginTest {
    private WebDriver driver;
    private WebElement emailInput;
    private WebElement passwordInput;
    private WebElement authButton;

    @BeforeMethod(description = "открытие страницы перед каждым тестом, поиск используемых элементов")
    public void openBrowser(){
        String browser = System.getProperty("browser");
        switch (browser) {
            case "firefox" -> driver = new FirefoxDriver();
            case "chrome" -> driver = new ChromeDriver();
            case "edge" -> driver = new EdgeDriver();
        }
        driver.get(System.getProperty("url"));
        emailInput = driver.findElement(By.id("loginEmail"));
        passwordInput = driver.findElement(By.id("loginPassword"));
        authButton = driver.findElement(By.id("authButton"));
    }

    @AfterMethod(description = "закрытие браузера после каждого теста")
    public void closeBrowser(){
        driver.quit();
    }

    @Test(description = "Проверка ввода неверного логина")
    public void invalidLogin() {
        emailInput.sendKeys("test@protei");
        passwordInput.sendKeys("test");
        authButton.click();
        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        assertTrue(error.isDisplayed(), "Элемент не отображается");
        assertEquals(error.getText(), "Неверный E-Mail или пароль", "Неверный текст ошибки");
    }

    @Test(description = "Проверка ввода неверного пароля")
    public void invalidPassword() {
        emailInput.sendKeys("test@protei.ru");
        passwordInput.sendKeys("testttt");
        authButton.click();
        WebElement error = driver.findElement(By.id("invalidEmailPassword"));
        assertTrue(error.isDisplayed(),"Элемент не отображается");
        assertEquals(error.getText(), "Неверный E-Mail или пароль", "Неверный текст ошибки");
    }

    @Test(description = "Проверка ввода email некорректного формата")
    public void incorrectEmailFormat() {
        emailInput.sendKeys("test");
        passwordInput.sendKeys("test");
        authButton.click();
        WebElement error = driver.findElement(By.id("emailFormatError"));
        assertTrue(error.isDisplayed(), "Элемент не отображается");
        assertEquals(error.getText(), "Неверный формат E-Mail", "Неверный текст ошибки");
    }

    @Test(description = "Проверка попытки авторизации с пустыми полями")
    public void emptyFields() {
        authButton.click();
        WebElement error = driver.findElement(By.id("emailFormatError"));
        assertTrue(error.isDisplayed(), "Элемент не отображается");
        assertEquals(error.getText(), "Неверный формат E-Mail", "Неверный текст ошибки");
    }

    @Test(description = "Проверка корректной авторизации, определяется по наличию id")
    public void correctSignIn() {
        emailInput.sendKeys("test@protei.ru");
        passwordInput.sendKeys("test");
        authButton.click();
        assertTrue(driver.findElement(By.id("dataName")).isDisplayed(), "Не удалось выполнить авторизацию");
    }
}