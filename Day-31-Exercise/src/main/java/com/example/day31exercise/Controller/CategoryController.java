package com.example.day31exercise.Controller;

import com.example.day31exercise.ApiResponse.ApiResponse;
import com.example.day31exercise.Model.Category;
import com.example.day31exercise.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/get-all")
    public ResponseEntity getCategories() {
        return ResponseEntity.status(200).body(categoryService.getAll());
    }


    @PostMapping("/add")
    public ResponseEntity addCategory (@RequestBody @Valid Category category, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        categoryService.addCategory(category);
        return ResponseEntity.status(200).body(new ApiResponse("category added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateCategory (@PathVariable Integer id, @RequestBody @Valid Category category, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = categoryService.updateCategory(id,category);
        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("category updated successfully"));
        }
        else {
            return ResponseEntity.status(400).body(new ApiResponse("no category with this id was found"));
        }


    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCategory (@PathVariable Integer id){
        boolean result = categoryService.deleteCategory(id);

        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("category deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("no category with this id was found"));

    }
}
