package com.training.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerOrderDTO {
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate orderDate;
	private String deliveryLocation;
	
	private List<CustomerOrderItemDTO> customerOrderItemDTOs;

	

	private int customerId;
	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(String deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}




	public List<CustomerOrderItemDTO> getCustomerOrderItemDTOs() {
		return customerOrderItemDTOs;
	}

	public void setCustomerOrderItemDTOs(List<CustomerOrderItemDTO> customerOrderItemDTOs) {
		this.customerOrderItemDTOs = customerOrderItemDTOs;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	
	

}
