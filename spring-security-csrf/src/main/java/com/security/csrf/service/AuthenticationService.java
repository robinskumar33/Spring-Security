package com.security.csrf.service;

import com.security.csrf.security.UserInfo;
import com.security.csrf.model.User;
import com.security.csrf.model.enumeration.UserType;
import com.security.csrf.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class AuthenticationService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserRepository userRepository;

    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;

    @Override
    public UserInfo loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = this.userRepository.findOneByEmail(email);

        if (user == null) {
            log.error("User not found for email: " + email);
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        log.info("Found user with id: " + user.getId() + " ;type: " + user.getUserType().toString() + " ;type-name: " + user.getUserType().name() + " ;");

        UserInfo authenticatedUser = new UserInfo(user.getEmail(), user.getPassword(), true,
                accountNonExpired, credentialsNonExpired, accountNonLocked, getAuthorities(user.getUserType()));
        log.info("authenticated user: " + authenticatedUser.toString());
        return authenticatedUser;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(UserType userType) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userType.toString());
        authorities.add(authority);
        return authorities;
    }

    public UserInfo login(String email, String password) throws UsernameNotFoundException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(5);
        System.out.println(encoder.encode(password));
        UserInfo user = this.loadUserByUsername(email);
        if (encoder.matches(password, user.getPassword())) {
            user.eraseCredentials();
            return user;
        } else {
            throw new UsernameNotFoundException("Invalid Credentials");
        }
    }
}