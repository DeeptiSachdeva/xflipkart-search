package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {

    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
    String url = "https://www.flipkart.com";


    public Wrappers(WebDriver driver){
        this.driver= driver;
    }


    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(url)) {
            this.driver.get(url);
        }
        // Close the login popup if it appears
    try {
            WebElement closeButton = driver.findElement(By.xpath("//button[contains(text(),'✕')]"));
            closeButton.click();
        } catch (Exception e) {
            //e.printStackTrace();
        }
 }

    public boolean searchForProduct(String product) {   
        try {
            // Clear the contents of the search box and Enter the product name in the search box
            boolean status=false;
            WebElement searchBox= wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.name("q"))));
            searchBox.sendKeys(Keys.CONTROL + "a");
            searchBox.sendKeys(Keys.DELETE);
            searchBox.sendKeys(product);
            searchBox.submit();

            WebElement searchResults = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='_75nlfW']"))));
            if(searchResults.isDisplayed()){
                status=true;
            }    
            return status;        
        } 
            catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() ;
        try {
            // Find all webelements corresponding to the card content section of each of search results
            searchResults = driver.findElements(By.xpath("//div[@class='_75nlfW']"));
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;
        }
    }

    public boolean sortBy(String sortCriteria) throws InterruptedException{
        //sort by criteria
        boolean status=true;
        WebElement popularityOption = driver.findElement(By.xpath("//div[contains(text(),'"+sortCriteria+"')]"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();",popularityOption);

          // Wait for results to
        Thread.sleep(2000);                                                                                       
        WebElement sorted=wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='zg-M3Z _0H7xSG' and contains(text(),'Popularity')]"))));
        if(sorted.isDisplayed()){
            status=true;
        }
        return status;
    }


    public String getTitleofResult(WebElement parentElement) {
        String titleOfSearchResult = "";
        // Find the element containing the title (product name) of the search result and
        // assign the extract title text to titleOfSearchResult
        titleOfSearchResult = parentElement.getText();
        return titleOfSearchResult;
    }

    public int countResults(){
        // Count items with rating less than or equal to 4 stars
        List<WebElement> ratings = driver.findElements(By.xpath("//div[@class= 'XQDdHH']"));

        int count = 0;
        for (WebElement rating : ratings) {
            String ratingText = rating.getText();
            if (!ratingText.isEmpty()) {
                double ratingValue = Double.parseDouble(ratingText);
                if (ratingValue <= 4.0) {
                    count++;
                }
            }
        }
        return count;
    } 


    public boolean getDiscountedSearchResults(){
        boolean status=false;
        List<WebElement> searchResults =driver.findElements(By.xpath("//div[@class='_75nlfW']"));
        for(WebElement discount : searchResults){
            // Extract the title
            String Title = discount.findElement(By.xpath(".//div[@class='KzDlHZ']")).getText();      
            // Extract the discount percentage
            String discountText = "";
                try {
                    discountText = discount.findElement(By.xpath(".//span[contains(text(),'%')]")).getText();
                    String[] discountper = discountText.split("%");
                    int discountPercentage= Integer.parseInt(discountper[0]);
                    // Print title and discount if discount is more than 17%
                    if (discountPercentage > 17) {
                        System.out.println("Title :" + Title);
                        System.out.println("Discount Percentage :" + discountText);
                    }
                    status=true;
                 }catch(Exception e){

                 }
        }
        return status;
        
    }


   /*  public List<WebElement> getRatingBasedSearchResults(String operator , double ratingRequired){
        System.out.println("Operator is :" + operator);
        List<WebElement> results = new ArrayList<WebElement>();
        List<WebElement> ratings = driver.findElements(By.xpath("//div[@class= 'XQDdHH']"));
        for (WebElement rating : ratings) {
            String ratingText = rating.getText();
            if (!ratingText.isEmpty()) {
                double ratingValue = Double.parseDouble(ratingText);
                    if(operator.equals("less than")){
                            if (ratingValue < ratingRequired){
                                results.add(rating);
                               
                            }
                    }else if(operator.equals("less than or equal to")){
                            if (ratingValue <= ratingRequired){ 
                                results.add(rating); 
                            }
                        }else if(operator.equals("greater than or equal to")){
                            if (ratingValue >= ratingRequired){
                                results.add(rating);   
                            }
                        }else if(operator.equals("greater than")){
                            if (ratingValue > ratingRequired){
                                results.add(rating);        
                            }
                         }else if(operator.equals("equal to")){
                              if (ratingValue == ratingRequired){
                                     results.add(rating);
                         }
                         } else {
                             System.out.println("Filter criteria is not correct.");
                     }
                 }
         }
          return results;
    } */

    public List<WebElement> selectRating() throws InterruptedException{
        WebElement fourStarsFilter =driver.findElement(By.xpath("//div[@class='_6i1qKy' and contains(text(),'4')]"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", fourStarsFilter);
        Thread.sleep(2000);
        // Wait for results to refresh
        List<WebElement> searchResults= driver.findElements(By.xpath("//div[@class='_75nlfW']"));
        return searchResults;
    }

    //print the Title and image URL of the 5 items with highest number of reviews
    public void getHighestReviewResults(){
       // System.out.println("Entered getHighestReviewResults method");
        // List to store items with their titles, image URLs, and review counts
         List<Item> items = new ArrayList<>();

            // Find all items in the results
           List<WebElement> searchResults = driver.findElements(By.xpath("//div[@class='_75nlfW']"));
            for (WebElement product : searchResults) {
               // System.out.println("Product details :" +product.getText());
                try {
                    String title = product.findElement(By.xpath("//a[@class='wjcEIp']")).getAttribute("title");
                    String imageUrl = product.findElement(By.xpath(".//img[contains(@class, 'DByuf4')]")).getAttribute("src");
                    String reviewsText = product.findElement(By.xpath("//span[@class='Wphh3N']")).getText();
                    int reviewsCount = Integer.parseInt(reviewsText.replaceAll("[^0-9]", ""));

                    items.add(new Item(title, imageUrl, reviewsCount));        
                } catch (Exception e) {
                    e.printStackTrace();
                   
                }
            }

            // Sort items by review count in descending order
            items.sort(Comparator.comparingInt(Item::getReviewsCount).reversed());
           

            // Print the title and image URL of the top 5 items
            for (int i = 0; i < Math.min(5, items.size()); i++) {
                Item item = items.get(i);
                System.out.println("Title: " + item.getTitle());
                System.out.println("Image URL: " + item.getImageUrl());
                System.out.println("Reviews Count: " + item.getReviewsCount());
                System.out.println();
            }
    }
}
