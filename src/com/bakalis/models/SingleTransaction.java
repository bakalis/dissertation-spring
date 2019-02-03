package com.bakalis.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="Transactions")
public class SingleTransaction {

	@Id
	@GeneratedValue
	protected int id;
	@ManyToOne
	@JoinColumn(name="productId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	protected Product product;
	@ManyToOne
	@JoinColumn(name="clientId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	protected Client client;
	
	@CreationTimestamp
	@Column(updatable =false) 
    protected Date created;
	
	protected int quantityChange;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getQuantityChange() {
		return quantityChange;
	}

	public void setQuantityChange(int quantityChange) {
		this.quantityChange = quantityChange;
	}


	
}
