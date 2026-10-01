package com.trainosys.shopapi.category;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CategoryServiceImpl implements CategoryDAO {
    private final AtomicLong idCounter = new AtomicLong(1);

    private List<Category> categories = new ArrayList<>();

    @Override
    public Category createCategory(Category category) {

        long nextId = idCounter.getAndIncrement();

        category.setId(nextId);

        // Add the category to your in-memory list
        categories.add(category);


        return category;
    }

    public List<Category> getAllCategories(){
        return categories;
    }

}
