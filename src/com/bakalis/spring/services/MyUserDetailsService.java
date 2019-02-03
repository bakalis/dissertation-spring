package com.bakalis.spring.services;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.bakalis.models.User;
import com.bakalis.spring.configuration.HibernateUtil;

@Service
public class MyUserDetailsService implements UserDetailsService {

	
	//This Service searches for a User with the Given username in the database
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		System.out.println(username);
		Criteria crit = session.createCriteria(User.class);
		crit.add(Restrictions.eq("username", username));
		ArrayList<User> user = (ArrayList<User>) crit.list();
		session.close();
		if(!user.isEmpty()){
			return user.get(0);
		}else{
			throw new UsernameNotFoundException(username);
		}
	}

}
