package com.eproject.library.service.impl;

import com.eproject.library.alghorithms.BinarySearch;
import com.eproject.library.dto.CustomerDTO;
import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Customer;
import com.eproject.library.model.Product;
import com.eproject.library.repository.CustomerRepository;
import com.eproject.library.repository.RoleRepository;
import com.eproject.library.service.CustomerService;
import com.eproject.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ImageUpload imageUpload;
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

    @Override
    public Customer saveInfor(Customer customer) {
        Customer customer1 = customerRepository.findByUsername(customer.getUsername());
        customer1.setAddress(customer.getAddress());
        customer1.setCity(customer.getCity());
        customer1.setCountry(customer.getCountry());
        customer1.setPhoneNumber(customer.getPhoneNumber());

        return customerRepository.save(customer1);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Page<Customer> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        return ProductServiceImpl.toPage(customerRepository.findAll(), pageable);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.getReferenceById(id);
    }

    @Override
    public Customer update(MultipartFile imageCustomer, Customer customer) {
        try {
            Customer customer1 = customerRepository.getReferenceById(customer.getId());
            if (imageCustomer == null)
                customer.setImage(customer.getImage());
            else {
                if (!imageUpload.checkExisted(imageCustomer)) {
                    imageUpload.uploadImage(imageCustomer);
                }
                customer1.setImage(Base64.getEncoder().encodeToString(imageCustomer.getBytes()));
            }
            customer1.setFirstName(customer.getFirstName());
            customer1.setLastName(customer.getLastName());
            customer1.setRoles(customer.getRoles());
            customer1.setPassword(customer.getPassword());

            return customerRepository.save(customer1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
