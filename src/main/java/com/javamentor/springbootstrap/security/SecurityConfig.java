package com.javamentor.springbootstrap.security;


import com.github.scribejava.apis.GoogleApi20;
import com.github.scribejava.core.builder.ScopeBuilder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    public SecurityConfig(@Qualifier("userServiceImpl") UserDetailsService userDetailsService, SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }

        @Autowired
        public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder()); // конфигурация для прохождения аутентификации
        }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/login**", "/logout").permitAll() // доступность всем
                .antMatchers("/crud_user/**", "/rest/**").access("hasAnyRole('ROLE_ADMIN')") // разрешаем входить на /user пользователям с ролью MyUser, Admin
                .antMatchers("/show_my_user").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
                .and()
                .formLogin()  // Spring сам подставит свою логин форму
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successUserHandler) // подключаем наш SuccessHandler для перенеправления по ролям
                .and().logout()
                .logoutUrl("/logout") //URL-адрес, запускающий выход из системы (по умолчанию "/ logout").
                .logoutSuccessUrl("/login") //URL-адрес для перенаправления после выхода из системы.
                .and()
                .csrf().disable();

    }

    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public OAuth20Service oAuth20Service() {
        return new ServiceBuilder("877276455442-71mig1jr5kner68lq0nl8sl0aocsg0b5.apps.googleusercontent.com")
                .apiSecret("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")
                .defaultScope(new ScopeBuilder().withScopes("profile","email"))
                .callback("http://localhost:8080/auth")
                .build(GoogleApi20.instance());
    }

}
