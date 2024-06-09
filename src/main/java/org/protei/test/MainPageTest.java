package org.protei.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Тесты главной страницы
 */
public class MainPageTest {
    private WebDriver driver;
    private WebElement emailInput;
    private WebElement nameInput;
    private WebElement genderInput;
    private WebElement checkboxInput1, checkboxInput2;
    private WebElement radioInput2, radioInput3;
    private WebElement addButton;

    @BeforeClass(description = "Открытие браузера, инициализация элементов")
    public void openPage(){
        String browser = System.getProperty("browser");
        switch (browser) {
            case "firefox" -> driver = new FirefoxDriver();
            case "chrome" -> driver = new ChromeDriver();
            case "edge" -> driver = new EdgeDriver();
        }
        driver.get(System.getProperty("url"));
        WebElement emailLoginInput = driver.findElement(By.id("loginEmail"));
        WebElement passwordLoginInput = driver.findElement(By.id("loginPassword"));
        WebElement authButton = driver.findElement(By.id("authButton"));
        emailLoginInput.sendKeys("test@protei.ru");
        passwordLoginInput.sendKeys("test");
        authButton.click();
        emailInput = driver.findElement(By.id("dataEmail"));
        nameInput = driver.findElement(By.id("dataName"));
        genderInput = driver.findElement(By.id("dataGender"));
        checkboxInput1 = driver.findElement(By.id("dataCheck11"));
        checkboxInput2 = driver.findElement(By.id("dataCheck12"));
        radioInput2 = driver.findElement(By.id("dataSelect22"));
        radioInput3 = driver.findElement(By.id("dataSelect23"));
        addButton = driver.findElement(By.id("dataSend"));
    }

    @AfterMethod(description = "очистка полей ввода после каждого теста")
    public void clearFields(){
        emailInput.clear();
        nameInput.clear();
    }

    @Test(description = "Проверка попытки авторизации с пустыми полями")
    public void emptyFields() {
        addButton.click();
        WebElement error = driver.findElement(By.id("emailFormatError"));
        assertTrue(error.isDisplayed(), "Элемент не отображается");
        assertEquals(error.getText(), "Неверный формат E-Mail", "Неверный текст ошибки");
    }

    @Test(description = "Проверка попытки авторизации с пустым полем почты",
            dependsOnMethods = "emptyFields")
    public void emptyEmail() {
        nameInput.sendKeys("Nikita");
        addButton.click();
        WebElement error = driver.findElement(By.id("emailFormatError"));
        assertTrue(error.isDisplayed(), "Элемент не отображается");
        assertEquals(error.getText(), "Неверный формат E-Mail", "Неверный текст ошибки");
    }

    @Test(description = "Проверка попытки авторизации с пустым полем имя",
            dependsOnMethods = "emptyEmail")
    public void emptyName() {
        emailInput.sendKeys("Nikita@protei.ru");
        addButton.click();
        WebElement error = driver.findElement(By.id("blankNameError"));
        assertTrue(error.isDisplayed(), "Элемент не отображается");
        assertEquals(error.getText(), "Поле имя не может быть пустым", "Неверный текст ошибки");
    }

    @Test(description = "Проверка авторизации с валидными полями 1",
            dependsOnMethods = "emptyName")
    public void validData1() {
        emailInput.sendKeys("Bob@protei.ru");
        nameInput.sendKeys("Bob");
        addButton.click();
        driver.findElement(By.className("uk-modal-close")).click();
        WebElement table = driver.findElement(By.id("dataTable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        String[] tableValues = getTableValues(rows);
        assertTrue(tableValues[1].contains("Bob@protei.ru"), "Не обнаружен почтовый адрес");
        assertTrue(tableValues[1].contains("Мужской"), "Не обнаружено значение поля 'Пол'");
        assertTrue(tableValues[1].contains("Нет"), "Не обнаружено значение поля 'checkbox'");
    }

    @Test(description = "Проверка авторизации с валидными полями 2",
            dependsOnMethods = "validData1")
    public void validData2() {
        emailInput.sendKeys("Alice@protei.ru");
        nameInput.sendKeys("Alice");
        checkboxInput1.click();
        radioInput2.click();
        Select select = new Select(genderInput);
        select.selectByVisibleText("Женский");
        addButton.click();
        driver.findElement(By.className("uk-modal-close")).click();
        WebElement table = driver.findElement(By.id("dataTable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        String[] tableValues = getTableValues(rows);
        assertTrue(tableValues[2].contains("Alice@protei.ru"), "Не обнаружен почтовый адрес");
        assertTrue(tableValues[2].contains("Женский"), "Не обнаружено значение поля 'Пол'");
        assertTrue(tableValues[2].contains("1.1"), "Не обнаружено значение поля 'checkbox'");
        assertTrue(tableValues[2].contains("2.2"), "Не обнаружено значение поля 'radiobutton'");
    }

    @Test(description = "Проверка авторизации с валидными полями 3",
            dependsOnMethods = "validData2")
    public void validData3() {
        emailInput.sendKeys("Eve@protei.ru");
        nameInput.sendKeys("Eve");
        checkboxInput2.click();
        radioInput3.click();
        addButton.click();
        driver.findElement(By.className("uk-modal-close")).click();
        WebElement table = driver.findElement(By.id("dataTable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        String[] tableValues = getTableValues(rows);
        assertTrue(tableValues[3].contains("Eve@protei.ru"), "Не обнаружен почтовый адрес");
        assertTrue(tableValues[3].contains("Женский"), "Не обнаружено значение поля 'Пол'");
        assertTrue(tableValues[3].contains("1.1, 1.2"), "Не обнаружено значение поля 'checkbox'");
        assertTrue(tableValues[3].contains("2.3"), "Не обнаружено значение поля 'radiobutton'");
    }

    @AfterClass(description = "закрыть браузер после завершения тестов")
    public void closeBrowser(){
        driver.quit();
    }

    /**
     * Метод для парсинга таблицы
     *
     * @return Список строк, каждая из которых содержит данные одного человека
     */
    private String[] getTableValues(List<WebElement> rows){
        StringBuilder tableString = new StringBuilder();
        for (WebElement row : rows) {
            List<WebElement> columns = row.findElements(By.tagName("td"));
            for (WebElement column : columns) {
                tableString.append(column.getText() + "\t");
            }
            tableString.append("\n");
        }
        return tableString.toString().split("\n");
    }
}
