package com.example.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

import javax.sql.DataSource

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value('${spring.queries.users-query}')
    private String usersQuery;

    @Value('${spring.queries.roles-query}')
    private String rolesQuery;


    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/slack").permitAll()
                .antMatchers("/registration").permitAll()
                .regexMatchers("/calendar/\\d{4}-\\d{2}-\\d{2}/\\?team=lunch").permitAll()
                .antMatchers("/home/**").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/slack").failureUrl("/slack?error=true")
                .defaultSuccessUrl("/calendar/" + new Date().format("yyyy-MM-dd"))
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
/*
	private Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter slackFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/calendar");
		OAuth2RestTemplate slackTemplate = new OAuth2RestTemplate(slack(), oauth2ClientContext);
		slackFilter.setRestTemplate(slackTemplate);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(slackResource().getUserInfoUri(),
				slack().getClientId());
		tokenServices.setRestTemplate(slackTemplate);
		slackFilter.setTokenServices(
				new UserInfoTokenServices(slackResource().getUserInfoUri(), slack().getClientId()));
		return slackFilter;
	}

	@Bean
	@ConfigurationProperties("slack.client")
	public AuthorizationCodeResourceDetails slack() {
		return new AuthorizationCodeResourceDetails();;
	}

	@Bean
	@Primary
	@ConfigurationProperties("slack.resource")
	public ResourceServerProperties slackResource() {
		return new ResourceServerProperties();
	}
*/
}