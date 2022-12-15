package com.eproject.library.service.impl;

import com.eproject.library.dto.CustomerDTO;
import com.eproject.library.model.Customer;
import com.eproject.library.repository.CustomerRepository;
import com.eproject.library.repository.RoleRepository;
import com.eproject.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPassword(customerDTO.getPassword());
        customer.setUsername(customerDTO.getUsername());
        customer.setRoles(Collections.singletonList(roleRepository.findByName("CUSTOMER")));

        Customer customerSave = customerRepository.save(customer);

        return mapperDTO(customerSave);
    }

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    private CustomerDTO mapperDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setUsername(customer.getUsername());
        return customerDTO;
    }
}
