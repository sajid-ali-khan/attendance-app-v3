package dev.sajid.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.raw.Employee;
import dev.sajid.backend.repositories.FacultyRepository;
import dev.sajid.backend.services.CsvProcessingService;
import dev.sajid.backend.services.RawEmployeeProcessor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/faculties")
@CrossOrigin
public class FacultyController {
    final CsvProcessingService csvProcessingService;
    final RawEmployeeProcessor rawEmployeeProcessor;
    private final FacultyRepository facultyRepository;;

    FacultyController(CsvProcessingService csvProcessingService, RawEmployeeProcessor rawEmployeeProcessor, FacultyRepository facultyRepository) {
        this.csvProcessingService = csvProcessingService;
        this.rawEmployeeProcessor = rawEmployeeProcessor;
        this.facultyRepository = facultyRepository;
    }
    
    @PostMapping("/bulkupload")
    public ResponseEntity<String> bulkUploadFaculties(@RequestParam MultipartFile file) {
        try{
            List<Employee> rawEmployees = csvProcessingService.process(file, Employee.class);
            rawEmployeeProcessor.processEmployees(rawEmployees);
            return ResponseEntity.ok("Faculty data uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return ResponseEntity.ok(faculties);
    }
    
    
}
