package com.eproject.library.service;

import com.eproject.library.dto.CustomerDTO;
import com.eproject.library.model.Customer;

public interface CustomerService {

    CustomerDTO save(CustomerDTO customerDTO);

    Customer findByUsername(String username);
}
