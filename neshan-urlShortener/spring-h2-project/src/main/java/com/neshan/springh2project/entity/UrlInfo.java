package com.neshan.springh2project.entity;

import com.neshan.springh2project.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "URL_TBL")
public class UrlInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ORIGINAL_URL")
    private String originalUrl;

    @Column(name = "SHORT_URL")
    private String shortUrl;

    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @Column(name = "NUMBER_OF_VISIT")
    private int numberOfVisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName="id", nullable = false)
    private User user;
}
