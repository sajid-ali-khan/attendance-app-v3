package dev.sajid.backend.controllers;

import java.util.List;
import java.util.Map;

import dev.sajid.backend.dtos.ClassDto;
import dev.sajid.backend.services.FacultyService;
import dev.sajid.backend.services.csv.RawEmployeeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.raw.Employee;
import dev.sajid.backend.repositories.FacultyRepository;
import dev.sajid.backend.services.csv.CsvProcessingService;


@Slf4j
@RestController
@RequestMapping("/api/faculties")
@CrossOrigin
public class FacultyController {
    final CsvProcessingService csvProcessingService;
    final RawEmployeeProcessor rawEmployeeProcessor;
    private final FacultyRepository facultyRepository;;
    private final FacultyService facultyService;

    FacultyController(CsvProcessingService csvProcessingService, RawEmployeeProcessor rawEmployeeProcessor, FacultyRepository facultyRepository, FacultyService facultyService) {
        this.csvProcessingService = csvProcessingService;
        this.rawEmployeeProcessor = rawEmployeeProcessor;
        this.facultyRepository = facultyRepository;
        this.facultyService = facultyService;
    }
    
    @PostMapping("/bulkupload")
    public ResponseEntity<?> bulkUploadFaculties(@RequestParam MultipartFile file) {
        List<Employee> rawEmployees = csvProcessingService.process(file, Employee.class);
        rawEmployeeProcessor.processEmployees(rawEmployees);
        return ResponseEntity.ok()
                .body(Map.of(
                        "message", "Faculties uploaded successfully."
                ));
    }

    @GetMapping("")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{facultyId}/classes")
    public ResponseEntity<List<ClassDto>> getAssignedClasses(@PathVariable String facultyId){
        List<ClassDto> classes = facultyService.getAssignedClasses(facultyId);
        return ResponseEntity.ok(classes);
    }
    
}
