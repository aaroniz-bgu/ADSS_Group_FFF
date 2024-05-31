package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static bgu.adss.fff.dev.controllers.mappers.CategoryMapper.map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service){
        this.service = service;
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("name") String name){
        return ResponseEntity.ok(map(service.getCategory(name)));
    }

}
