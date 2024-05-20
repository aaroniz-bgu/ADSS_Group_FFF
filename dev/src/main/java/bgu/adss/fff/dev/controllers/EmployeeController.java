package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody FullEmployeeDto request) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<EmployeeDto[]> getEmployees() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") long id) {
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<EmployeeDto> updateEmployee(@RequestBody EmployeeDto request) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) {
        return ResponseEntity.noContent().build();
    }
}
