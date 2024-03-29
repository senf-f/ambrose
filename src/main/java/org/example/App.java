package org.example;

import com.opencsv.CSVWriter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class App {
    public static void main(String[] args) {

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
//        if(!System.getProperty("user.name").equals("MateMrse")){
//            options.setBinary("/app/.apt/usr/bin/google-chrome-stable");
//        }
        options.addArguments("headless");
        // For heroku
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        //
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://stampar.hr/hr/peludna-prognoza");
        driver.findElement(By.cssSelector("#perpetuum-cookie-bar .perpetuum-button-dismiss a")).click();
        // Select from dropdown
        String grad;
        String koncentracijaAmbrozije;
        List<String> gradovi = Arrays.asList("Zagreb", "Split", "Pula", "Zadar", "Dubrovnik");
        for (String s : gradovi) {
            grad = s;
            Select izbornik = new Select(driver.findElement(By.cssSelector("select[id^='edit-title']")));
            izbornik.selectByVisibleText(grad);
            new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ajax-progress-fullscreen")));
            String xpath = "//div[@class='biljka-naslov'][contains(., 'Ambrozija')]/following-sibling::div//div[@class='mjerenje-container']//div[contains(@class, 'field-field-vrijednost')][2]";
            LocalDate date = LocalDate.now(ZoneId.of("Europe/Zagreb"));
            LocalTime time = LocalTime.now(ZoneId.of("Europe/Zagreb"));
            int rezultat = driver.findElements(By.xpath(xpath)).size();
            if(rezultat>0){
                koncentracijaAmbrozije = new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
            } else {
                koncentracijaAmbrozije = "0.0";
            }
            try {
                CSVWriter writer = new CSVWriter(new FileWriter(year+"-"+month+" "+grad+" - ambrozija.csv", true));
                writer.writeNext(new String[]{koncentracijaAmbrozije, date.toString(), time.toString(), grad}, false);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(grad + ", " + date + " " + time + ": " + koncentracijaAmbrozije);
        }
        driver.quit();
    }
}
