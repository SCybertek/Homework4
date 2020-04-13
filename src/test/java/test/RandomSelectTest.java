package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utilities.BrowserFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class RandomSelectTest {
    private WebDriver driver;

    // DAYS
    //go to http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox2.Randomlyselect a checkbox.
    // As soon as you check any day, print the name of the day and uncheck immediately.
    // After you check and uncheck Friday for the third time, exit the program

    @Test
    public void checkBoxTest(){
        driver = BrowserFactory.getDriver("chrome");
        driver.get("http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwCheckBox");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //days of the week
        List<WebElement> days = driver.findElements(By.cssSelector(".gwt-CheckBox>label"));
        //all checkboxes
        List<WebElement> allCheckboxes = driver.findElements(By.xpath("//*[@type='checkbox']"));

        //css : .gwt-CheckBox>input

        //check Random
        Random myRd = new Random();
        int count = 0;
        while (count < 3 ) {

            // gives you the number form 0 to bound (exclusive)
        int index = myRd.nextInt(days.size()); //random class is boundary exclusive


        if (allCheckboxes.get(index).isEnabled()) {
            days.get(index).click();
            if (days.get(index).getText().equals("Friday")){
                count++;
            }
            System.out.println(days.get(index).getText());
            days.get(index).click();
        }
        }

    }


    @AfterMethod
    public void teardown(){
        driver.quit();
    }
}
