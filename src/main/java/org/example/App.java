package org.example;

import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.sql.*;


public class App 
{
    public static void main( String[] args )
    {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mate.mrse\\webdriver\\chromedriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
//        if(!System.getProperty("user.name").equals("MateMrse")){
//            options.setBinary("/app/.apt/usr/bin/google-chrome-stable");
//        }
//        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://stampar.hr/hr/peludna-prognoza");
        driver.findElement(By.cssSelector("#perpetuum-cookie-bar .perpetuum-button-dismiss a")).click();
        // Select from dropdown
        String grad;
        List<String> gradovi = Arrays.asList("Zagreb", "Split", "Pula");
        for (String s : gradovi) {
            grad = s;
            Select izbornik = new Select(driver.findElement(By.cssSelector("select[id^='edit-title']")));
            izbornik.selectByVisibleText(grad);
            new WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ajax-progress-fullscreen")));
            String xpath = "//div[@class='biljka-naslov'][contains(., 'Ambrozija')]/following-sibling::div//div[@class='mjerenje-container']//div[contains(@class, 'field-field-vrijednost')][2]";
            LocalDate date = LocalDate.now(ZoneId.of("Europe/Zagreb"));
            LocalTime time = LocalTime.now(ZoneId.of("Europe/Zagreb"));
            String koncentracijaAmbrozije = new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();

            try {
                CSVWriter writer = new CSVWriter(new FileWriter("output.csv", true));
                writer.writeNext(new String[]{koncentracijaAmbrozije, date.toString(), time.toString(), grad}, false);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(grad + ", " + date + " " + time + ": " + koncentracijaAmbrozije);
        }

//
//
//        /****************************************************************************/
//        Connection c = null;
//        Statement stmt = null;
//
//
//// OPEN DATABASE
////        try {
////            Class.forName("org.sqlite.JDBC");
////            c = DriverManager.getConnection("jdbc:sqlite:test.db");
////        } catch ( Exception e ) {
////            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
////            System.exit(0);
////        }
////        System.out.println("Opened database successfully");
//
//
//
//
//
//// CREATE TABLE
////        try {
////            Class.forName("org.sqlite.JDBC");
////            c = DriverManager.getConnection("jdbc:sqlite:test.db");
////            System.out.println("Opened database successfully");
////
////            stmt = c.createStatement();
////            String sql = "CREATE TABLE AMBROZIJA " +
////                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
////                    " LEVEL          INT    NOT NULL, " +
////                    " DATE           STRING   NOT NULL, " +
////                    " TIME           STRING   NOT NULL)";
////            stmt.executeUpdate(sql);
////            stmt.close();
////            c.close();
////        } catch ( Exception e ) {
////            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
////            System.exit(0);
////        }
////        System.out.println("Table created successfully");
//
//
//        // ADD RECORD
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
////            stmt = c.createStatement();
//
////            String sql = "INSERT INTO AMBROZIJA (LEVEL,DATE,TIME) " +
////                    "VALUES ("+koncentracijaAmbrozije+","+date+","+time+");";
//            String sql = "INSERT INTO AMBROZIJA(ID,LEVEL,DATE,TIME) VALUES ($next_id,?,?,?);";
//            PreparedStatement pstmt = c.prepareStatement(sql);
////            pstmt.setInt(1, 1);
//            pstmt.setString(2, koncentracijaAmbrozije);
//            pstmt.setDate(3, Date.valueOf(date));
//            pstmt.setTime(4, Time.valueOf(time));
//            pstmt.executeUpdate();
//
//
////            System.out.println(sql);
////            stmt.executeUpdate(sql);
//
////            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
////                    "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
////            stmt.executeUpdate(sql);
////
////            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
////                    "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
////            stmt.executeUpdate(sql);
////
////            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
////                    "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
////            stmt.executeUpdate(sql);
//
////            stmt.close();
//            pstmt.close();
//
//            c.commit();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Records created successfully");
//
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM AMBROZIJA;" );
//
//            while ( rs.next() ) {
//                int id = rs.getInt("id");
//                String  level = rs.getString("level");
//                Date dateRetrieved = rs.getDate("date");
//                Time timeRetrieved = rs.getTime("time");
//
//                System.out.println( "ID = " + id );
//                System.out.println( "LEVEL = " + level );
//                System.out.println( "DATE = " + dateRetrieved );
//                System.out.println( "TIME = " + timeRetrieved );
//                System.out.println();
//            }
//
//            System.out.println(rs);
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//
//
//        /****************************************************************************/
        driver.quit();
    }
}
