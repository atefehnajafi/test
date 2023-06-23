package com.neshan.springh2project.entity.user;

import com.neshan.springh2project.entity.UrlInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_TBL")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy="user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<UrlInfo> urls;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
