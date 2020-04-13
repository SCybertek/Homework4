package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BrowserFactory {

    public static WebDriver getDriver(String browser) {
        browser = browser.toLowerCase();
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup(); //version removed
                return new ChromeDriver();
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();

        }
        return null;
    }
}
