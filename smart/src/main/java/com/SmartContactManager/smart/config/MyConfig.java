package com.SmartContactManager.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class MyConfig 
{

   @Bean
   public UserDetailsService userDetailsService(){
    return new UserDetailsServiceImple();
   }

 

@Bean
   public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
   }

   @Bean
   public DaoAuthenticationProvider authenticationProvider(){
    
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthenticationProvider;

   }




 //configure method

   @Bean
   public AuthenticationManager authenticationManager( AuthenticationConfiguration configuration) throws Exception{
      return configuration.getAuthenticationManager();

   }





   
   
   
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

      http.csrf(c-> c.disable())

      .authorizeHttpRequests(request -> request.requestMatchers("/user/**")
      .authenticated().requestMatchers("/**").permitAll().anyRequest().authenticated())


      .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
      .defaultSuccessUrl("/user/index").permitAll());


      
      return http.build();
      

   }


   
      
   //  //   httpSecurity.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
   //  //   .antMatchers("/user/**").hasRole("USER")
   //  //   .antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
   //  http.formLogin(form -> form.loginPage("/login").permitAll());
   
   
   
    
}
