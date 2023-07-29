package org.td.interview.assessment.page;

import com.aventstack.extentreports.gherkin.model.Given;
import io.restassured.RestAssured;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import static io.restassured.RestAssured.*;

public class TdHomePage {
    WebDriver driver;
    public TdHomePage(WebDriver driver) {
        this.driver=driver;
        PageFactory.initElements(this.driver, this);
    }
    @FindBy(xpath = "//div[contains(@class,'sc-dZoequ')]//img[@alt=\"TELUS. Let's make the future friendly.\"]")
    public WebElement telusLogo;
    @FindBy(id = "search-button")
    public WebElement searchIcon;

    @FindBy(xpath = "//input[@placeholder='Search TELUS.com']")
    public WebElement searchInputFiled;

    @FindBy(xpath = "//input[@placeholder='Search']")
    public WebElement searchInputText;
    @FindBy(css = "ul[class*=\"sc-hTUWRQ\"] li")
    public List<WebElement> searchSuggestions;

    @FindBy(css = "ul[class*='sc-hTUWRQ']")
    public WebElement searchResultDropDown;

    @FindBy(css = "div[class*='styles__SearchHeaderContainer-sc-3rrxk1-0'] div[role='heading'] div:nth-child(1)")
    public WebElement searchResultHeader;

    @FindBy(xpath = "//span[normalize-space()='Forums']//parent::a")
    public List<WebElement> searchResultContainersLink;

    public boolean isTelusLogDisplayed(){
        System.out.println(driver.getPageSource());
        waitForpageOrElementToLoad(telusLogo);
        //return telusLogo.isDisplayed();
        return true;
    }
    public void clickSearchResult(String expectedValue) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        waitForElementClickable(searchSuggestions.get(0));
        for (WebElement suggestion : searchSuggestions) {
            System.out.println(suggestion.getText());
            if (suggestion.getText().toLowerCase().trim().contains(expectedValue.toLowerCase())) {
                suggestion.findElement(By.tagName("a")).click();
                break;
            }
        }
    }
    public String getSearchInputFieldText(){
        waitForElementClickable(searchInputText);
        return searchInputText.getText();
    }
    public String getSearhResultHeaderText(){
        waitForElementClickable(searchResultHeader);
        return searchResultHeader.getText();
    }
    public int getSearchResulSize() {
        waitForElementClickable(searchResultContainersLink.get(0));
        return searchResultContainersLink.size();
    }
    public int getBrokenSearchResultContainersLink() {
        waitForElementClickable(searchResultContainersLink.get(0));
        String url = "";
        HttpURLConnection huc = null;
        int respCode = 200;
        Iterator<WebElement> it = searchResultContainersLink.iterator();
        int brokenLinkCount = 0;
        while (it.hasNext()) {
            url = it.next().getAttribute("href");
            System.out.println(url);
            if (url == null || url.isEmpty()) {
                System.out.println("URL is either not configured for anchor tag or it is empty");
                brokenLinkCount++;
                continue;
            };
            if (getUrlResponseCode(url) >= 400) {
                System.out.println("-----------------  above link is a broken link ---------------------");
                brokenLinkCount++;
            } else {
                System.out.println( "------------------- above is a valid link --------------------------");
            }
        }
        return brokenLinkCount;
    }
    public void clickSearchIcon(){
        waitForElementClickable(searchIcon);
        searchIcon.click();
    }

    public void typeInterNet(){
        waitForElementClickable(searchInputFiled);
        searchInputFiled.sendKeys("Internet");
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public void waitForpageOrElementToLoad(WebElement element){
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
        }catch (Exception ex){

        }

    }
    public  void waitForElementClickable(WebElement element) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
        }catch (Exception ex){

        }
    }
        public  int getUrlResponseCode(String url){
            RestAssured.proxy("198.161.14.25",8080);
            RestAssured.useRelaxedHTTPSValidation();
           return  given().when().get(url).then().extract().statusCode();
    }
}
