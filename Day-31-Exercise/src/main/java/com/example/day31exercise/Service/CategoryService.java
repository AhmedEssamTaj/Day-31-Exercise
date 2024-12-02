package com.example.day31exercise.Service;

import com.example.day31exercise.Model.Category;
import com.example.day31exercise.Repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

    // Read
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    // Create
    public void addCategory(Category category){
        categoryRepository.save(category);
    }

    // Update
    public boolean updateCategory(Integer id,Category category){
        Category oldCategory = categoryRepository.getById(id);
        if(oldCategory != null){
            oldCategory.setCategory_name(category.getCategory_name());
            categoryRepository.save(oldCategory);
            return true;
        }
        return false;

    }

    // Delete
    public boolean deleteCategory(Integer id){
        Category oldCategory = categoryRepository.getById(id);
        if(oldCategory != null){
            categoryRepository.delete(oldCategory);
            return true;
        }
        return false;
    }


}
