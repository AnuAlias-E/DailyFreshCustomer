package com.training.dto.request;

import com.training.model.Customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateRequest {
	Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
