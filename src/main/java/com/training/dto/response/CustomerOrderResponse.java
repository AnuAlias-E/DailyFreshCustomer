package com.training.dto.response;

import com.training.dto.OrderDTO;

public class CustomerOrderResponse {
	int statusCode;
	String description;
	OrderDTO orderDTO;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public OrderDTO getOrder() {
		return orderDTO;
	}
	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}
}
