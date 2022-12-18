package com.eproject.library.service;

import com.eproject.library.dto.CustomerDTO;
import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    CustomerDTO save(CustomerDTO customerDTO);

    Customer findByUsername(String username);

    Customer saveInfor(Customer customer);

    List<Customer> findAll();

    Page<Customer> pageProducts(int pageNo);

    Customer getById(Long id);

    Customer update(MultipartFile imageCustomer, Customer customer);
}
