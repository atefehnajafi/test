package com.neshan.springh2project.service;

import com.neshan.springh2project.base.UrlShortener;
import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.entity.user.User;
import com.neshan.springh2project.exception.UrlException;
import com.neshan.springh2project.exception.UrlNotFoundException;
import com.neshan.springh2project.repository.UrlInfoRepository;
import com.neshan.springh2project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UrlInfoServiceImpl implements UrlInfoService {
    private final UrlInfoRepository urlInfoRepository;
    private final UserRepository userRepository;
    private final UrlShortener urlShortener;

    Logger logger = Logger.getLogger(UrlInfoServiceImpl.class.getName());

    public UrlInfoServiceImpl(UrlInfoRepository urlInfoRepository, UserRepository userRepository, UrlShortener urlShortener) {
        this.urlInfoRepository = urlInfoRepository;
        this.userRepository = userRepository;
        this.urlShortener = urlShortener;
    }

    @Override
    public void beforeAdd(String userName) throws UrlException {
        User user = userRepository.findByUserName(userName);
        int count = 0;
        if (user != null) {
            count = urlInfoRepository.countUrlInfoByUser(user);
            if (count >= 10) {
                logger.log(Level.WARNING, "There are more than 10 url for this user for user : " + userName);
                throw new UrlException("There are more than 10 url for this user");
            }
        }

    }

    @Override
    public UrlInfo createShortUrl(String inputUrl, String userName) throws UrlException {
        beforeAdd(userName);
        UrlInfo newUrl = new UrlInfo();
        newUrl.setOriginalUrl(inputUrl);
        newUrl.setCreateDate(LocalDate.now());
        newUrl.setShortUrl(urlShortener.shortenUrl(inputUrl));

        newUrl.setUser(userRepository.findByUserName(userName));
        //todo --- else user info is null
        return urlInfoRepository.save(newUrl);
    }

    @Override
    public List<UrlInfo> listUrl(String userName) {
        User user = userRepository.findByUserName(userName);
        return urlInfoRepository.findAllByUser(user);
    }

    @Override
    public boolean deleteUrl(Long id) throws UrlException {
        Optional<UrlInfo> url = urlInfoRepository.findById(id);
        if (url.isPresent()) {
            urlInfoRepository.deleteById(id);
            return true;
        } else {
            logger.log(Level.WARNING, "can not find url with id : " + id);
            throw new UrlException("can not find url with id : " + id);
        }
    }

    @Override
    public String redirectToOriginal(Long id) throws Exception {
        UrlInfo url = updateCountClickShortUrl(id);
        return url.getOriginalUrl();
    }

    @Override
    public String redirectToOriginalWithParameter(String shortUrlWithParameter, Long urlId) throws UrlNotFoundException, MalformedURLException {
        URL urlWithParam = new URL(shortUrlWithParameter);
        String query = urlWithParam.getQuery();

        Optional<UrlInfo> url = urlInfoRepository.findById(urlId);
        if (!url.isPresent())
            throw new UrlNotFoundException("not exist url by id :  " + urlId);

       return createOriginalUrlWithParameter(url.get().getOriginalUrl(), query);
    }

    private String createOriginalUrlWithParameter(String originalUrl, String parameter) {
        originalUrl = StringUtils.removeEnd(originalUrl, "/");
        if (originalUrl.contains("?"))
            return originalUrl + "&" + parameter;
        else
            return originalUrl + "?" + parameter;
    }

    @Override
    public UrlInfo updateCountClickShortUrl(Long id) throws Exception {
        Optional<UrlInfo> url = urlInfoRepository.findById(id);
        if (url.isPresent()) {
            int count = url.get().getNumberOfVisit();
            count++;
            url.get().setNumberOfVisit(count);
            return urlInfoRepository.save(url.get());
        } else {
            throw new Exception("url not exist");
        }
    }

    @Override
    public int countOfShortUrlClick(Long id) throws Exception {
        Optional<UrlInfo> url = urlInfoRepository.findById(id);
        if (url.isPresent()) {
            return url.get().getNumberOfVisit();
        } else {
            throw new Exception("url not exist");
        }
    }

    @Override
    public void deleteAutomaticUrl() {
        urlInfoRepository.findAll().stream()
                .filter(item -> {
                    Period period = Period.between(item.getCreateDate(), LocalDate.now());
                    return period.getYears() >= 1;
                }).filter(url -> url.getNumberOfVisit() == 0)
                .forEach(item -> {
                    urlInfoRepository.deleteById(item.getId());
                });
    }


}
