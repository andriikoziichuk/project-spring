package com.eproject.customer.controller;

import com.eproject.library.dto.CategoryDTO;
import com.eproject.library.model.Category;
import com.eproject.library.model.Product;
import com.eproject.library.service.CategoryService;
import com.eproject.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model){
        List<CategoryDTO> categoryDtoList = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getAllProducts();
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("viewProducts", listViewProducts);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/find-product/{id}")
    public String findProductById(@PathVariable("id") Long id, Model model){
        Product product = productService.getProductById(id);
        Long categoryId = product.getCategory().getId();
        List<Product>  products = productService.getRelatedProducts(categoryId);
        model.addAttribute("product", product);
        model.addAttribute("products", products);
        return "product-detail";
    }

    @GetMapping("/products-in-category/{id}")
    public String getProductsInCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id);
        List<CategoryDTO> categoryDTOS = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getProductsInCategory(id);
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("viewProducts", listViewProducts);
        model.addAttribute("categories",categoryDTOS);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "products-in-category";
    }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        List<Product> products = productService.filterHighPrice();
        List<CategoryDTO> categoryDTOS = categoryService.getCategoryAndProduct();
        model.addAttribute("categories",categories);
        model.addAttribute("products", products);
        model.addAttribute("categoriesDTO", categoryDTOS);
        return "filter-high-price";
    }

    @GetMapping("/low-price")
    public String filterLowPrice(Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        List<Product> products = productService.filterLowPrice();
        List<CategoryDTO> categoryDTOS = categoryService.getCategoryAndProduct();
        model.addAttribute("categoryDTOS",categoryDTOS);
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "filter-low-price";
    }
}
