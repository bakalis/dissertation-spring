package com.bakalis.spring.services;

import java.util.ArrayList;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bakalis.models.Category;
import com.bakalis.models.Client;
import com.bakalis.models.ContentsTuple;
import com.bakalis.models.Product;
import com.bakalis.models.SingleTransaction;
import com.bakalis.spring.configuration.HibernateUtil;


@Service
public class ContentsService {
	
	@Autowired
	ValidationService validationService;
	@Autowired
	ErrorLoggingService errorService;
	
	//Get all the contents in the database
	@SuppressWarnings({ "unchecked", "deprecation" })
	public ArrayList<ContentsTuple> getAllContents(){
		ArrayList<ContentsTuple> contents = new ArrayList<ContentsTuple>();
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Criteria crit = session.createCriteria(Product.class);
		ArrayList<Product> products = (ArrayList<Product>) crit.list();
		for(Product product : products){
			ContentsTuple tuple = new ContentsTuple();
			tuple.setProduct(product);
			tuple.setCategory(product.getCategory().getCategoryName());
			contents.add(tuple);
		}
		session.close();
		return contents;	
	}
	
	
	//Get the contents for the table by the search form values
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ArrayList<ContentsTuple> getSearchedContents(String query, String filter){
		ArrayList<ContentsTuple> contents = new ArrayList<ContentsTuple>();
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		switch(filter){
		
			case "id": //Select Option By Product Id
				try{
					Product product = session.get(Product.class, Integer.parseInt(query));
					if(product!=null){
						ContentsTuple tuple = new ContentsTuple();
						tuple.setProduct(product);
						tuple.setCategory(product.getCategory().getCategoryName());
						contents.add(tuple);	
					}
				}catch(Exception ex){
									
				}
				break;
			case "name": //Select Option By Product Name
				Criteria crit = session.createCriteria(Product.class);
				crit.add(Restrictions.like("productName", "%"+query+"%"));
				ArrayList<Product> products = (ArrayList<Product>) crit.list();
				if(products!=null){
					for(Product prod : products){
						ContentsTuple tuple = new ContentsTuple();
						tuple.setCategory(prod.getCategory().getCategoryName());
						tuple.setProduct(prod);
						contents.add(tuple);
					}
				}
				break;
			case "category": //Select Option By Category
				Criteria crit2 = session.createCriteria(Category.class);
				crit2.add(Restrictions.like("categoryName", "%"+query+"%"));
				ArrayList<Category> cats = (ArrayList<Category>) crit2.list();
				if(cats!=null){
					for(Category cat : cats){
						crit = session.createCriteria(Product.class);
						crit.add(Restrictions.eq("category.id", cat.getId()));
						ArrayList<Product> prods = (ArrayList<Product>) crit.list();
						if(prods!=null){
							for(Product prod : prods){
								ContentsTuple tuple = new ContentsTuple();
								tuple.setCategory(cat.getCategoryName());
								tuple.setProduct(prod);
								contents.add(tuple);
							}
						}
					}
				}
				break;
			case "code": //Select Option By Storage Code
				Criteria crit3 = session.createCriteria(Product.class);
				crit3.add(Restrictions.eq("storageCode", query));
				ArrayList<Product> products1 = (ArrayList<Product>) crit3.list();
				if(products1!=null){
					for(Product prod : products1){
						ContentsTuple tuple = new ContentsTuple();
						tuple.setCategory(prod.getCategory().getCategoryName());
						tuple.setProduct(prod);
						contents.add(tuple);
					}
				}
				break;
			default:
				errorService.setError("Wrong Selection Filter");
				break;
		}
		
		
		session.close();
		return contents;
	}
	
	
	//Getting the List of Transactions
	@SuppressWarnings({ "unchecked", "deprecation" })
	public ArrayList<SingleTransaction> getPastTransactions(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Criteria crit = session.createCriteria(SingleTransaction.class);
		ArrayList<SingleTransaction> transactions = (ArrayList<SingleTransaction>) crit.list();
		return transactions;
	}
	
	
	//Getting the List of all Clients

	@SuppressWarnings({ "deprecation", "unchecked" })
	public ArrayList<Client> getClients(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Criteria crit = session.createCriteria(Client.class);
		ArrayList<Client> clients = (ArrayList<Client>) crit.list();
		session.close();
		return clients;
	}
	
	// Getting a list of clients Based on the search in clients.html
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ArrayList<Client> getSearchedClients(String searchBar){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Criteria crit = session.createCriteria(Client.class);
		if(validationService.isNumeric(searchBar)){ // User searches for clients by Client id;
			crit.add(Restrictions.eq("id", Integer.parseInt(searchBar)));
			ArrayList<Client> clients = (ArrayList<Client>) crit.list();
			return clients;
		}else{ // User searches for clients by Client Name
			crit.add(Restrictions.like("clientName", "%"+searchBar+"%"));
			ArrayList<Client> clients = (ArrayList<Client>) crit.list();
			return clients;
		}
		
	}

	//Given an Id we return the client
	public Client getEditedClient(String editId) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		if(validationService.isNumeric(editId)){
			Client client = session.get(Client.class, Integer.parseInt(editId));
			if(client==null){
				errorService.setError("There is no Client with that Id");
			}
			session.close();
			return client;
		} else{
			errorService.setError("You need to pass an Integer as an Id for the Client to be edited.");
			session.close();
			return null;
		}
		
		
	}
	
	
	//Getting the List of all Categories

	@SuppressWarnings({ "deprecation", "unchecked" })
	public ArrayList<Category> getCategories(){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Criteria crit = session.createCriteria(Category.class);
		ArrayList<Category> categories = (ArrayList<Category>) crit.list();
		session.close();
		return categories;
	}
	
	
	// Getting a list of clients Based on the search in clients.html
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ArrayList<Category> getSearchedCategories(String searchBar){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Criteria crit = session.createCriteria(Category.class);
		if(validationService.isNumeric(searchBar)){ // User searches for categories by Category id;
			crit.add(Restrictions.eq("id", Integer.parseInt(searchBar)));
			ArrayList<Category> categories = (ArrayList<Category>) crit.list();
			return categories;
		}else{ // User searches for clients by Client Name
			crit.add(Restrictions.like("categoryName", "%"+searchBar+"%"));
			ArrayList<Category> categories = (ArrayList<Category>) crit.list();
			return categories;
		}
		
	}
	
	
	//Given an Id we return the Category
	public Category getEditedCategory(String editedId) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		if(validationService.isNumeric(editedId)){
			Category category = session.get(Category.class, Integer.parseInt(editedId));
			if(category==null){
				errorService.setError("There is no Category with that Id");
			}
			session.close();
			return category;
		} else{
			errorService.setError("You need to pass an Integer as an Id for the Category to be edited.");
			session.close();
			return null;
		}
		
	}
	
	
	
	
}
