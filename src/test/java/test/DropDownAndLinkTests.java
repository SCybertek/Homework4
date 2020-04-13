package test;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BrowserFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DropDownAndLinkTests {
    private WebDriver driver;
    private By yearBy = By.id("year");
    private By monthBy = By.id("month");
    private By dayBy = By.id("day");

    @BeforeMethod
    public void setup(){
        driver = BrowserFactory.getDriver("chrome");
        //implicit wait
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }
    //    1.go to http://practice.cybertekschool.com/dropdown
    //    2.verify that dropdowns under Select your date of birth display current year,
    //     month,day
    @Test (description = "verify the current date")
    public void todaysDate(){

        driver.get("http://practice.cybertekschool.com/dropdown");

        WebElement year = driver.findElement(yearBy);
        WebElement month = driver.findElement(monthBy);
        WebElement day = driver.findElement(dayBy);

        Select y = new Select(year);
        Select m = new Select(month);
        Select d = new Select(day);

        String year_value = y.getFirstSelectedOption().getText();
        String month_value = m.getFirstSelectedOption().getText();
        String day_value = d.getFirstSelectedOption().getText();

        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMMMdd"));
        //SimpleDateFormat sf = new SimpleDateFormat("yyyyMMMMdd"); ==>  sf.format(new Date()
        String actual = year_value + month_value + day_value;
        Assert.assertEquals(expectedDate,actual);

    }

    //go tohttp://practice.cybertekschool.com/dropdown
    // 2.select a random year under Select your date of birth
    // 3.select month January
    // 4.verify that days dropdown has current number of days
    // 5.repeat steps 3, 4 for all the months

    /**
     * The java.util.GregorianCalendar.isLeapYear() method determines
     * if the given year passed as a parameter to the function is a leap year
     * or not and returns true if the given year is a leap year and false otherwise.
     * Syntax:
     * public boolean isLeapYear(int year)
     *
     */

    @Test (description = "verify that dropdown has current number of days")
    public void yearsMonthsDaysTest() {

        driver.get("http://practice.cybertekschool.com/dropdown");

        Select y = new Select(driver.findElement(yearBy));
        Select m = new Select(driver.findElement(monthBy));
        Select d = new Select(driver.findElement(dayBy));

        Random random = new Random();

        int yearToSelect = random.nextInt(y.getOptions().size()); // year is selected randomly
        //select a year
        y.selectByIndex(yearToSelect);

        for (int x = 0; x < 12; x++) {
            //we created date object based on your and month
            LocalDate localDate = LocalDate.of(yearToSelect, 1 + x, 1);

            //select a month
            m.selectByIndex(x);

            int actual = d.getOptions().size();//actual number of days
            int expected = Month.from(localDate).length(isLeapYear(yearToSelect)); //expected number of days in a month
            System.out.println("Month: " + m.getFirstSelectedOption().getText());
            System.out.println("Expected number of days: " + expected);
            System.out.println("Actual number of days: " + actual);
            System.out.println();
            Assert.assertEquals(actual, expected);
        }

    }


    public static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            }
        }
        return year % 4 == 0;
    }


    //verify that default value of the All departments dropdown is All
    // verify that options in the All departments dropdown are not sorted alphabetically
    @Test
    public void departmentSort(){

        driver.get("https://www.amazon.com/");

        String all = driver.findElement(By.className("nav-search-label")).getText();
        Assert.assertEquals(all,"All");

        WebElement dropDown = driver.findElement(By.id("searchDropdownBox"));
        Select select = new Select(dropDown);
        List <WebElement> allOptions = select.getOptions();

        boolean notAlphabetical = false;
        for (int i = 0; i < allOptions.size() - 1 ; i++) {
            if (allOptions.get(i).getText().compareTo(allOptions.get(i).getText()) > 0 ) {
                // if the result of the above is positive it means the list is not alphabetical
                notAlphabetical = true;
                break;
            }
        }
        Assert.assertTrue(notAlphabetical);

    }

    @Test

    //verify that every main department name
    // is present in the All departments dropdown
    public void mainDepartments() throws Exception {

        driver.get("https://www.amazon.com/gp/site-directory");
        driver.manage().window().maximize();
        Thread.sleep(3000);

        WebElement dropDown = driver.findElement(By.id("searchDropdownBox"));
        dropDown.click(); //make all dropdown visible

        Select select = new Select(dropDown);
        //                              new Select(driver.findElement(By.id("searchDropdownBox"))).getOptions()
        List<WebElement> allDropDownOptions = select.getOptions(); //store all options in List of WebElements
        List<WebElement> allDepartmentNames = driver.findElements(By.xpath("//h2"));

        //convert them both to String

        List<String> allElementsText=new ArrayList<>();
        for (int i = 0; i < allDropDownOptions.size(); i++) {
            allElementsText.add(allDropDownOptions.get(i).getText());
        }

        List<String> allDepartmentNamesText = new ArrayList<>();
        for (int i = 0; i < allDepartmentNames.size(); i++) {
            allDepartmentNamesText.add(allDepartmentNames.get(i).getText());
        }

        for (String departmentElement : allDepartmentNamesText){
            System.out.println("departmentElement : " + departmentElement);
            Assert.assertTrue(allElementsText.contains(departmentElement));
        }


    }
//go to https://www.w3schools.com/
//2.find all the elements in the page with the tag a
// 3.for each of those elements, if it is displayed on the page,
// print the text and the hrefof that element.
    @Test
    public void links(){
        driver.get("https://www.w3schools.com/");
        driver.manage().window().maximize();

        List<WebElement> allATags = driver.findElements(By.tagName("a"));
        for ( WebElement each : allATags) {
            if (each.isDisplayed()) {
                System.out.println(each.getText());
                System.out.println(each.getAttribute("href"));
            }
        }
    }

    //go to https://www.selenium.dev/documentation/en/
    // 2.find all the elements in the page with the tag a
    // 3.verify that all the links are valid
    @Test
    public void validLinks(){
        driver.get("https://www.selenium.dev/documentation/en/");
        driver.manage().window().maximize();
        List<WebElement> allATags = driver.findElements(By.tagName("a"));

        for (WebElement eachTag : allATags) {
            String abc = eachTag.getAttribute("href");
            System.out.println(abc);

            try {
                URL link = new URL(abc);
                HttpURLConnection http = (HttpURLConnection) link.openConnection();
                http.setConnectTimeout(3000);
                http.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

// MalformedURLException is thrown when the built-in URL class encounters an invalid URL;
// specifically, when the protocol that is provided is missing or invalid.

    @AfterMethod
    public void teardown(){
        driver.quit();
    }
}
