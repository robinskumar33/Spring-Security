package com.security.csrf.model;

import com.security.csrf.model.enumeration.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@Component
public class User{
    private Long id;
    private String name;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private UserType userType;
    private Date createdDate = new Date();
    private Date updatedDate = new Date();

    public User(String name, String email, String password, UserType userType){
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
}
