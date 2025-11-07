package dev.sajid.backend.controllers;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BranchController {
    private final BranchRepository branchRepository;
    private final SchemeRepository schemeRepository;

    BranchController(BranchRepository branchRepository, SchemeRepository schemeRepository) {
        this.branchRepository = branchRepository;
        this.schemeRepository = schemeRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getBranchesByScheme(@RequestParam("scheme") String scheme) {
        if (!schemeRepository.existsByCode(scheme))
            throw new ResourceNotFoundException("Scheme not found with ID: " + scheme);
        return ResponseEntity.ok(branchRepository.findByScheme(scheme));
    }

}
