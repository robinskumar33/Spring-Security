package com.security.csrf.service;

import com.security.csrf.model.User;
import com.security.csrf.model.constant.Constant;
import com.security.csrf.model.enumeration.UserType;
import com.security.csrf.repository.UserRepository;
import com.security.csrf.utils.ResponseJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    private  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);

    public boolean validateEmail(String email) {
        User user = this.userRepository.findOneByEmail(email);
        return user == null;
    }

    public Map addAdminUser(User addUserObj) {
        User user = this.userRepository.findOneByEmail(addUserObj.getEmail());
        if (user != null && user.getId() > 0) {
            return ResponseJsonUtil.getFailedResponseJson(Constant.EMAIL_ID_ALREADY_EXIST,Constant.USE_DIFF_EMAIL);
        } else {
            user = new User();
            user.setName(addUserObj.getName());
            user.setEmail(addUserObj.getEmail());
            user.setUserType(UserType.APP_USER);
            user.setMobile(addUserObj.getMobile());
            user.setAddress(addUserObj.getAddress());
            user.setPassword(passwordEncoder.encode(addUserObj.getPassword()));
            user = this.userRepository.addUser(user);
            return ResponseJsonUtil.getSuccessResponseJson(user);
        }
    }

    public Map searchUsersByName(String name) throws Exception {
        List<User> res = new ArrayList<>();
        List<User> users = this.userRepository.findAll();
        users.forEach(user -> {
            if (user.getName().toLowerCase().contains(name.toLowerCase())) {
                res.add(user);
            }
        });
        if (res.size() == 0) {
            throw new Exception(Constant.USER_NOT_FOUND);
        } else {
            return ResponseJsonUtil.getSuccessResponseJson(res);
        }
    }

    public Map searchUsersByEmail(String email) throws Exception {
        User user = userRepository.findOneByEmail(email);
        if (user == null) {
            throw new Exception("User Not Found email : "+email);
        } else {
            return ResponseJsonUtil.getSuccessResponseJson(user);
        }
    }

    public User getUsersById(Long id) throws Exception {
        User user = userRepository.findOneById(id);
        if (user == null) {
            throw new Exception("User Not Found id : "+id);
        } else {
            return user;
        }
    }

    public Map userTypesList() {
        EnumSet<UserType> userTypes = EnumSet.allOf(UserType.class);
        return ResponseJsonUtil.getSuccessResponseJson(userTypes);
    }

    public Map listAllUsers() {
        List<User> users = this.userRepository.findAll();
        return ResponseJsonUtil.getSuccessResponseJson(users);
    }

    public Map updateUser(User updatedUser) {
        User user = this.userRepository.findOneById(updatedUser.getId());
        if (user == null) {
            return ResponseJsonUtil.getFailedResponseJson(Constant.USER_NOT_FOUND);
        } else {
            user.setName(updatedUser.getName());
            user.setMobile(updatedUser.getMobile());
            user.setAddress(updatedUser.getAddress());
            User result = this.userRepository.updateUser(user);

            return ResponseJsonUtil.getSuccessResponseJson(result);
        }
    }

    public Map deleteUser(Long userId) {
        User user = this.userRepository.findOneById(userId);
        if (user == null) {
            return ResponseJsonUtil.getFailedResponseJson("invalidInputs", "Invalid User Id!");
        }
        userRepository.delete(userId);

        return ResponseJsonUtil.getSuccessResponseJson();
    }
}
