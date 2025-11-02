package dev.sajid.backend.controllers;

import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.BranchRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.models.normalized.course.Subject;
import dev.sajid.backend.repositories.BranchSubjectRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/branch-subjects")
@CrossOrigin
public class BranchSubjectController {
    private final BranchSubjectRepository branchSubjectService;
    private final BranchRepository branchRepository;

    BranchSubjectController(BranchSubjectRepository branchSubjectService, BranchRepository branchRepository) {
        this.branchSubjectService = branchSubjectService;
        this.branchRepository = branchRepository;
    }

    @GetMapping("/subjects")
    public ResponseEntity<?> getSubjects(@RequestParam("branchId") int branchId, @RequestParam("semester") int semester) {
        if (!branchRepository.existsById(branchId))
            throw new ResourceNotFoundException("Branch doesn't exists with ID: " + branchId);

        List<Subject> subjects = branchSubjectService.findSubjectsByBranchIdAndSemester(branchId, semester);
        return ResponseEntity.ok(subjects);
    }
    
}
