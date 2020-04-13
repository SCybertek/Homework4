package test;

import com.google.gson.internal.$Gson$Preconditions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utilities.BrowserFactory;
import utilities.BrowserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AmazonTests {

    private WebDriver driver;



    @BeforeMethod
    public void setup(){
        driver = BrowserFactory.getDriver("chrome");
        driver.get("https://www.amazon.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("wooden spoon", Keys.ENTER);
        //BrowserUtils.wait(3);
    }


    //go to https://amazon.com
    // 2.search for "wooden spoon"
    // 3.click search
    // 4.remember the name and the price of a random result
    // 5.click on that random result
    // 6.verify default quantityof items is 1
    // 7.verify that the name and the price is the same as the one from step 5
    // 8.verify button"Add to Cart" is visible
    @Test
    public void cart(){

        List<WebElement> items = driver.findElements(By.cssSelector("[class='sg-col-inner']"));

        items.removeIf(p -> p.findElements(By.cssSelector("[aria-label='Amazon Prime']")).isEmpty()); // remove all non-prime items

        List<WebElement> prices = items.stream().map(p -> p.findElement(By.cssSelector("[class='a-price'] > [aria-hidden='true']"))).collect(Collectors.toList());
        List<WebElement> descriptions = items.stream().map(p -> p.findElement(By.cssSelector("[class='a-size-base-plus a-color-base a-text-normal']"))).collect(Collectors.toList());


        System.out.println("Number of prices: " + prices.size());
        System.out.println("Number of descriptions: " + descriptions.size());

        Random random = new Random();
        int randomNumber = random.nextInt(descriptions.size());

        prices.removeIf(p -> !p.isDisplayed()); //remove invisible items
        descriptions.removeIf(p -> !p.isDisplayed()); //remove invisible items

        //replace new line with .
        List<String> parsedPrices = BrowserUtils.getTextFromWebElements(prices).parallelStream().map(p -> p.replace("\n", ".")).collect(Collectors.toList());
        List<String> parsedDescriptions = BrowserUtils.getTextFromWebElements(prices);

        parsedDescriptions.removeIf(String::isEmpty);

        String expectedPrice = parsedPrices.get(randomNumber);

        WebElement randomItem = descriptions.get(randomNumber);

        String expectedDescription = randomItem.getText().trim();


//        System.out.println("Prices: " + parsedPrices);
//        System.out.println("Descriptions: " + BrowserUtils.getTextFromWebElements(descriptions));


        randomItem.click();//click on random item

        WebElement quantity = driver.findElement(By.xpath("//span[text()='Qty:']/following-sibling::span"));

        int actual = Integer.parseInt(quantity.getText().trim());

        Assert.assertEquals(actual, 1);

        WebElement productTitle = driver.findElement(By.id("productTitle"));
        WebElement productPrice = driver.findElement(By.cssSelector("[id='priceInsideBuyBox_feature_div'] > div"));

        String actualDescription = productTitle.getText().trim();
        String actualPrice = productPrice.getText().trim();

        Assert.assertEquals(actualDescription, expectedDescription);
        Assert.assertEquals(actualPrice, expectedPrice);


    }

    //go to https://amazon.com
    // 2.search for "wooden spoon"
    // 3.click search
    // 4.remember name first result that has prime label
    // 5.select Prime check box on the left
    // 6.verify that name first result that has prime label is same as step 4
    // 7.check the last checkbox under Brand on the left
    // 8.verify that name first result that has prime label is different

    @Test
    public void prime(){

        String primeProduct = driver.findElement(By.xpath("//*[text()='OXO 1130880 Good Grips Wooden Corner Spoon & Scraper,Brown']")).getText();
        driver.findElement(By.xpath("(//*[@class='a-icon a-icon-checkbox'])[1]")).click();//clicking on prime
        List<WebElement> allPrime = driver.findElements(By.xpath("//h2"));
        String firstPrime = allPrime.get(0).getText();
        //verify that name first result that has prime label is same as step 4
        Assert.assertEquals(primeProduct,firstPrime);
        //test does not pass here because above assertion fails

        List<WebElement> primeBrands=driver.findElements(By.xpath("//div[@id='p_89-title']//following-sibling::ul//li"));
        primeBrands.get(primeBrands.size()-1).click();
        BrowserUtils.wait(2);

        List<WebElement> allNewBrands = driver.findElements(By.tagName("h2"));
        String firstNewBrand = allNewBrands.get(0).getText();

        boolean abc = firstNewBrand.equals(firstPrime); //false

        Assert.assertTrue(abc);
        //if we ignore first assertion this assertion passes
    }

    //1.go to https://amazon.com
    // 2.search for "wooden spoon"
    // 3.remember all Brand nameson the left
    // 4.select Prime checkboxon the left
    // 5.verify that same Brand names are still displayed

    @Test
    public void moreSpoons(){

        List<WebElement> allBrands=driver.findElements(By.xpath("//div[@id='p_89-title']//following-sibling::ul//li"));

        //converting to Strings
        List<String> brandsAsStrings = new ArrayList<>();
        for (int i = 0; i < allBrands.size(); i++) {
            brandsAsStrings.add(allBrands.get(i).getText());
        }

        driver.findElement(By.xpath("(//*[@class='a-icon a-icon-checkbox'])[1]")).click();//clicking on prime

        List<WebElement> primeBrands = driver.findElements(By.xpath("//div[@id='p_89-title']//following-sibling::ul//li"));

        for (int i = 0; i < primeBrands.size()-1; i++) {
            String primeBrand = primeBrands.get(i).getText();
            System.out.println("primeBrand = " + primeBrand);
            Assert.assertTrue(brandsAsStrings.contains(primeBrand));
        }

    }

    //1.go to https://amazon.com
    // 2.search for "wooden spoon"
    // 3.click on Price option Under $25 on the left
    // 4.verify that all results are cheaper than $25

    @Test
    public void cheapSpoons(){
        //driver.findElement(By.xpath("//*[text()='Under $25']")).click(); => did not work this one
        //it is actually link text so :
        driver.findElement(By.linkText("Under $25")).click(); //sometimes test case fails just because page does not display under $25

        //locator for price
        //css = [class='a-size-base a-link-normal s-no-hover a-text-normal'] > span[class='a-price']
        //List<WebElement> prices = driver.findElements(By.cssSelector("span[class='a-price'] > [class= 'a-offscreen']"));

        //we can get only dollars , excluding cents
        //new locator for price will be :
        //className("a-price-whole")

        //we collect only dollar values from the price of every item
        List<WebElement> prices = driver.findElements(By.className("a-price-whole"));
        //we convert collection of web elements into collection of strings
        List<String> pricesText = BrowserUtils.getTextFromWebElements(prices);
        System.out.println(pricesText);
        for (String price : pricesText) {
            //we convert every price as a string into integer
            int priceConverted = Integer.parseInt(price);
            //checking if the price of every item is under 25
            Assert.assertTrue(priceConverted < 25);
        }

    }


    @AfterMethod
    public void teardown(){
        driver.quit();
    }
}
