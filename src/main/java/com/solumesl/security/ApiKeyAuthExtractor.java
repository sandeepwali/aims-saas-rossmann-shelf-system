package com.solumesl.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

@Component
public class ApiKeyAuthExtractor {
	/**
	 * @sandeep
	 *
	 * 
	 */
    @Value("${application.security.api-key}")
    private String apiKey;

    public Optional<Authentication> extract(HttpServletRequest request) {
        String providedKey = request.getHeader("api-key");
        if (providedKey == null || !providedKey.trim().equals(apiKey.trim()))
            return Optional.empty();

        return Optional.of(new ApiKeyAuth(providedKey, AuthorityUtils.NO_AUTHORITIES));
    }
	
}
