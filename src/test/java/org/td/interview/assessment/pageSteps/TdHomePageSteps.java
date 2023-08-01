package org.td.interview.assessment.pageSteps;

import org.openqa.selenium.WebDriver;
import org.td.interview.assessment.page.TdHomePage;
import org.testng.Assert;

public class TdHomePageSteps {
    WebDriver driver;
    TdHomePage tdHomePage;
    public TdHomePageSteps(WebDriver driver){
        this.driver=driver;
        tdHomePage=new TdHomePage(driver);
    }
    public void verifyHomePageLoadedSuccessfully(){
        System.out.println("verify HomePage....");
        Assert.assertTrue(tdHomePage.isTelusLogoDisplayed(),"verify Telus logo is displayed");
    }
    public void searchInternetTroubleshooting(){
        System.out.println("search internet troubleshooting....");
        tdHomePage.clickSearchIcon();
        tdHomePage.typeInterNet();
        tdHomePage.clickSearchResult("Internet Troubleshooting:");
    }
    public void verifySearchResult(){
        System.out.println("verify search result of internet troubleshooting....");
        Assert.assertTrue(tdHomePage.getSearhResultHeaderText()
                .contains(tdHomePage.getSearchInputFieldText()),"verify search result header must contains the search value");
        Assert.assertTrue(tdHomePage.getSearchResulSize()>=6,"verify search result must be greater than 5");
        Assert.assertTrue(tdHomePage.getBrokenSearchResultContainersLink()==0,"verify there is no broken link");
    }
}
