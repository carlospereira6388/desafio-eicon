package br.com.eicon.desafio.security;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.eicon.desafio.exception.CustomException;
import br.com.eicon.desafio.model.AppUser;
import br.com.eicon.desafio.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		final AppUser user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("E-mail '" + email + "' não encontrado.");
		}

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user
				.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList()));
	}

	public AppUser getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public AppUser getLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String email = ((UserDetails) principal).getUsername();
			return getUserByEmail(email);
		} else {
			throw new CustomException("Usuário não autenticado", HttpStatus.UNAUTHORIZED);
		}
	}
}