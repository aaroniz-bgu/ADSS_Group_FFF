package bgu.adss.fff.dev.controllers;

import bgu.adss.fff.dev.domain.models.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bgu.adss.fff.dev.services.BranchService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/branch")
public class BranchController {
    private final BranchService service;

    @Autowired
    public BranchController(BranchService service) { this.service = service; }

    /**
     * Establishes a new branch in the system.
     * @param branchName The branch's name.
     * @return ResponseEntity containing the created branch's name if successful, or a bad request status if the input is invalid.
     */
    @PostMapping("/{branchName}")
    public ResponseEntity<String> createBranch(@PathVariable String branchName) {
        return new ResponseEntity<>(service.createBranch(new Branch(branchName)).getName(), HttpStatus.CREATED);
    }

    /**
     * Fetches all branches in the system.
     * @return ResponseEntity containing an array of branch names or no content if none exist.
     */
    @GetMapping
    public ResponseEntity<String[]> getBranches() {
        String[] branches = new String[0];
        return ResponseEntity.ok().body(service.getBranches()
                .stream()
                .map(Branch::getName)
                .toList()
                .toArray(branches));
    }

    /**
     * Removes a branch by its name.
     * @param branchName
     * @return ResponseEntity containing no content if successful, or a not found status if the branch does not exist.
     */
    @DeleteMapping("/{branchName}")
    public ResponseEntity<String> deleteBranch(@PathVariable("branchName") String branchName) {
        service.removeBranch(branchName);
        return ResponseEntity.noContent().build();
    }
}
