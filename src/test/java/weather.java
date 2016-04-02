/**
 * A small Selenium program to fetch weather results for a given zipcode from Weather.com through Google.
 * Created by Kevin on 4/1/2016.
 */

import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class weather {

    @Test
    public void weatherResults() throws InterruptedException {
        final String zipcode = "20001"; //Input the desired zipcode here

        WebDriver driver = new FirefoxDriver(); //start the WebDriver
        driver.manage().window().setSize(new Dimension(1920, 1080)); //Rescale the window - note, if your monitor is
        // not 1920x1080 either disable this or change
        // the values.

        driver.get("http://google.com"); //go to google
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("weather.com\n"); //search for weather.com
        element.submit();

        waitOnPageId(driver, "resultStats"); //wait for it to load

        driver.findElement(By.xpath
                ("/html/body/div[1]/div[5]/div[4]/div[7]/div[1]/div[3]" +
                        "/div/div[2]/div[2]/div/div/div/div[1]/div/div/h3/a")).click(); //click the first result

        waitOnPageXpath(driver, "//*[@id=\"wx-header-wrap\"]/div/div/div/div[2]/div[2]/div/section/div/form/input");

        element = driver.findElement(By.xpath
                ("//*[@id=\"wx-header-wrap\"]/div/div/div/div[2]/div[2]/div/section/div/form/input"));
        element.sendKeys(zipcode); //type in the zipcode
        element.submit();

        waitOnPageXpath(driver, "/html/body/div[1]/div[1]/header/div/div/div/div[2]/div[2]" +
                "/div/section/div/div[2]/div/div/div/div/ul/li/div/div/p");

        element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/header/div/div/div/div[2]/div[2]/div/section/div/div[1]"));
        element.click(); //search for the zipcode

        /*
         * Now that we are one the correct page for our given zipcode, we pull all the displayed information down and output
         * it to the console.
         */
        element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/section[1]/div/section/div/div/div/glomo-right-now/section"));
        String output = element.getText();
        String s1[] = output.split("\n");
        element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/section/div[1]/div/div/div[1]/div/div[2]"));
        String timestamp = element.getText();

        System.out.println("########################################################");
        System.out.println("Zip Code: " + zipcode + "\n");
        System.out.println(timestamp);
        System.out.println("\t" + s1[0] + " - " + s1[1] + ", " + s1[2]);
        for(int i=3;i<s1.length-1;i++){
            System.out.println("\t" + s1[i] + " - " + s1[i+1]);
            i+=1;
        }
        System.out.println("\n");

        element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/section[2]"));
        output = element.getText();
        String s2[] = output.split("\n");

        printHeader();
        for(int i=1;i<s2.length-1;i++){
            printFLine(s2[i], s2[i+1],s2[i+2], s2[i+3], s2[i+4]);
            i+=4;
        }
        System.out.println();
        System.out.println("########################################################");

        implicitWait(driver, 30);

        driver.close();
    }

    /**
     * prints a formatted weather line
     * @params all the required fields that are retrieved from Weather.com
     * @return void
     */
    public void printFLine(String day, String temp, String desc, String prec, String wind_hum){
        System.out.printf("\n\t%-20s %6s %30s %15s %20s", day, temp, desc, prec, wind_hum);
    }

    /**
     * Prints a header for the weather information.
     * @return void
     */
    public void printHeader(){
        System.out.println("Brief forecast:");
        System.out.printf("\t%-20s %6s %30s %15s %20s", "Day", "Temp", "Description", "Precip", "Wind/Humidity");
    }

    /**
     * Waits until an ID can be located
     * @param driver passed WebDriver
     * @param ID    ID to look for
     */
    public void waitOnPageId(WebDriver driver, String ID){
        WebElement myDynamicElement = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.id(ID)));
    }

    /**
     * Waits until a given XPath is seen
     * @param driver passed WebDriver
     * @param xpath XPath to look for
     */
    public void waitOnPageXpath(WebDriver driver, String xpath){
        WebElement myDynamicElement = (new WebDriverWait(driver, 20))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    /**
     * Implicitly waits for a given amount of time
     * @param driver passed WebDriver
     * @param waitTime time to wait
     */
    public void implicitWait(WebDriver driver, int waitTime){
        driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
    }

}
