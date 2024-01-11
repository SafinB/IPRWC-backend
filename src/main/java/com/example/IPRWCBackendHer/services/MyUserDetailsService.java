package com.example.IPRWCBackendHer.services;

import com.example.IPRWCBackendHer.DAO.UserDao;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public MyUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.example.IPRWCBackendHer.models.User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Could not findUser with email = " + email));

        return new User(
                email,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
