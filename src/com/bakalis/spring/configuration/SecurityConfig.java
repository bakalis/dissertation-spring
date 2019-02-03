package com.bakalis.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.bakalis.spring.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	// Set the AuthenticationProvider we created
	@Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    // Configure the URLs to be Protected and the Login Page provider 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  
      http.authorizeRequests()
      	.antMatchers("/img/**","/css/**", "/js/**").permitAll()
      	.antMatchers("/resources/img/**","/resources/css/**", "/resources/js/**").permitAll()
        .antMatchers("/", "/index").permitAll()
        .anyRequest().authenticated()
        .and().csrf().disable()
        .cors().disable()
        .formLogin().loginPage("/login").permitAll()
        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/index").deleteCookies("JSESSIONID")
        .invalidateHttpSession(true);
  
    }
    
    
    //	Create the Authentication Provider with the Service we Implemented and the BCryptPassword Encoder
    @Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider
	      = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(encoder());
	    return authProvider;
	}
	// Create a password encoder 
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder(11);
	}
	
}
