package tech.gen.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignUpPage {

    private final WebDriver driver;

    public SignUpPage(WebDriver driver) {
        this.driver = driver;
    }

    public SignUpPage setName(String name){
        driver.findElement(By.xpath("//input[@name='title']")).sendKeys(name);
        return this;
    }

    public SignUpPage setEmail(String email){
        driver.findElement(By.xpath("//input[@name='login']")).sendKeys(email);
        return this;
    }

    public SignUpPage setPassword(String password){
        driver.findElement(By.xpath("//input[@name='password']")).sendKeys(password);
        return this;
    }

    public void clickButtonSubmit(){
        driver.findElement(By.xpath("//div[@class='signup-submit']//button")).click();
    }
}
