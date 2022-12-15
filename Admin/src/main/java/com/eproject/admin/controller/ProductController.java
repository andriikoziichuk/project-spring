package com.eproject.admin.controller;

import com.eproject.library.dto.ProductDTO;
import com.eproject.library.model.Category;
import com.eproject.library.service.CategoryService;
import com.eproject.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null)
            return "redirect:/login";

        List<ProductDTO> productDTOList = productService.findAll();
        model.addAttribute("products", productDTOList);
        model.addAttribute("size", productDTOList.size());
        model.addAttribute("title", "Products manager");

        Page<ProductDTO> products = productService.pageProducts(0);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", 0);

        return "products";
    }

    @GetMapping("/add-product")
    public String addProductForm(Model model) {
        model.addAttribute("categories", categoryService.findAllByActivated());
        model.addAttribute("product", new ProductDTO());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") ProductDTO productDto,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes attributes){
        try {
            productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("success", "Add successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to add!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT , RequestMethod.GET})
    public String enabledProduct(@PathVariable("id")Long id, RedirectAttributes attributes){
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "Enabled successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to enabled!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes){
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "Deleted successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to deleted");
        }
        return "redirect:/products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id")Long id, Model model, Principal principal){
        if (principal == null)
            return "redirect:/login";

        model.addAttribute("title", "Update products");
        List<Category> categories = categoryService.findAllByActivated();
        ProductDTO productDTO = productService.getById(id);
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDTO);

        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String processUpdate(
                                @ModelAttribute("productDto") ProductDTO productDTO,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes){
        try {
            productService.update(imageProduct, productDTO);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update!");
        }
        return "redirect:/products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Page<ProductDTO> products = productService.pageProducts(pageNo);
        model.addAttribute("title", "Manage Product");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/search-result/{pageNo}")
    public String searchProducts(@PathVariable("pageNo")int pageNo,
                                 @RequestParam("keyword")String keyword,
                                 Model model,
                                 Principal principal) {

        if (principal == null)
            return "redirect:/login";
        Page<ProductDTO> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("products", products);
        model.addAttribute("title", "Search result");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        return "result-products";
    }
}
