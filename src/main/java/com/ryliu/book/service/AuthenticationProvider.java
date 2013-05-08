package com.ryliu.book.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.ryliu.book.domain.Person;
import com.ryliu.book.domain.PersonRole;

@Service("authenticationProvider")
public class AuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		String password = (String) authentication.getCredentials();
		if (!StringUtils.hasText(password)) {
			throw new BadCredentialsException("Please enter password!");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		try {
			String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());
			Person person = Person.findPeopleByLogin(username).getSingleResult();
			if (!encryptPassword.equals(person.getPassword())) {
				throw new BadCredentialsException("Invalid password!");
			}
			PersonRole role = person.getPersonRole();
			switch (role) {
			case SUPER_USER:
				authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_USER"));
				break;
			case ADMIN:
				authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				break;
			}
		} catch (EntityNotFoundException e) {
			throw new BadCredentialsException("Invalid username!");
		}
		return new User(username, password, authorities);
	}
}
