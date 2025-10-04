package dev.sajid.backend.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.repositories.BranchRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/branches")
@CrossOrigin
public class BranchController {
    final BranchRepository branchRepository;

    BranchController(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Branch>> getBranchesByScheme(@RequestParam("scheme") String scheme) {
        return ResponseEntity.ok(branchRepository.findByScheme(scheme));
    }
    
}
