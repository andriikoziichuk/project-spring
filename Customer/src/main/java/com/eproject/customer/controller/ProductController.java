package com.eproject.customer.controller;

import com.eproject.library.dto.CategoryDTO;
import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Category;
import com.eproject.library.model.Product;
import com.eproject.library.service.CategoryService;
import com.eproject.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(@RequestParam(defaultValue = "id,desc") String[] sort,
                           Model model){
        List<CategoryDTO> categoryDtoList = categoryService.getCategoryAndProduct();

        List<Product> listViewProducts = productService.listViewProducts();

        List<ProductDTO> products = productService.findAll(sort);//Sort.by(orders));

        model.addAttribute("pageName", "Книги");
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
        model.addAttribute("pageName", "Пошук книги");
        model.addAttribute("product", product);
        model.addAttribute("products", products);
        return "product-detail";
    }

    @GetMapping("/products-in-category/{id}")
    public String getProductsInCategory(@RequestParam(defaultValue = "id,desc") String[] sort,
                                        @PathVariable("id") Long id, Model model) {
        Category category = categoryService.findById(id);
        List<CategoryDTO> categoryDTOS = categoryService.getCategoryAndProduct();
        List<Product> products = productService.getProductsInCategory(id, sort);
        List<Product> listViewProducts = productService.listViewProducts();
        model.addAttribute("pageName", "Книги в категорії");
        model.addAttribute("viewProducts", listViewProducts);
        model.addAttribute("categories",categoryDTOS);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "products-in-category";
    }

    @GetMapping("/search-result")
    public String searchProducts(@RequestParam(defaultValue = "id,desc") String[] sort,
                                 @RequestParam("keyword") String keyword,
                                 Model model,
                                 Principal principal) {

        if (principal == null)
            return "redirect:/login";

        List<CategoryDTO> categoryDtoList = categoryService.getCategoryAndProduct();
        List<Product> listViewProducts = productService.listViewProducts();
        List<ProductDTO> products = productService.searchProducts(keyword, sort);

        model.addAttribute("products", products);
        model.addAttribute("size", products.size());
        model.addAttribute("pageName", "Результат пошуку");
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("viewProducts", listViewProducts);

        return "shop";
    }
}
