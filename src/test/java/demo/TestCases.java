package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

import java.util.List;
import java.util.logging.Level;

// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

    @Test(enabled = true)
    public void testCase01() throws InterruptedException {
        System.out.println("Start Test Case: TestCase01");
        boolean status;
        // Open the flipkart website
        Wrappers wrapper = new Wrappers(driver);
        wrapper.navigateToHome();
        System.out.println("Step Success : Successfully navigated to Flipkart Website");

        // Search for product "Washing Machine".
        status = wrapper.searchForProduct("Washing Machine");
        if (!status) {
            System.out.println("TestCase 1 :Test Case Failure. Unable to search for given product");
        } else
            System.out.println("Step Success : Successfully searched for the product");

        Thread.sleep(4000);
        // Verify Search results
        List<WebElement> searchResults = wrapper.getSearchResults();
        if (searchResults.size() == 0) {
            System.out.println("TestCase 1 :Test Case Failure. There were no results for the given search string");
        } else
            for (WebElement webElement : searchResults) {
                // Verify that all results contain the searched text
                String elementText = wrapper.getTitleofResult(webElement);
                if (!elementText.contains("Washing Machine")) {
                    System.out.println(
                            "TestCase 1 :Test Case Failure. Test Results contains un-expected values: " + elementText);
                }
                break;
            }
        System.out.println("Step Success : Successfully validated the search results");

        // Sort by popularity
        status = wrapper.sortBy("Popularity");
        if (!status) {
            System.out.println("TestCase 1: Test case Failure . Test Results are not sorted by Popularity");
        }
        System.out.println("Step Success : Successfully sorted the search results");

        // print the count of items with rating less than or equal to 4 stars
        int count = wrapper.countResults();
        System.out.println("Count of items with rating less than or equal to 4 stars: " + count);

        System.out.println("End Test Case: TestCase01");

    }

    @Test(enabled = true)
    public void testCase02() throws InterruptedException {
        System.out.println("Start Test Case: TestCase02");
        Wrappers wrapper = new Wrappers(driver);
        boolean status;
        // Search for product "iPhone".
        status = wrapper.searchForProduct("iPhone");
        if (!status) {
            System.out.println("TestCase 2 :Test Case Failure. Unable to search for given product");
        } else
            System.out.println("Step Success : Successfully searched for the product");

        Thread.sleep(4000);
        // Verify Search results
        List<WebElement> searchResults = wrapper.getSearchResults();
        if (searchResults.size() == 0) {
            System.out.println("TestCase 2 :Test Case Failure. There were no results for the given search string");
        } else
            for (WebElement webElement : searchResults) {
                // Verify that all results contain the searched text
                String elementText = wrapper.getTitleofResult(webElement);
                if (!elementText.contains("iPhone")) {
                    System.out.println(
                            "TestCase 2 :Test Case Failure. Test Results contains un-expected values: " + elementText);
                }
                break;
            }
        System.out.println("Step Success : Successfully validated the search results");

        // Filter out search results having more than 17% discount
        status = wrapper.getDiscountedSearchResults();
        if (!status) {
            System.out.println(
                    "TestCase 2 :Test Case Failure. Test Results are not having products having more than 17% discount");
        }
        System.out.println("Step Success : Successfully printed the search results");

        System.out.println("End Test Case: TestCase 02");

    }

    @Test(enabled = true)
    public void testCase03() throws InterruptedException {
        System.out.println("Start Test Case: TestCase03");
        Wrappers wrapper = new Wrappers(driver);
        boolean status;
        // Search for product "Coffee Mug".
        status = wrapper.searchForProduct("Coffee Mug");
        if (!status) {
            System.out.println("TestCase 3 :Test Case Failure. Unable to search for given product");
        } else
            System.out.println("Step Success : Successfully searched for the product");

        Thread.sleep(4000);
     

        // select 4 stars and above
        List<WebElement> ratingFilterSearchResults= wrapper.selectRating();
        if(ratingFilterSearchResults.size()==0){
            System.out.println("TestCase 3 :Test Case Failure. Rating is not selected");
        }
        System.out.println("Step Success : Successfully applied Rating Filter");

        //print the Title and image URL of the 5 items with highest number of reviews
        wrapper.getHighestReviewResults();

        System.out.println("end Test case : TestCase03");



    }

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}