package overtime_control.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
		return new MvcRequestMatcher.Builder(introspector);
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc)throws Exception {
				
		http.formLogin(login -> login
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureUrl("/login?error")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/menu", true)
				.permitAll()
		);
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers(mvc.pattern("/signup")).permitAll()
				//ここに権限によって入れるページを指定
				.requestMatchers(mvc.pattern("/confirm/**")).hasAuthority("ROLE_SUPERVISOR")
				.requestMatchers(
					    mvc.pattern("/approval"), 
					    mvc.pattern("/approval/**"),
					    mvc.pattern("/success/**"),
					    mvc.pattern("/reject/**")
				).hasAnyAuthority("ROLE_MANAGER", "ROLE_DIRECTOR")
				.requestMatchers(mvc.pattern("/admin/**")).hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()
		);
		http.exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler()));
		
        return http.build();
	};
		
	@Bean
	AccessDeniedHandler customAccessDeniedHandler() {
	    return (request, response, accessDeniedException) -> {
	        logger.warn("不正アクセス検知: [{}] {} IP: {}", 
	            request.getMethod(),
	            request.getRequestURI(),
	            request.getRemoteAddr()
	        );
	        request.setAttribute("message", "ページが見つかりません。");
	        request.getRequestDispatcher("/error/error").forward(request, response);
	    };

		/*.requiresChannel(channel -> channel
                .anyRequest().requiresSecure()
        );*/
	}
}
