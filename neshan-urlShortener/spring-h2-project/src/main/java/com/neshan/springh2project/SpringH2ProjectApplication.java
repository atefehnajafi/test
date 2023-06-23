package com.neshan.springh2project;

import ch.qos.logback.core.net.server.Client;
import com.neshan.springh2project.base.UrlShortener;
import com.neshan.springh2project.config.LoadBalancer;
import com.neshan.springh2project.config.RoundRobinLoadBalancer;
import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.entity.user.User;
import com.neshan.springh2project.repository.UrlInfoRepository;
import com.neshan.springh2project.repository.UserRepository;
import com.neshan.springh2project.service.UrlInfoService;
import com.neshan.springh2project.service.UrlInfoServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringH2ProjectApplication {

    public SpringH2ProjectApplication(UrlInfoService urlInfoService) {
        this.urlInfoService = urlInfoService;
    }

    Logger logger = Logger.getLogger(UrlInfoServiceImpl.class.getName());

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    final UrlInfoService urlInfoService;

    public static void main(String[] args) {
        SpringApplication.run(SpringH2ProjectApplication.class, args);

        int NUM_OF_REQUESTS = 15;

        ArrayList<String> ipPool = new ArrayList<>();
        ipPool.add("192.168.0.1");
        ipPool.add("192.168.0.2");
        ipPool.add("192.168.0.3");
        ipPool.add("192.168.0.4");
        ipPool.add("192.168.0.5");


        printNextTurn("Round-Robin");
        LoadBalancer roundRobbin = new RoundRobinLoadBalancer(ipPool);
        simulateConcurrentClientRequest(roundRobbin, NUM_OF_REQUESTS);


    }


    private static void simulateConcurrentClientRequest(LoadBalancer loadBalancer, int numOfCalls) {

        IntStream
                .range(0, numOfCalls)
                .parallel()
                .forEach(i ->
                        System.out.println(
                                "IP: " + loadBalancer.getIp()
                                        + " --- Request from Client: " + i
                                        + " --- [Thread: " + Thread.currentThread().getName() + "]")
                );
    }


    private static void printNextTurn(String name) {
        System.out.println("---");
        System.out.println("Clients starts to send requests to " + name + " Load Balancer");
        System.out.println("---");
    }


    /**
     * create Data For Test ---- and Automatic deletion of data that has not been used for a year
     */
    @Bean
    public CommandLineRunner mappingDemo(UserRepository userRepository,
                                         UrlShortener urlShortener,
                                         UrlInfoRepository urlInfoRepository) {
        return args -> {


            User user1 = new User();
            user1.setUserName("user1");
            user1.setPassword("pass111");
            userRepository.save(user1);

            for (int i = 0; i < 5; i++) {
                UrlInfo urlInfo1 = new UrlInfo();
                urlInfo1.setUser(user1);
                urlInfo1.setCreateDate(LocalDate.now().minusYears(1));
                urlInfo1.setOriginalUrl("https://teamtreehouse.com/community/how-to-add-an-object-and-then-another-object-as-an-attribute-to-the-first-object-in-a-thymeleaf-template");
                urlInfo1.setShortUrl(urlShortener.shortenUrl("https://teamtreehouse.com/community/how-to-add-an-object-and-then-another-object-as-an-attribute-to-the-first-object-in-a-thymeleaf-template"));
                urlInfoRepository.save(urlInfo1);
            }


            for (int i = 0; i < 3; i++) {
                UrlInfo urlInfo1 = new UrlInfo();
                urlInfo1.setUser(user1);
                urlInfo1.setCreateDate(LocalDate.now().minusMonths(7));
                urlInfo1.setOriginalUrl("https://teamtreehouse.com/community/how-to-add-an-object-and-then-another-object-as-an-attribute-to-the-first-object-in-a-thymeleaf-template");
                urlInfo1.setShortUrl(urlShortener.shortenUrl("https://teamtreehouse.com/community/how-to-add-an-object-and-then-another-object-as-an-attribute-to-the-first-object-in-a-thymeleaf-template"));
                urlInfoRepository.save(urlInfo1);
            }

            logger.log(Level.INFO, "Automatic deletion of data that has not been used for a yearLog");
            urlInfoService.deleteAutomaticUrl();

        };
    }


}
