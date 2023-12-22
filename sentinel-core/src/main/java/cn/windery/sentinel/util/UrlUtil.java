package cn.windery.sentinel.util;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    // 正则表达式来检查URL格式
    private static final String URL_PATTERN =
            "^http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\\(\\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+";

    public static void main(String[] args) {
        String url = "https://www.example.com/path/page?query=argument";
        try {
            String domain = extractDomain(url);
            System.out.println("Domain: " + domain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String extractDomain(String url) throws Exception {
        // 检查URL格式
        if (!isValidURL(url)) {
            throw new IllegalArgumentException("Invalid URL format");
        }

        URL netUrl = new URL(url);
        return netUrl.getHost();
    }

    public static boolean isValidURL(String url) {
        Pattern pattern = Pattern.compile(URL_PATTERN);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
