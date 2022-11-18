package org.francis.community.core.config.security;

import lombok.extern.slf4j.Slf4j;
import org.francis.community.core.config.security.authentication.JwtAuthenticationFilter;
import org.francis.community.core.config.security.authentication.email.EmailAuthProcessFilter;
import org.francis.community.core.config.security.authentication.email.EmailAuthProvider;
import org.francis.community.core.config.security.authentication.pass.UsernamePasswordProcessFilter;
import org.francis.community.core.config.security.authentication.pass.UsernamePasswordProvider;
import org.francis.community.core.config.security.handler.AuthenticationEntryPointImpl;
import org.francis.community.core.config.security.handler.CustomAuthenticationFailureHandler;
import org.francis.community.core.config.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Franc1s
 * @date 2022/11/14
 * @apiNote
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UsernamePasswordProvider usernamePasswordProvider;

    @Autowired
    private EmailAuthProvider emailAuthProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernamePasswordProvider);
        auth.authenticationProvider(emailAuthProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public UsernamePasswordProcessFilter usernamePasswordProcessFilter() {
        return new UsernamePasswordProcessFilter(authenticationManager, failureHandler, successHandler);
    }

    public EmailAuthProcessFilter emailAuthProcessFilter(){
        return new EmailAuthProcessFilter(authenticationManager, failureHandler, successHandler);
    }

    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources",
                        "/configuration/security", "/swagger-ui.html", "/webjars/**")
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html", "/**/*.css",
                        "/**/*.js");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html", "/**/*.css",
                        "/**/*.js").permitAll()
                // swagger start
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/callback/**").permitAll()
                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(usernamePasswordProcessFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(emailAuthProcessFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
