package com.mindhub.homebanking.configurations;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected  void  configure(HttpSecurity httpSecurity) throws  Exception{
        httpSecurity.authorizeRequests()
                .antMatchers("/admin/**", "/rest/**", "/h2-console/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clients/current").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("ADMIN", "CLIENT")
                .antMatchers("/web/**", "/Web/**", "/wEb/**", "/weB/**", "/WEb/**", "/wEB/**","/WeB/**", "/WEB/**" ).hasAnyAuthority("ADMIN", "CLIENT")
                .antMatchers("/**" , "/landingPage/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").permitAll();


        httpSecurity.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        httpSecurity.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

            // turn off checking for CSRF tokens

        httpSecurity.csrf().disable();

            //disabling frameOptions so h2-console can be accessed

            httpSecurity.headers().frameOptions().disable();

            // if user is not authenticated, just send an authentication failure response

        httpSecurity.exceptionHandling().authenticationEntryPoint((req, res, exc) -> {
            if (req.getRequestURI().contains("web") || req.getRequestURI().contains("admin") || req.getRequestURI().contains("api") || req.getRequestURI().contains("rest")){
                res.sendRedirect("/logging.html");
            }
        });

            // if login is successful, just clear the flags asking for authentication

        httpSecurity.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

            // if login fails, just send an authentication failure response

        httpSecurity.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

            // if logout is successful, just send a success response

            httpSecurity.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        }
        //PARA NO LOGUEARSE A CADA RATO POR CADA PETICION
        private void clearAuthenticationAttributes(HttpServletRequest request) {

            HttpSession session = request.getSession(false);

            if (session != null) {

                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            }

        }


    }

