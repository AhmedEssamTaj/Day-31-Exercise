package com.example.day31exercise.Service;

import com.example.day31exercise.Model.Category;
import com.example.day31exercise.Model.Merchant;
import com.example.day31exercise.Model.Product;
import com.example.day31exercise.Repository.CategoryRepository;
import com.example.day31exercise.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service

public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // READ
    public List<Product> getProducts () {
        return productRepository.findAll();
    }

    // CREATE
    public boolean addProduct (Product product){
        // make sure category already exist
        for (Category c : categoryRepository.findAll()){
            if (c.getCategory_id().equals(product.getCategory_id())){
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

    // UPDATE
    public int updateProduct (Integer id, Product product) {
        boolean categoryExists = false;
        Product oldProduct = productRepository.getById(id);
        if (oldProduct == null) {
            return 0; // product does not exist
        }

        // check if the category exist
        for (Category c : categoryRepository.findAll()) {
            if (c.getCategory_id().equals(product.getCategory_id())) {

                oldProduct.setProduct_name(product.getProduct_name());
                oldProduct.setProduct_price(product.getProduct_price());
                oldProduct.setCategory_id(product.getCategory_id());
                productRepository.save(oldProduct);
                return 1; // all good
            }
        }
        return -1; // category does not exist

    }

    // DELETE
    public boolean deleteProduct (Integer id){
        Product oldProduct = productRepository.getById(id);

        if(oldProduct != null){
            productRepository.delete(oldProduct);
            return true;
        }
        return false;
    }

    }


