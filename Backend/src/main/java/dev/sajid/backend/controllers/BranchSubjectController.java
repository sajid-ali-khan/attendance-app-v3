package dev.sajid.backend.controllers;

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

    BranchSubjectController(BranchSubjectRepository branchSubjectService) {
        this.branchSubjectService = branchSubjectService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getSubjects(@RequestParam("branchId") int branchId, @RequestParam("semester") int semester) {
        List<Subject> subjects = branchSubjectService.findSubjectsByBranchIdAndSemester(branchId, semester);
        return ResponseEntity.ok(subjects);
    }
    
}
