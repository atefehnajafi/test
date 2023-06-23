package com.neshan.springh2project.base;

import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.repository.UrlInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UrlShortener {

    @Autowired
    UrlInfoRepository urlInfoRepository;

    // A base 62 string that contains digits, lowercase and uppercase letters
    private final String BASE_62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // A method to convert an integer value to a base 62 string
    private String toBase62(int value) {
        StringBuilder sb = new StringBuilder();

        // Get the absolute value of the integer
        int absValue = Math.abs(value);

        // Convert the absolute value to a base 62 string
        while (absValue > 0) {
            sb.append(BASE_62.charAt(absValue % 62));
            absValue /= 62;
        }

        // Add a sign character at the end of the base 62 string
        if (value < 0) {
            sb.append("-");
        } else {
            sb.append("+");
        }

        // Reverse and return the base 62 string
        return sb.reverse().toString();

    }


    // A method to convert a long URL to a short URL
    public String shortenUrl(String longUrl) {

        // Get the hash code of the long URL
        int hashCode = longUrl.hashCode();

        // Convert the hash code to a base 62 string
        String shortUrl = toBase62(hashCode);

        // Pad the short URL with leading zeros if necessary
        while (shortUrl.length() < 6) {
            shortUrl = "0" + shortUrl;
        }

        /** Check if the short URL is already in the map **/
        if (checkDuplicateShortUrl(shortUrl)) {
            // Generate a new hash code by adding a random number
            hashCode += (int) (Math.random() * 1000);

            // Convert the new hash code to a base 62 string
            shortUrl = toBase62(hashCode);

            // Pad the short URL with leading zeros if necessary
            while (shortUrl.length() < 6) {
                shortUrl = "0" + shortUrl;
            }
        }

        return "http://nsh.org/" + shortUrl;

    }

    public boolean checkDuplicateShortUrl(String shortUrl) {
        UrlInfo urlInfo = urlInfoRepository.findByShortUrl("http://nsh.org/" + shortUrl);
        return urlInfo != null;
    }

    private int decode(String base62) {
        int result = 0;

        // Get the sign character at the end of the base 62 string
        char sign = base62.charAt(base62.length() - 1);

        // Remove the sign character from the base 62 string
        base62 = base62.substring(0, base62.length() - 1);

        // Convert the base 62 string to an integer value
        for (int i = 0; i < base62.length(); i++) {
            result = result * 62 + BASE_62.indexOf(base62.charAt(i));
        }

        // Apply the sign to the result
        if (sign == '-') {
            result = -result;
        }

        // Return the result
        return result;
    }

    // A method to get the long URL from a short URL
//    public String getLongUrl(String shortUrl) {
//// Return the long URL from the map or null if not found
//        return urlMap.getOrDefault(shortUrl, null);
//    }
}
