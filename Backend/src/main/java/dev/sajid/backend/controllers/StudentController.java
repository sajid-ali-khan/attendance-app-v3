package dev.sajid.backend.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.raw.Student;
import dev.sajid.backend.services.csv.CsvProcessingService;
import dev.sajid.backend.services.csv.RawStudentsProcessor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {
    @Autowired
    CsvProcessingService csvProcessingService;
    @Autowired
    RawStudentsProcessor rawStudentsProcessor;

    @PostMapping("/bulkupload")
    public ResponseEntity<?> bulkUploadStudents(@RequestParam MultipartFile file) {
        List<Student> rawStudents = csvProcessingService.process(file, Student.class);
        rawStudentsProcessor.process(rawStudents);
        return ResponseEntity.ok().body(Map.of(
                "message", "Students uploaded successfully."
        ));
    }
    
}
