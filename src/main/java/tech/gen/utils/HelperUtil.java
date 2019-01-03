package tech.gen.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperUtil {

    /**
     * @return genesisautomationtest+hDrXSwal@gmail.com
     */
    public static String generateEmail() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            double index = Math.random() * characters.length();
            str.append(characters.charAt((int) index));
        }
        return String.format(ConfigProperty.getInstance().getProperty("email.address"), str.toString());
    }

    /**
     * @param regex [\d]+
     * @param data  text1234text
     * @return 1234
     */
    public static String getLink(String regex, String data) {
        Matcher matcher = Pattern.compile(regex).matcher(data);
        return matcher.find() ? matcher.group(1) : "";
    }
}
