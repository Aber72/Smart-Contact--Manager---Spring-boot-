package com.SmartContactManager.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.SmartContactManager.smart.dao.UserRepository;
import com.SmartContactManager.smart.entities.User;

public class UserDetailsServiceImple implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        //throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");

        User user =userRepository. getUserByUserName(username);

        if(user == null){
            throw new UsernameNotFoundException("Could not found user");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails;




    }
    
}
