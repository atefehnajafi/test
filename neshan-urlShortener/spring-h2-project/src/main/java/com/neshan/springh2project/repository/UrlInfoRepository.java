package com.neshan.springh2project.repository;

import com.neshan.springh2project.entity.UrlInfo;
import com.neshan.springh2project.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface UrlInfoRepository extends JpaRepository<UrlInfo,Long> {
    UrlInfo findByShortUrl(String shortUrl);
    List<UrlInfo> findAllByUser(User user);
    void deleteById(Long id);
    int countUrlInfoByUser(User user);
}
