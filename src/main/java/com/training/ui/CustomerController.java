package com.training.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.dto.request.CustomerAddRequest;
import com.training.dto.request.CustomerOrderRequest;
import com.training.dto.request.CustomerRewardUpdateRequest;
import com.training.dto.request.CustomerUpdateRequest;
import com.training.dto.response.CustomerAddResponse;
import com.training.dto.response.CustomerOrderResponse;
import com.training.dto.response.CustomerSearchResponse;
import com.training.dto.response.CustomerShowAllResponse;
import com.training.dto.response.CustomerUpdateResponse;
import com.training.exception.CustomerNotFoundException;
import com.training.model.Customer;
import com.training.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	CustomerService service;

	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerAddResponse> addNewCustomer(@Valid @RequestBody CustomerAddRequest request) {
		CustomerAddResponse response = new CustomerAddResponse();
		Customer customer = service.addNewCustomer(request.getCustomer());
		response.setStatusCode(201);
		response.setDescription("Customer Added Successfully");
		response.setCustomer(customer);

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@PutMapping(value = "/modify")
	public ResponseEntity<CustomerUpdateResponse> updateCustomer(@RequestBody CustomerUpdateRequest request)
			throws CustomerNotFoundException {
		CustomerUpdateResponse response = new CustomerUpdateResponse();

		Customer customer = service.searchCustomer(request.getCustomer().getCustomerId());
		if (customer != null) {

			Customer customerResult = service.updateCustomer(request.getCustomer());
			response.setStatusCode(200);
			response.setDescription("Customer Modified Successfully");
			response.setCustomer(customerResult);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setStatusCode(404);
			response.setDescription("Customer Not Found");
			response.setCustomer(customer);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "/find/{cid}")
	public ResponseEntity<CustomerSearchResponse> searchCustomer(@PathVariable(name = "cid") int customerId)
			throws Exception {
		Customer customer = service.searchCustomer(customerId);
		CustomerSearchResponse response = new CustomerSearchResponse();
		if (customer != null) {
			response.setStatusCode(200);
			response.setDescription("Customer fetched Successfully");
			response.setCustomer(customer);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			Exception e = new CustomerNotFoundException("Customer Not Found with id:" + customerId);
			throw e;
		}
	}

	@GetMapping(value = "/showAll", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CustomerShowAllResponse> getAllCustomers() throws CustomerNotFoundException {
		List<Customer> customers = service.getAllCustomers();
		CustomerShowAllResponse response = new CustomerShowAllResponse();
		response.setStatusCode(200);
		response.setDescription("All customers Fetched");
		response.setCustomers(customers);

		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/findByLoc/{location}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CustomerShowAllResponse> getAllCustomersByLocation(@PathVariable (name="location") String loc)  {
		List<Customer> customers = service.getAllCustomersByLocation(loc);
		CustomerShowAllResponse response = new CustomerShowAllResponse();
		response.setStatusCode(200);
		response.setDescription("All customers Fetched");
		response.setCustomers(customers);

		return ResponseEntity.ok(response);
	}

	
	@GetMapping(value = "/findByCity/{cityName}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CustomerShowAllResponse> getAllCustomersByCity(@PathVariable (name="cityName") String city)  {
		List<Customer> customers = service.getAllCustomersByCity(city);
		CustomerShowAllResponse response = new CustomerShowAllResponse();
		response.setStatusCode(200);
		response.setDescription("All customers Fetched");
		response.setCustomers(customers);

		return ResponseEntity.ok(response);
	}

	@PostMapping(value="/placeOrder",produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CustomerOrderResponse> placeOrder(@RequestBody CustomerOrderRequest orderRequest) {
		ResponseEntity<CustomerOrderResponse> response = service.placeOrderForCustomer(orderRequest);
	    return response;
	}
	

	@PutMapping("/updateRewardPoints")
	public ResponseEntity<String> updateRewardPoints(@RequestBody CustomerRewardUpdateRequest request) throws CustomerNotFoundException{
		try {
			service.updateRewardPoints(request);
			return ResponseEntity.ok("RewardPoints updated successfully");
		} catch (CustomerNotFoundException e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
	}
}
