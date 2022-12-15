package com.eproject.library.repository;

import com.eproject.library.model.Product;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
    Page<Product> searchProduct(String keyword, Pageable pageable);

    @Query("select p from Product p where p.description like %?1% or p.name like %?1%")
    List<Product> searchProductsList(String keyword);
    List<Product> searchByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(@Length(max = 256, message = "Title too long(256)") String name, String description);
    /*Customer*/
    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false")
    List<Product> getAllProducts();


    @Query(value = "select * from products p where p.is_deleted = false and p.is_activated = true order by rand() asc ", nativeQuery = true)
    List<Product> listViewProducts();


    @Query(value = "select * from products p inner join categories c on c.category_id = p.category_id where p.category_id = ?1", nativeQuery = true)
    List<Product> getRelatedProducts(Long categoryId);


    @Query(value = "select p from Product p inner join Category c on c.id = p.category.id where c.id = ?1 and p.is_deleted = false and p.is_activated = true")
    List<Product> getProductsInCategory(Long categoryId);


    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false" +
            " order by p.costPrice desc")
    List<Product> filterHighPrice();


    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false order by p.costPrice ")
    List<Product> filterLowPrice();

}
