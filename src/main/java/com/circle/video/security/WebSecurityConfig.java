package com.circle.video.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public UserDetailsService userDetailsService() {
    return new CircleUserDetailsService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().
        authorizeRequests().
        antMatchers("/videos/delete/**").hasAnyAuthority("Admin", "Assistant").
        antMatchers("/users/**").hasAuthority("Admin").
        antMatchers("/guest/**").anonymous().
        anyRequest().authenticated().
        and().
        formLogin().
          loginPage("/login").
          usernameParameter("userName").
        passwordParameter("password").
          defaultSuccessUrl("/home", true).
          permitAll().
    and().logout().permitAll().
    and().rememberMe().key("AbcdEFgHiJkLMnoPqRs_134679582").tokenValiditySeconds(7 * 24 * 60 *60);
  }

  @Override public void configure(WebSecurity web) {
    //web.ignoring().antMatchers("/images/**", "/bootstrap/**", "/css/**", "/fonts/**", "/js/**", "/user-presentations/**", "/user-videos/**", "/user-pictures/**", "/user-profile-image/**");
    web.ignoring().antMatchers("/images/**", "/bootstrap/**", "/css/**", "/fonts/**", "/js/**", "/media/**");
  }

  /*@Override public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().permitAll();
  }*/
}
