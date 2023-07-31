package org.td.interview.assessment.testClass;

import org.testng.annotations.Test;

public class Testcase extends TestBase{
    @Test
    public void searchInternetTroubleshootingTest() throws InterruptedException {
        openApplication("https://www.telus.com/");
        tdHomePageSteps.verifyHomePageLoadedSuccessfully();
        tdHomePageSteps.searchInternetTroubleshooting();
        tdHomePageSteps.verifySearchResult();
    }
}
