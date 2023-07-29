package org.td.interview.assessment.testClass;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.td.interview.assessment.pageSteps.TdHomePageSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class TestBase {
    private DesiredCapabilities capabilities;
    private String binaryPath;
    protected TdHomePageSteps tdHomePageSteps;
    WebDriver driver;
    @BeforeClass
    @Parameters({"browserName"})
   public void setUp(String browser){
        initWebDriver(browser);
        tdHomePageSteps=new TdHomePageSteps(driver);
    }

    @AfterClass
    public void tearDown(){
        if(driver!=null){
            driver.quit();
        }
    }
    private void initWebDriver(String browser){
        capabilities = new DesiredCapabilities();
        binaryPath = this.getClass().getClassLoader().getResource("webDriverBinaries/chromedriver.exe").getPath();
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chrome_options = new ChromeOptions();
                chrome_options.addArguments("--remote-allow-origins=*");
                System.setProperty("webdriver.chrome.driver", binaryPath);
                capabilities.setBrowserName("chrome");
                capabilities.setCapability(ChromeOptions.CAPABILITY, chrome_options);
                driver = new ChromeDriver(capabilities);
                break;
            case "firefox":
                break;

        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }
    protected void openApplication(String url){
        driver.get(url);
    }
}
