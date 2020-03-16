import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AllegroTestSuite {
    WebDriver driver;
    WebDriverWait wait;




    @BeforeTest
    public void Connection() {
        System.setProperty("webdriver.chrome.driver", "src\\test\\java\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().fullscreen();

        // Connection to allegro.pl
        driver.get("https://www.allegro.pl/");
    }

    @Test
    public void AllegroTest1() {
        wait = new WebDriverWait(driver, 5);
        WebElement consent = driver.findElement(By.xpath("//button[@data-role='accept-consent']"));
        consent.click();

        //2.Wpisujemy w przeglądarce Iphone 11
        WebElement searchField = driver.findElement(By.xpath("//input[@name='string']"));
        searchField.sendKeys("Iphone 11");
        WebElement searchButton = driver.findElement(By.xpath("//button[@data-role='search-button']"));
        searchButton.click();

        //3.Wybieramy kolor
        WebElement colorBlack = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("czarny")));
        colorBlack.click();

        new WebDriverWait(driver, 5).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        //4.Zliczamy ilość wyświetlonych produktów
        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//article[@data-item='true']")));
        System.out.println("There is "+products.size()+" items on page 1");


        //5.Szukamy największej ceny na liście --
        List<WebElement> prices = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//article[@data-item='true']/div/div/div[2]/div[2]/div/div/span")));
        Iterator<WebElement> itr = prices.iterator();
        int maxValue = 0;

        while(itr.hasNext()) {
            WebElement price = itr.next();
            String priceNum = price.getText().replace("zł","")
                    .replace(",","").replace(" ", "").trim();
            int priceInt = Integer.parseInt(priceNum);
            if (priceInt > maxValue) {
                maxValue = priceInt;
            }
        }

        //5.-- wyświetlamy w konsoli
        String topPrice = Integer.toString(maxValue);
        topPrice = new StringBuffer(topPrice).insert(topPrice.length()-2,",").toString();
        System.out.println(topPrice);

        //6. -- Do największej ceny dodajemy 23%
        int maxValueTax = (int)(maxValue*(23.0f/100.0f));
        int totalPrice = maxValue+maxValueTax;
        String topPricewithTax = Integer.toString(totalPrice);
        topPricewithTax = new StringBuffer(topPricewithTax).insert(topPricewithTax.length()-2,",").toString();
        System.out.println(topPricewithTax);
    }

    @AfterTest
    public void Disconnect() {
        driver.quit();
    }

}
