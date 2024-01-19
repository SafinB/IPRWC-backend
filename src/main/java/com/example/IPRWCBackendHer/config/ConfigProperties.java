package com.example.IPRWCBackendHer.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Component
@Getter
@Setter
@Validated
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {
    @NotBlank
    @Size(min = 20)
    private String jwtSecret;
}