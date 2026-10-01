package com.trainosys.shopapi.category;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    public final CategoryServiceImpl categoryService;

    public CategoryController (CategoryServiceImpl categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping("/api/admin/category/create")
    public Category createCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @GetMapping("/api/public/category")
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

}
