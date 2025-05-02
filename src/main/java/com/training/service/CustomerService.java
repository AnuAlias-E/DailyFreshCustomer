package com.training.service;

import com.training.db.CustomerRepository;
import com.training.dto.OrderDTO;
import com.training.dto.request.CustomerOrderRequest;
import com.training.dto.request.CustomerRewardUpdateRequest;
import com.training.dto.response.CustomerOrderResponse;
import com.training.exception.CustomerNotFoundException;
import com.training.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repo;

	@Autowired
	private RestTemplate restTemplate;

	// ───────────────────────────────
	// CRUD Operations
	// ───────────────────────────────
	public Customer addNewCustomer(Customer customer) {
		return repo.save(customer);
	}

	public Customer updateCustomer(Customer customer) {
		return repo.save(customer);
	}

	public Customer searchCustomer(int id) throws CustomerNotFoundException {
		return repo.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
	}

	public boolean deleteCustomer(Customer customer) {
		try {
			repo.delete(customer);
			return true;
		} catch (EmptyResultDataAccessException ex) {
			return false;
		}
	}

	public List<Customer> getAllCustomers() {
		return repo.findAll();
	}

	// ───────────────────────────────
	// Custom Queries
	// ───────────────────────────────
	public List<Customer> getAllCustomersByLocation(String location) {
		return repo.findByLocation_Name(location).stream()
				.filter(c -> location.equalsIgnoreCase(c.getLocation().getName())).collect(Collectors.toList());
	}

	public List<Customer> getAllCustomersByCity(String city) {
		return repo.findByCity_Name(city).stream().filter(c -> city.equalsIgnoreCase(c.getCity().getName()))
				.collect(Collectors.toList());
	}

	// ───────────────────────────────
	// Order Integration
	// ───────────────────────────────
	public ResponseEntity<CustomerOrderResponse> placeOrderForCustomer(CustomerOrderRequest orderRequest) {
		String orderServiceUrl = "http://localhost:8083/orders/add"; // Replace with dynamic config or service discovery

		ResponseEntity<OrderDTO> orderResponse = restTemplate.postForEntity(orderServiceUrl, orderRequest,
				OrderDTO.class);

		CustomerOrderResponse response = new CustomerOrderResponse();
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setDescription("Order placed successfully! Order ID: " + orderResponse.getBody().getOrderId());
		response.setOrderDTO(orderResponse.getBody());

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// ───────────────────────────────
	// Reward Points Update from BillService
	// ───────────────────────────────
	public void updateRewardPoints(CustomerRewardUpdateRequest request) throws CustomerNotFoundException {
		int customerId = request.getCustomerBillDTO().getCustomerId();
		int rewardPoints = request.getCustomerBillDTO().getRewardPoints();

		Optional<Customer> customerOpt = repo.findById(customerId);
		if (customerOpt.isEmpty()) {
			throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
		}

		Customer customer = customerOpt.get();
		customer.setRewardPoints(rewardPoints);
		repo.save(customer);
	}
}
