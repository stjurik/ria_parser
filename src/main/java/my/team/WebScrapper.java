package my.team;
        

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import my.team.CarsData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebScrapper {

    private static List < WebElement >  elements  = new ArrayList<>();
    private static WebDriver driver;
    private static String link = "https://auto.ria.com/";
    
    public static void openTestSite(String newLink) {
        driver = new FirefoxDriver();
        link = newLink;
        driver.get(link);
        System.out.println("Loking on\n"+link);
    }
    public static void siteAction(){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("scroll(0, 2800)");
        
    }
    public static String[] separateString(String text) {
        try {
            String[] strArray = new String[20];
            int i = 0;//index for strArray
            strArray = text.split("\n");
            if (!checkCarName(strArray[0].substring(0, 3))) {
                i = 1;
            }
            String aName = strArray[i++];
            String price = strArray[i++];
            String city = strArray[i++];
            String millage = strArray[i++];
//            millage = millage.substring(0, millage.indexOf("т"));
//            millage = millage.replace(" ", "");
            String engine = strArray[i++];
            return new String[]{aName, price, city, millage, engine};
        } catch (Exception exc) {
            exc.printStackTrace();
            return new String[]{"error", "0", "error", "error", "error"};
        }
    }
        
    /**
     * Function return true if in first cell the car name(english letters, first letter in upper case).
     * Else return false.
     */
    public static boolean checkCarName(String forCheckString){
        String emplant = "[A-Z][a-z]+";
        Pattern pat = Pattern.compile(emplant);
        Matcher m = pat.matcher(forCheckString);
        if(m.matches())
            return true;
        return false;
    }
	/**
	 * 
	 * @param username
	 * @param Password
	 * 
	 *            Logins into the website, by entering provided username and
	 *            password
	 */

    public static void putCarstoDB() {
        int i = 0;
        String linkStr= "";
        String [] strData = new String[5];
        try {
            elements = driver.findElements(By.className("content"));
            //wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            for (WebElement element : elements) {
                strData = separateString(element.getText());
                toString(strData);
                WebElement link = element.findElement(By.className("address"));
                linkStr = link.getAttribute("href");
                CarsData.insertNewCar(strData[0], strData[1],
                        strData[2],strData[3],strData[4],linkStr);
                i++;
                if(i==10)
                    break;
                
            }
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }
//		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("status.txt"), "utf-8"));
//		writer.write(text);
//		writer.close();
        System.out.println(i+" elment(s) has been parsing");
        return;

    }
    
    public static void closeBrowser() {driver.close();	}
        
    public static void toString(String [] arr) {
        for (String arr1 : arr) {
            System.out.println(arr1);
        }
    }
    
    public static void setLink(String newLink) {
        link = newLink;
    }
}
