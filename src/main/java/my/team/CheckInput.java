/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.team;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jura
 */
public class CheckInput {

    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String LINK_PATTERN = "https://auto.ria.com/.+";

    /**
     * Validate hex with regular expression
     *
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validateMail(final String mail) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mail);
        return matcher.matches();

    }
    
    public static boolean validateLink(String link) {
        pattern = Pattern.compile(LINK_PATTERN);
        matcher = pattern.matcher(link);
        return matcher.matches();

    }

}
