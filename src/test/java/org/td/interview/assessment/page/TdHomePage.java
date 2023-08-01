package org.td.interview.assessment.page;

import io.restassured.RestAssured;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
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

    public boolean isTelusLogoDisplayed(){
        System.out.println("is telus Logo displayed....");
        driver.manage().window().maximize();
        waitForpageOrElementToLoad(telusLogo);
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            File destFile = new File(System.getProperty("user.dir") + "\\screenshot\\screenshot1.png");
           if(destFile.exists()){
               destFile.delete();
           }
            Files.copy(scrFile.toPath(),destFile.toPath() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("is telus Logo displayed end....");
        return telusLogo.isDisplayed();
    }
    public void clickSearchResult(String expectedValue) {
        System.out.println("click search dropdown....");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        waitForElementClickable(searchSuggestions.get(0));
        for (WebElement suggestion : searchSuggestions) {
            System.out.println(suggestion.getText());
            if (suggestion.getText().toLowerCase().trim().contains(expectedValue.toLowerCase())) {
                suggestion.findElement(By.tagName("a")).click();
                System.out.println("click search dropdown done....");
                break;
            }
        }
    }
    public String getSearchInputFieldText(){
        System.out.println("click text from searchInputText....");
        waitForElementClickable(searchInputText);
        return searchInputText.getText();
    }
    public String getSearhResultHeaderText(){
        System.out.println("click text from searchResultHeaderText....");
        waitForElementClickable(searchResultHeader);
        return searchResultHeader.getText();
    }
    public int getSearchResulSize() {
        System.out.println("click text from searchResultHeaderSize....");
        waitForElementClickable(searchResultContainersLink.get(0));
        return searchResultContainersLink.size();
    }
    public int getBrokenSearchResultContainersLink() {
        System.out.println("get browken link....");
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
        System.out.println("click search icon....");
        waitForElementClickable(searchIcon);
        searchIcon.click();
    }

    public void typeInterNet(){
        System.out.println("type internet ....");
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
            System.out.println("get url response ....");
            RestAssured.proxy("198.161.14.25",8080);
            RestAssured.useRelaxedHTTPSValidation();
           return  given().when().get(url).then().extract().statusCode();
    }
}
