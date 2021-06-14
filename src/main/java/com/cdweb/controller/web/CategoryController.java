package com.cdweb.controller.web;

import com.cdweb.dto.CategoryDTO;
import com.cdweb.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("/addCategory")
    public CategoryDTO save(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.save(categoryDTO);
    }
    @GetMapping("/getCategory")
    public List<CategoryDTO> get(){
        return categoryService.findAll();
    }
}
