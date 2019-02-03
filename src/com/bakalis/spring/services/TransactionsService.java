package com.bakalis.spring.services;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.Transaction;

import com.bakalis.models.*;
import com.bakalis.spring.configuration.HibernateUtil;

@Service
public class TransactionsService {

	@Autowired
	ValidationService validationService;
	@Autowired
	ErrorLoggingService errorService;
	
	
	//This Method Adds Products to the Database and Adds the addition to the transactions
	public void manageEntry(String productId, String productName, String category, String client, String quantity, String code){
	
		if(validationService.isNumeric(productId) && validationService.isNumeric(quantity) 
				&& !validationService.isEmptyString(productName)
				&& !validationService.isEmptyString(category)      //Checking that supposed Integer parameters are 
				&& !validationService.isEmptyString(client)		   //indeed integers and that other Strings are
				&& !validationService.isEmptyString(code)){		   //not empty Strings
			
			
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Category cat =  session.get(Category.class , Integer.parseInt(category));
			Client cl = session.get(Client.class, Integer.parseInt(client));
			Product prod = session.get(Product.class, Integer.parseInt(productId));
			int quant = Integer.parseInt(quantity);
			
			if(prod!=null){ // We already got some of this product in our warehouse
				prod.setQuantity(prod.getQuantity() + quant);
				session.update(prod);
				addTransactionToDatabase(prod, cl, quant, true);
			}else{ // We create the first entry for this new Product
				Product product = new Product();
				product.setId(Integer.parseInt(productId));
				product.setCategory(cat);
				product.setProductName(productName);
				product.setQuantity(Integer.parseInt(quantity));
				product.setStorageCode(code);
				session.save(product);
				addTransactionToDatabase(product, cl, quant, true);
			}
			
			tr.commit();
			session.close();
		}else{
			errorService.setError("Wrong Input");
		}
		
		
	}
	
	//This Method Retrieves Products from the Database and Adds the retrieval to the transactions
	public void manageRetrieval(String productId, String productName, String category, String client, String quantity, String code){
			
		if(validationService.isNumeric(productId) && validationService.isNumeric(quantity) 
				&& !validationService.isEmptyString(productName)
				&& !validationService.isEmptyString(category)      //Checking that supposed Integer parameters are 
				&& !validationService.isEmptyString(client)		   //indeed integers and that other Strings are
				&& !validationService.isEmptyString(code)){		   //not empty Strings
			
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			
			Client cl = session.get(Client.class, Integer.parseInt(client));
			Product prod = session.get(Product.class, Integer.parseInt(productId));
			if(prod!=null){ // We already got some of this product in our warehouse
				int quant = Integer.parseInt(quantity);
				if(prod.getQuantity() >= quant){ //If we got the desired quantity to retrieve available in the warehouse
					prod.setQuantity(prod.getQuantity() - quant);
					session.update(prod);
					addTransactionToDatabase(prod, cl, quant, false);
				}else{ // We don't have the required quantity despite having some of this product
					errorService.setError("We don't have enough of this product in stock");
				}
				tr.commit();
				session.close();
				
			}else{ // We don't have any of this product in our warehouse
				errorService.setError("We don't have any of this product in stock");
				tr.commit();
				session.close();
			}
		}else{
			errorService.setError("Wrong Input");
		}
			
	}
	
	// Self Explanatory. Adds the Transaction to the database. 
	public void addTransactionToDatabase(Product product, Client client, int quantityChange, boolean isAnEntry){
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.openSession();
		Transaction tr = session.beginTransaction();
		
		
		SingleTransaction singleTransaction = new SingleTransaction();
		singleTransaction.setClient(client);
		singleTransaction.setProduct(product);
		if(isAnEntry){
			singleTransaction.setQuantityChange(quantityChange);
		}else{
			singleTransaction.setQuantityChange(-quantityChange);
		}
		
		session.save(singleTransaction);
		
		tr.commit();
		session.close();
		
	}

	// Adds a client to the database 
	public void addClient(String clientId, String clientName) {
		if(validationService.isNumeric(clientId) && !validationService.isEmptyString(clientName)){
		
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Client client = new Client();
			client.setClientName(clientName);
			client.setId(Integer.parseInt(clientId));
			session.save(client);
			tr.commit();
			session.close();
		}else{
			errorService.setError("Wrong Input");
		}
	}

	// Deletes the Client with the given Id from the Database
	public void deleteClient(String deleteId) {
		if(validationService.isNumeric(deleteId)){
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Client client = session.get(Client.class, Integer.parseInt(deleteId));
			if(client!=null){
				session.delete(client);
			}else{
				errorService.setError("There is not a client with this Id");
			}
			tr.commit();
			session.close();
		}else{
			errorService.setError("Wrong Input");
		}
	}
	
	
	//Updates an existing Client with the Given Data
	public void editClient(String editedId, String editedName){
		if(validationService.isNumeric(editedId) && !validationService.isEmptyString(editedName)){
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Client client = session.get(Client.class, Integer.parseInt(editedId));
			if(client!=null){
				client.setClientName(editedName);
				session.update(client);
				
			}else{
				errorService.setError("There is no Client with this Id");
			}
			tr.commit();
			session.close();
		}else{
			errorService.setError("Wrong Input");
		}
		
	}
	
	
	//Adds a new Category
	public void addCategory(String categoryId, String categoryName) {
		if(validationService.isNumeric(categoryId) && !validationService.isEmptyString(categoryName)){
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Category category = new Category();
			category.setCategoryName(categoryName);
			category.setId(Integer.parseInt(categoryId));
			session.save(category);
			tr.commit();
			session.close();
		}else{
			System.out.println(validationService.isNumeric(categoryId));
			System.out.println(!validationService.isEmptyString(categoryName));
			System.out.println(categoryId+" "+categoryName);
			errorService.setError("Wrong Input");
		}
	}
	
	
	//Deletes the Category with the given Id
	public void deleteCategory(String deleteId) {
		if(validationService.isNumeric(deleteId)){
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Category category = session.get(Category.class, Integer.parseInt(deleteId));
			if(category!=null){
				session.delete(category);
			}else{
				errorService.setError("There is not a category with this Id");
			}
			tr.commit();
			session.close();
		}else{
			errorService.setError("Wrong Input");
		}
	}
	
	
	//Edits an Existing Category with the Given Data
	public void editCategory(String editedId, String editedName){
		if(validationService.isNumeric(editedId) && !validationService.isEmptyString(editedName)){
			SessionFactory sf = HibernateUtil.getSessionFactory();
			Session session = sf.openSession();
			Transaction tr = session.beginTransaction();
			Category category = session.get(Category.class, Integer.parseInt(editedId));
			if(category!=null){
				category.setCategoryName(editedName);
				session.update(category);
				
			}else{
				errorService.setError("There is not a category with this Id");
			}
			tr.commit();
			session.close();
		}else{
			errorService.setError("Wrong Input");
		}
	}
	
}
