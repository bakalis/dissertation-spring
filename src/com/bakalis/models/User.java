package com.bakalis.models;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
public class User implements UserDetails{
	
	@Id
	protected String username;
	protected String password;
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//We just want to authenticate users, we don't Classify them by Roles
		
		return null;
	}

	
	@Override
	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}


	@Override
	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
