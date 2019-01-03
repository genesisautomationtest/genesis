package tech.gen.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CabinetPage {

    private final WebDriver driver;

    public CabinetPage(WebDriver driver) {
        this.driver = driver;
    }

    public CabinetPage open(String url) {
        driver.manage().deleteAllCookies();
        driver.get(url);
        return new CabinetPage(driver);
    }

    public String geEmail() {
        return driver.findElement(By.xpath("//div[contains(text(),'почтa')]/..//div[contains(@class,'info')]")).getText();
    }
}
