package br.com.eicon.desafio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.eicon.desafio.model.AppUser;
import br.com.eicon.desafio.repository.UserRepository;

public class CustomAuthenticationManager implements AuthenticationManager {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException, BadCredentialsException {
		AppUser user = userRepository.findByEmail(auth.getPrincipal().toString());
        if (passwordEncoder.matches(auth.getCredentials().toString(), user.getPassword())) {
            return auth;
        }
		return auth;
    }
}