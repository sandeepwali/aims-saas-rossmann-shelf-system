package com.solumesl.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuth extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 7277374221426328373L;
	/**
	 * @sandeep
	 *
	 * 
	 */
	private final String apiKey;

    public ApiKeyAuth(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }
	
}
