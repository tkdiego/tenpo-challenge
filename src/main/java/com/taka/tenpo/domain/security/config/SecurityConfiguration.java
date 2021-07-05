package com.taka.tenpo.domain.security.config;


import com.taka.tenpo.domain.security.config.entrypoint.CustomAuthenticationEntryPoint;
import com.taka.tenpo.domain.security.filter.TokenInspectorFilter;
import com.taka.tenpo.domain.security.service.ISessionSecurityService;
import com.taka.tenpo.domain.security.service.JwtService;
import com.taka.tenpo.domain.security.service.SessionService;
import com.taka.tenpo.domain.security.service.UserCredentialService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.taka.tenpo.controller.URLConstants.API_DOCS;
import static com.taka.tenpo.controller.URLConstants.AUTH;
import static com.taka.tenpo.controller.URLConstants.LOGIN;
import static com.taka.tenpo.controller.URLConstants.SECURITY;
import static com.taka.tenpo.controller.URLConstants.SIGN_IN;
import static com.taka.tenpo.controller.URLConstants.SWAGGER_RESOURCES;
import static com.taka.tenpo.controller.URLConstants.SWAGGER_UI;
import static com.taka.tenpo.controller.URLConstants.UI;
import static com.taka.tenpo.controller.URLConstants.V2;
import static com.taka.tenpo.controller.URLConstants.WEBJARS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AccessDeniedHandlerImpl accessDeniedHandler;

    private final JwtService jwtService;

    private final ISessionSecurityService sessionService;

    private final UserCredentialService userCredentialService;


    @Lazy
    public SecurityConfiguration(AccessDeniedHandlerImpl accessDeniedHandler, JwtService jwtService,
                                 SessionService sessionService, UserCredentialService userCredentialService) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
        this.userCredentialService = userCredentialService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(createCustomAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler)
                .and().authorizeRequests()
                .antMatchers(AUTH + SIGN_IN).permitAll()
                .antMatchers(AUTH + LOGIN).permitAll()
                .antMatchers(API_DOCS).permitAll()
                .antMatchers(UI).permitAll()
                .antMatchers(SWAGGER_RESOURCES + "/**").permitAll()
                .antMatchers(SECURITY).permitAll()
                .antMatchers(SWAGGER_UI + ".html").permitAll()
                .antMatchers(SWAGGER_UI + "/*").permitAll()
                .antMatchers(WEBJARS + "/**").permitAll()
                .antMatchers(V2 + "/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterAfter(createTokenInspectorFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenInspectorFilter createTokenInspectorFilter() {
        return new TokenInspectorFilter(userCredentialService, jwtService, sessionService);
    }

    @Bean
    public CustomAuthenticationEntryPoint createCustomAuthenticationEntryPoint() {
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
        return customAuthenticationEntryPoint;
    }

    @Bean
    public AccessDeniedHandlerImpl createAccessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userCredentialService).passwordEncoder(createPasswordEncoder());
    }
}
