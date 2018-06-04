package com.security.csrf.controller;

import com.security.csrf.model.User;
import com.security.csrf.model.constant.Constant;
import com.security.csrf.security.UserInfo;
import com.security.csrf.service.AuthenticationService;
import com.security.csrf.utils.ResponseJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/isAuthenticated", method = RequestMethod.GET)
    public boolean isAuthenticated(Principal user) {
        return user != null;
    }

    @RequestMapping(value = "/user" , method = RequestMethod.GET)
    public Principal isAuthorised(Principal user) {
        return user;
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody User loginUser, HttpSession httpSession, HttpServletResponse httpServletResponse) {
        log.info("Authenticating user: " + loginUser.getEmail());
        try {
            UserInfo authenticatedUser = this.authenticationService.login(loginUser.getEmail(), loginUser.getPassword());
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, authenticatedUser.getPassword(), authenticatedUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put(Constant.AUTHENTICATED_USER, authenticatedUser);

            ResponseEntity responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
            return responseEntity;
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(Constant.USER_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "{\"message\":\"success\"}";
    }
}

