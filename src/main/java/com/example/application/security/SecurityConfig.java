package com.example.application.security;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/*.png")).permitAll();
                auth.requestMatchers("/spoljasnji-saradnici", "/unutrasnji-saradnici").hasRole("ADMIN");
        });


        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users() {
        UserDetails dubravko = User.builder()
                .username("dmalinic")
                // password = password with this hash, don't tell anybody :-)
                .password("{bcrypt}$2a$10$6q4U.xYN8kl8CtM5PZvyuOciyVd5jKSSVy16CZZBVH0rRmdCQ8y/K")
                .roles("USER")
                .build();

        UserDetails ddubravko = User.builder()
                .username("malinicd")
                // password = password with this hash, don't tell anybody :-)
                .password("{bcrypt}$2a$10$6q4U.xYN8kl8CtM5PZvyuOciyVd5jKSSVy16CZZBVH0rRmdCQ8y/K")
                .roles("USER", "ADMIN")
                .build();

        UserDetails vladan = User.builder()
                .username("vmalinic")
                .password("{bcrypt}$2a$10$rozRn5SMxzK4HvHYCaHHa.oL8yumN.p8d0pEKfFloDJmLugeysTOq")
                .roles("USER")
                .build();

        UserDetails branka = User.builder()
                .username("branka")
                .password("{bcrypt}$2a$10$ghzyXleqQb9KNt6stWO/SeyvuqBTQqh1JSCR4pLiaFxhOfUniokCu")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(dubravko, vladan, branka, ddubravko);
    }
}