package com.eproject.library.service;

import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    Product save(MultipartFile imageProduct, ProductDTO productDTO);
    Product update(MultipartFile imageProduct, ProductDTO productDTO);
    void deleteById(Long id);
    void enableById(Long id);
    ProductDTO getById(Long id);
    Page<ProductDTO> pageProducts(int pageNo);
    Page<ProductDTO> searchProducts(int pageNo, String keyword);
    Product getProductById(Long id);
    List<Product> listViewProducts();
    List<Product> getRelatedProducts(Long categoryId);
    List<Product> getAllProducts();
    List<Product> getProductsInCategory(Long categoryId);
    List<Product> filterHighPrice();
    List<Product> filterLowPrice();
}
