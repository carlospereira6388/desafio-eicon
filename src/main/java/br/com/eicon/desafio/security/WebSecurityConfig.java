package br.com.eicon.desafio.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtTokenProvider jwtTokenProvider;
		
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	        .csrf().disable()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
	        .authorizeRequests()
	            .antMatchers("/auth/login", "/auth/register", "/h2-console/**/**").permitAll()
	            .antMatchers("/users/**").hasRole("ADMIN")
	            .antMatchers("/orders/**").hasAnyRole("ADMIN", "USER")
	            .anyRequest().authenticated()
	        .and()
	        .exceptionHandling()
	            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) 
	            .accessDeniedHandler((request, response, accessDeniedException) -> 
	                response.sendError(HttpStatus.FORBIDDEN.value(), "Acesso negado"))
	        .and()
	        .apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs")
				.antMatchers("/swagger-resources/**")
				.antMatchers("/swagger-ui.html")
				.antMatchers("/configuration/**")
				.antMatchers("/webjars/**")
				.antMatchers("/public").and().ignoring().antMatchers("/h2-console/**/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return new CustomAuthenticationManager();
	}
}
