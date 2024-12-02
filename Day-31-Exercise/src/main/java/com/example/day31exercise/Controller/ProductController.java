package com.example.day31exercise.Controller;

import com.example.day31exercise.ApiResponse.ApiResponse;
import com.example.day31exercise.Model.Product;
import com.example.day31exercise.Service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/product")
public class ProductController {
    private final ProductService productService;

    // READ
    @GetMapping("/get-all")
    public ResponseEntity getProducts() {
        return ResponseEntity.status(200).body(productService.getProducts());
    }

    // CREATE
    @PostMapping("/add")
    public ResponseEntity addProducts (@RequestBody @Valid Product product, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        boolean result = productService.addProduct(product);
        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("product added successfully"));
        }else{
            return ResponseEntity.status(400).body(new ApiResponse("category does not exist. Failed to add product"));
        }

    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct (@PathVariable Integer id, @RequestBody @Valid Product product, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        int result = productService.updateProduct(id,product);
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("product updated successfully"));
        }
        else if (result == 0){
            return ResponseEntity.status(400).body(new ApiResponse("no product with this id was found"));
        }else {
            return ResponseEntity.status(400).body(new ApiResponse("category does not exist. Failed to update product"));
        }


    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct (@PathVariable Integer id){
        boolean result = productService.deleteProduct(id);

        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("product deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("no product with this id was found"));

    }


}
