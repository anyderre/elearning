package com.sorbSoft.CabAcademie.config;

/**
 * Created by Dany on 06/09/2017.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.sorbSoft.CabAcademie.config.JwtTokenUtil.PRE_SIGN_UP_URL;
import static com.sorbSoft.CabAcademie.config.JwtTokenUtil.SIGN_UP_URL;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private MyCorsFilter myCorsFilter;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${swagger.username}")
    private String SWAGGER_USER_NAME;

    @Value("${swagger.password}")
    private String SWAGGER_PASSWORD;

    private static final String[] SWAGGER_URLS = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    private static final String[] CATEGORY_URLS = {

            "/api/category/all",
            "/api/category/info",
            "/api/category/all/filtered"

    };

    private static final String[] SUB_CATEGORIES_URLS = {

            "/api/subCategory/all",
            "/api/subCategory/info",
            "/api/subCategory/all/filtered"

    };

    private static final String[] SECTIONS_URLS = {

            "/api/section/all",
            "/api/section/info",
            "/api/section/all/filtered"

    };

    private static final String[] SUB_SECTIONS_URLS = {

            "/api/subSection/all",
            "/api/subSection/info",
            "/api/subSection/all/filtered"

    };

    @Configuration
    @Order(2)
    public class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {


        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            // configure AuthenticationManager so that it knows from where to load
            // user for matching credentials
            // Use BCryptPasswordEncoder
            auth.userDetailsService(jwtUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }


        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

            return source;
        }


        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            // We don't need CSRF for this example
            httpSecurity
                    .addFilterBefore(myCorsFilter, ChannelProcessingFilter.class)
                    .csrf().disable()
//                .cors().and()
                    // dont authenticate this particular request
                    .authorizeRequests()
                    .antMatchers(SWAGGER_URLS).permitAll()
                    .antMatchers(HttpMethod.GET,"/api/appointment/approve/**").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/appointment/decline/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/languages/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/course/lastCreated/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/course/bestRated/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/course/featured/**").permitAll()

                    //TODO: potential security issue here. Need to split api on registerUpload and loggedIn upload
                    //TODO: registerUpload should be limited by file size like up to 10 Mb
                    .antMatchers(HttpMethod.POST, "/api/file/**").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/api/file/**").permitAll()

                    .antMatchers(HttpMethod.GET, CATEGORY_URLS).permitAll()
                    .antMatchers(HttpMethod.GET, SUB_CATEGORIES_URLS).permitAll()
                    .antMatchers(HttpMethod.GET, SECTIONS_URLS).permitAll()
                    .antMatchers(HttpMethod.GET, SUB_SECTIONS_URLS).permitAll()
                    .antMatchers(HttpMethod.GET, "/api/subSection/allBySection/**").permitAll()
                    .antMatchers("/authenticate").permitAll()
                    .antMatchers("/jitsi-auth").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/user/saveStudent").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/user/saveOrganization").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/user/saveSchool").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/user/saveTeacher").permitAll()
                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                    .antMatchers(HttpMethod.GET, PRE_SIGN_UP_URL).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    // all other requests need to be authenticated
                    // make sure we use stateless session; session won't be used to
                    // store user's state.
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // Add a filter to validate the tokens with every request
            httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Configuration
    @Order(1)
    public class SwaggerSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/swagger**").
                    authorizeRequests().
                    antMatchers("/swagger**").authenticated().
                    and().httpBasic().and().csrf().disable();

            http.antMatcher("/v2/api-docs").
                    authorizeRequests().
                    antMatchers("/v2/api-docs").authenticated().
                    and().httpBasic().and().csrf().disable();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.inMemoryAuthentication()
                    .withUser(SWAGGER_USER_NAME)
                    .password(SWAGGER_PASSWORD)
                    .roles("USER");
        }
    }
}