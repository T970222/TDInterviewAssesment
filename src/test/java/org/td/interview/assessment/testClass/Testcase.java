package org.td.interview.assessment.testClass;

import org.testng.annotations.Test;

public class Testcase extends TestBase{
    @Test
    public void test() throws InterruptedException {
        openApplication("https://www.telus.com/");
        tdHomePageSteps.verifyHomePageLoadedSuccessfully();
        tdHomePageSteps.searchInternetTroubleshooting();
        tdHomePageSteps.verifySearchResult();
        Thread.sleep(10*1000);
    }
}
