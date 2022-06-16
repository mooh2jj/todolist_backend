package com.example.todolist_prac.config.security;

import com.example.todolist_prac.components.jwt.JwtAuthenticationEntryPoint;
import com.example.todolist_prac.components.jwt.JwtAuthenticationFilter;
import com.example.todolist_prac.service.user.OAuth2UserService;
import com.example.todolist_prac.service.user.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;
    private final OAuth2UserService oAuth2UserService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final Environment environment;
    private final String registration = "spring.security.oauth2.client.registration.";

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                // jwt
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/todo/**").permitAll()
                .antMatchers("/users/**").permitAll()
                .anyRequest().authenticated();   // 그외는 인증을 해야 한다.
//                .and()
//                .httpBasic();
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.oauth2Login(oauth2 -> oauth2
//                .clientRegistrationRepository(clientRegistrationRepository())
//                .authorizedClientService(oAuth2AuthorizedClientService())
//        );     // Customizer.withDefaults() 대체
            http.oauth2Login()
                    .userInfoEndpoint()
                    .userService(oAuth2UserService);
//        super.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService() {
//        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
//    }
//
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        List<ClientRegistration> clientRegistrations = Arrays.asList(
//                googleClientRegistration(),
//                facebookClientRegistration()
//        );
//
//        return new InMemoryClientRegistrationRepository(clientRegistrations);
//    }
//
//    private ClientRegistration googleClientRegistration() {
//        final String clientId = environment.getProperty(registration + "google.client-id");
//        final String clientSecret = environment.getProperty(registration + "google.client-secret");
//
//        return CommonOAuth2Provider
//                .GOOGLE
//                .getBuilder("google")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }
//
//    private ClientRegistration facebookClientRegistration() {
//        final String clientId = environment.getProperty(registration + "facebook.client-id");
//        final String clientSecret = environment.getProperty(registration + "facebook.client-secret");
//
//        return CommonOAuth2Provider
//                .FACEBOOK
//                .getBuilder("facebook")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }
}
