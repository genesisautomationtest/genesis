package tech.gen.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tech.gen.pages.CabinetPage;
import tech.gen.pages.SignUpPage;
import tech.gen.utils.ConfigProperty;
import tech.gen.utils.GmailUtil;
import tech.gen.utils.HelperUtil;


public class RegistrationTest {

    protected WebDriver driver;

    @BeforeClass
    public void beforeRegistration() {
        System.setProperty("webdriver.chrome.driver", ConfigProperty.getInstance().getProperty("chrome.driver.path"));
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(ConfigProperty.getInstance().getProperty("site.url"));
    }

    @AfterClass
    public void afterRegistration() {
        driver.quit();
    }

    @Test
    public void validRegistration() {
        String name = "Rozetka";
        String email = HelperUtil.generateEmail();
        String password = "123Qwerty";

        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.setName(name);
        signUpPage.setEmail(email);
        signUpPage.setPassword(password);
        signUpPage.clickButtonSubmit();

        String link = HelperUtil.getLink(ConfigProperty.getInstance().getProperty("regex.link.authorize"),
                GmailUtil.getInstance().getMessageBody(email));

        CabinetPage cabinetPage = new CabinetPage(driver);
        cabinetPage.open(link);
        Assert.assertEquals(cabinetPage.geEmail(), email.toLowerCase(), "email");
    }
}
