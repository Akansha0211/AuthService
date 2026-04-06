package org.example.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/*
* Here we define at which endpoints we need authentication
* */
@Configuration
@EnableMethodSecurity
@Data
public class SecurityConfig {
}
