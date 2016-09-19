/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.team;


/**
 *
 * @author jura
 */
public class RunCarScanner implements Runnable {

    private String myLink = "";
    private String myMail = "";
    
    public RunCarScanner(String link, String mail){
        myLink = link;
        myMail = mail;
        
    }

    @Override
    public void run() {
        try {
            if(!DBConnect.checkDB()){
                DBConnect.display("Please cheack Database connection");
                return;
            }
            WebScrapper.openTestSite(myLink);
            WebScrapper.siteAction();
            CarsData.CreateTempDB();
            WebScrapper.putCarstoDB();
            CarsData.sendSMS(myMail);
            CarsData.addToDB();
            CarsData.finishWork();

            WebScrapper.closeBrowser();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
