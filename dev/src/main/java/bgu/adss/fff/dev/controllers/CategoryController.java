package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.controllers.mappers.CategoryMapper;
import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static bgu.adss.fff.dev.controllers.mappers.CategoryMapper.map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    @Autowired
    public CategoryController(CategoryService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto, @PathVariable){
        Category category = CategoryMapper.map(categoryDto);
        return new ResponseEntity<>(map(service.createCategory(category)), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("name") String name){
        return ResponseEntity.ok(map(service.getCategory(name)));
    }

    @GetMapping
    public ResponseEntity<CategoryDto[]> getCategory(){

        CategoryDto[] objects = new CategoryDto[0];

        return ResponseEntity.ok().body(service.getCategory()
                .stream()
                .map(CategoryMapper::map)
                .toList()
                .toArray(objects));
    }

    @PutMapping("/{name}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable String name, @RequestBody CategoryDto categoryDto){
        service.updateCategory(name, map(categoryDto));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCategory(@PathVariable ("name") String name){
        service.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }

}
