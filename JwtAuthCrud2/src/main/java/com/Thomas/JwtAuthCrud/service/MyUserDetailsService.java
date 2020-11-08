package com.Thomas.JwtAuthCrud.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Thomas.JwtAuthCrud.model.AppUser;




@Service
public class MyUserDetailsService  implements UserDetailsService {

	@Autowired
	UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		AppUser user = userService.findByUsername(login);
		
		
		List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) userService.dbToString(user.getId());
	
		return new User(user.getLogin(), user.getPassword(), grantedAuthorities);
//		return new User("foo", "foo", new ArrayList<>());
	}
	

}
