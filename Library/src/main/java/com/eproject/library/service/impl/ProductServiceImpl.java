package com.eproject.library.service.impl;

import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Product;
import com.eproject.library.repository.ProductRepository;
import com.eproject.library.service.ProductService;
import com.eproject.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
        return transfer(products);
    }

    @Override
    public List<ProductDTO> findAll(String[] sort) {

        return transfer(productRepository.findAll(Sort.by(getOrders(sort))));
    }

    @Override
    public Page<ProductDTO> findAll(String[] sort, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDTO> products = transfer(productRepository.findAll(Sort.by(getOrders(sort))));
        return toPage(products, pageable);
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
            Product product = productRepository.getReferenceById(productDTO.getId());
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
        Product product = productRepository.getReferenceById(id);

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
        return toPage(products, pageable);
    }

    @Override
    public Page<ProductDTO> searchProducts(int pageNO, String keyword) {
        Pageable pageable = PageRequest.of(pageNO,5);

        List<ProductDTO> productDTOList = transfer(productRepository.searchByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword));

        return toPage(productDTOList, pageable);
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword, String[] sort) {

        return transfer(productRepository.searchByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, Sort.by(getOrders(sort))));
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

    public static <T> Page<T> toPage(List<T> list , Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List<T> subList = list.subList(startIndex, endIndex);
        return new PageImpl<>(subList, pageable, list.size());
    }

    @Override
    public List<Product> getProductsInCategory(Long categoryId, String[] sort) {
        return productRepository.getProductsInCategory(categoryId, Sort.by(getOrders(sort)));
    }

    private List<Sort.Order> getOrders(String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            // will sort more than 2 columns
            for (String sortOrder : sort) {
                // sortOrder="column, direction"
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[column, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }
        return orders;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }
}
