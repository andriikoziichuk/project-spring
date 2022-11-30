package com.eproject.library.service;

import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    Product save(MultipartFile imageProduct, ProductDTO productDTO);
    Product update(MultipartFile imageProduct, ProductDTO productDTO);
    void deleteById(Long id);
    void enableById(Long id);
    ProductDTO getById(Long id);
}
