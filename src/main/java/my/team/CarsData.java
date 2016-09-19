/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.team;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

/**
 *
 * @author jura
 */
public class CarsData {


    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;

public static boolean createConnection(String myURL,String usr, String pwd) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Get a connection
            Properties properties = new Properties();
            properties.setProperty("user", usr);
            properties.setProperty("password", pwd);
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "UTF-8");
            conn = (Connection) DriverManager.getConnection(myURL, properties);
            conn.setCharacterEncoding("UTF-8");
            stmt = (Statement) conn.createStatement();
            System.out.println("Succesfully connected to DB");
            return true;

        } catch (Exception except) {
            except.printStackTrace();
            return false;
        }
    }

    public static void CreateTempDB() {
        try {
            stmt.executeUpdate("CREATE TABLE `table_temp` (\n"
                    + "  `cName` varchar(64) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                    + "  `price` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                    + "  `city` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                    + "  `millage` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                    + "  `engine` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                    + "  `link` varchar(100) NOT NULL,\n"
                    + "  PRIMARY KEY  (`link`)\n"
                    + ") ENGINE=InnoDB;");
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public static void insertNewCar(String name, String price, String city, String mil,
            String engine, String link) throws Exception {
        stmt.executeUpdate("INSERT INTO `table_temp` (`cName`, `price`, `city`, `millage`, `engine`, `link`) "
                + "VALUES ('" + name + "', '" + price + "', '" + city + "', '" + mil + "', '" + engine + "', '" + link + "');");

    }

    public static void addToDB() {
        try {
            stmt.executeUpdate("insert into cars "
                    + "SELECT * FROM table_temp "
                    + "where link not in (select table_temp.link from table_temp "
                    + "inner join cars on table_temp.link = cars.link);");
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    public static void sendSMS(String mailTo) throws Exception {
        Email msg = new Email();
        ResultSet results;
        int i = 0;
        StringBuilder carString = new StringBuilder();

        results = stmt.executeQuery("SELECT * FROM table_temp\n"
                + "where link not in (select table_temp.link from table_temp \n"
                + "inner join cars on table_temp.link = cars.link);");

        while (results.next()) {
            i++;
            carString.append(results.getString("cName") + " \t" + results.getString("price") + " \t"
                    + results.getString("millage") + " \t" + results.getString("engine") + "\n" + results.getString("link") + "\n");
        }
        if (!carString.toString().isEmpty()) {
            System.out.println("\nSending e-mail with " + i + " new car(s)!");
            msg.sendEmail(mailTo, "new cars", carString.toString());
        } else {
            System.out.println("\nNow new cars yet!");
        }

    }

    public static void firstLanch() {
        try {
            System.out.println("Starting");
            stmt.executeQuery("SELECT * FROM cars;");
            System.out.println("Cars DB already exist");
            return;
        } catch (Exception exc) {
            try {
                System.out.println("Can't find the DB");
                stmt.executeUpdate("CREATE TABLE cars (\n"
                        + "  `cName` varchar(64) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                        + "  `price` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                        + "  `city` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                        + "  `millage` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                        + "  `engine` varchar(45) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL COMMENT '',\n"
                        + "  `link` varchar(100) NOT NULL,\n"
                        + "  PRIMARY KEY  (`link`)\n"
                        + ") ENGINE=InnoDB;");
                System.out.println("Successfully create new DB");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void finishWork() {
        try {
            stmt.executeUpdate("drop table table_temp;");
            stmt.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }



}
