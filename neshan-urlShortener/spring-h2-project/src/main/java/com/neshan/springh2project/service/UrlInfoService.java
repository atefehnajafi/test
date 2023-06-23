package com.neshan.springh2project.service;

import com.neshan.springh2project.exception.UrlNotFoundException;
import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.exception.UrlException;

import java.net.MalformedURLException;
import java.util.List;

public interface UrlInfoService {

     void beforeAdd(String userName) throws Exception;

     UrlInfo createShortUrl(String inputUrl, String userName) throws UrlException;

     List<UrlInfo> listUrl(String userName);

     boolean deleteUrl(Long id) throws UrlException;

     UrlInfo updateCountClickShortUrl(Long id) throws Exception;

     String redirectToOriginal(Long id) throws Exception;

     int countOfShortUrlClick(Long id) throws Exception;

     void deleteAutomaticUrl();

    String redirectToOriginalWithParameter(String shortUrl,Long urlId) throws UrlNotFoundException, MalformedURLException;

    /***

     public String generateShortURLFromOriginalURL(String longURL) {
     String characters = extractCharacters(longURL);
     int length = 6;
     StringBuilder sb = new StringBuilder();


     for (int i = 0; i < length; i++) {
     int randomIndex = random.nextInt(characters.length());
     char randomChar = characters.charAt(randomIndex);
     sb.append(randomChar);
     }


     return "neshan.ir/" + sb.toString();
     }

     private String extractCharacters(String longURL) {
     // Remove "https://" from the beginning of the URL
     String withoutHttps = longURL.replaceFirst("^https://", "");


     // Remove "www" from the beginning of the URL if it exists
     String withoutWWW = withoutHttps.replaceFirst("^www.", "");


     // Remove any remaining special characters
     String characters = withoutWWW.replaceAll("[^a-zA-Z0-9]", "");


     if (characters.isEmpty()) {
     throw new IllegalArgumentException("Invalid URL");
     }


     return characters;
     }
     ***/


}
