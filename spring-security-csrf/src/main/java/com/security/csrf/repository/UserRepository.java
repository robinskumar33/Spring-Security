package com.security.csrf.repository;

import com.security.csrf.model.enumeration.UserType;
import com.security.csrf.model.User;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Sample In Memory DB
@Repository
public class UserRepository{

    private Long idSeq = 1l;

    private List<User> userList = new ArrayList<>();

    @PostConstruct
    public void init(){
        //password = super_admin
        addUser(new User("Robins","robins@gmail.com",
                "$2a$05$rvZFs4OMuCvJRF5UNgv0GOIAIeyPUHCwU1qQaxMopvVWd7ek6g1sS",
                UserType.SUPER_ADMIN));
        //password = admin
        addUser(new User("Admin","admin@gmail.com",
                "$2a$05$Ac0K299JvzUVj0MnhKxva.g6cJR.8KAHz8g2Lf50zaIcS/f/iQq9.",
                UserType.ADMIN));
        //password = user
        addUser(new User("AppUser","user@gmail.com",
                "$2a$05$ks009xTMAPpkT5b3rOo6XOsF4XkmG7jGhpBvk5sRREIGIceCIsas.",
                UserType.APP_USER));
    }

    public User findOneById(Long id){
        for (User user : userList) {
            if (user.getId().equals(id)){
                return user;
            }
        }
        return null;
    }

    public User findOneByEmail(String email){
        for (User user : userList) {
            if (user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }

    public User addUser(User user){
        user.setId(idSeq);
        idSeq++;
        userList.add(user);
        return user;
    }

    public User updateUser(User updatedUser){
        User user = this.findOneById(updatedUser.getId());
        user.setName(updatedUser.getName());
        user.setMobile(updatedUser.getMobile());
        user.setAddress(updatedUser.getAddress());
        return updatedUser;
    }

    public List<User> findAll(){
        return userList;
    }
    public void delete(Long userId){
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()){
            if(iterator.next().getId().equals(userId)){
                iterator.remove();
            }
        }
    }
}
