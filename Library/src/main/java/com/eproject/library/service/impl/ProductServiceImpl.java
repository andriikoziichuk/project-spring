package com.eproject.library.service.impl;

import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Product;
import com.eproject.library.repository.ProductRepository;
import com.eproject.library.service.ProductService;
import com.eproject.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUpload imageUpload;

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDTO productDTO) {
        try {
            Product product = new Product();
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                if (imageUpload.uploadImage(imageProduct)) {
                    System.out.println("Upload successfully");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }

            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setCategory(productDTO.getCategory());
            product.setCostPrice(productDTO.getCostPrice());
            product.setCurrentQuantity(productDTO.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);

            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDTO productDTO) {
        try {
            Product product = productRepository.getById(productDTO.getId());
            if (imageProduct == null)
                product.setImage(product.getImage());
            else {
                if (!imageUpload.checkExisted(imageProduct)) {
                    imageUpload.uploadImage(imageProduct);
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setSalePrice(productDTO.getSalePrice());
            product.setCostPrice(productDTO.getCostPrice());
            product.setCurrentQuantity(productDTO.getCurrentQuantity());
            product.setCategory(productDTO.getCategory());

            return productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getReferenceById(id);
        product.set_activated(false);
        product.set_deleted(true);
        productRepository.save(product);
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.getReferenceById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    @Override
    public ProductDTO getById(Long id) {
        Product product = productRepository.getById(id);

        return getProductDTO(product);
    }

    private static ProductDTO getProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCurrentQuantity(product.getCurrentQuantity());
        productDTO.setCategory(product.getCategory());
        productDTO.setSalePrice(product.getSalePrice());
        productDTO.setCostPrice(product.getCostPrice());
        productDTO.setImage(product.getImage());
        productDTO.setDeleted(product.is_deleted());
        productDTO.setActivated(product.is_activated());
        return productDTO;
    }

    private List<ProductDTO> transfer(List<Product> products){
        List<ProductDTO> productDtoList = new ArrayList<>();
        for(Product product : products){
            ProductDTO productDto = getProductDTO(product);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public Page<ProductDTO> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDTO> products = transfer(productRepository.findAll());
        Page<ProductDTO> productPages = toPage(products, pageable);
        return productPages;
    }

    @Override
    public Page<ProductDTO> searchProducts(int pageNO, String keyword) {
        Pageable pageable = PageRequest.of(pageNO,5);
//        List<ProductDTO> productDTOList = transfer(productRepository.searchProductsList(keyword));
        List<ProductDTO> productDTOList = transfer(productRepository.searchByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword));

        Page<ProductDTO> products = toPage(productDTOList, pageable);

        return products;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.getReferenceById(id);
    }

    @Override
    public List<Product> listViewProducts() {
        return productRepository.listViewProducts();
    }

    @Override
    public List<Product> getRelatedProducts(Long categoryId) {
        return productRepository.getRelatedProducts(categoryId);
    }

    private Page toPage(List<ProductDTO> list , Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List<ProductDTO> subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<Product> getProductsInCategory(Long categoryId) {
        return productRepository.getProductsInCategory(categoryId);
    }

    @Override
    public List<Product> filterHighPrice() {
        return productRepository.filterHighPrice();
    }

    @Override
    public List<Product> filterLowPrice() {
        return productRepository.filterLowPrice();
    }

}
