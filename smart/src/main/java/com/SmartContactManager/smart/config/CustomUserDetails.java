package com.SmartContactManager.smart.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.SmartContactManager.smart.entities.User;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        super();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

       SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
       return List.of(simpleGrantedAuthority);
       
        //throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    }

    @Override
    public String getPassword() {
       
        //throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
        return user.getPassword();

    }

    @Override
    public String getUsername() {
       
        //throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
        return user.getEmail();

    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
        //throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
       
        //throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
        
       //throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
    }

    @Override
    public boolean isEnabled() {
        
        //throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
        return true;
    }
    
}
