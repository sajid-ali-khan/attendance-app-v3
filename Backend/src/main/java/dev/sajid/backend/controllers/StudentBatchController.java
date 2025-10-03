package dev.sajid.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.repositories.StudentBatchRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/student-batches")
public class StudentBatchController {
    private final StudentBatchRepository studentBatchRepository;

    StudentBatchController(StudentBatchRepository studentBatchRepository) {
        this.studentBatchRepository = studentBatchRepository;
    }

    @GetMapping("/branch/{branchCode}/semesters")
    public ResponseEntity<List<Integer>> getSemestersByBranchCode(@PathVariable("branchCode") String branchCode) {
        List<Integer> semesters = studentBatchRepository.findSemestersByBranchCode(branchCode);
        return ResponseEntity.ok(semesters);
    }
    
    @GetMapping("/branch/{branchCode}/sections")
    public ResponseEntity<List<String>> getSectionsBySemesterAndBranchCode(@PathVariable("branchCode") String branchCode, @RequestParam("semester") int semester) {
        List<String> sections = studentBatchRepository.findSectionsByBranchCodeAndSemester(branchCode, semester);
        return ResponseEntity.ok(sections);
    }
}
