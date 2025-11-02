package dev.sajid.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.sajid.backend.dtos.AssignedClassDto;
import dev.sajid.backend.exceptions.ResourceNotFoundException;
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
    private final FacultyRepository facultyRepository;
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

    @GetMapping("/{facultyCode}/name")
    public ResponseEntity<?> getFacultyName(@PathVariable String facultyCode) {
        Optional<Faculty> faculty = facultyRepository.findByUsername(facultyCode);
        if (faculty.isPresent())
            return ResponseEntity.ok().body(Map.of(
                    "name", faculty.get().getSalutation() + faculty.get().getName()
            ));
        else throw new ResourceNotFoundException("Faculty not found with code: " + facultyCode);
    }

    @GetMapping("")
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{facultyId}/classes")
    public ResponseEntity<?> getAssignedClasses(@PathVariable String facultyId) {
        if (!facultyRepository.existsByCode(facultyId))
            throw new ResourceNotFoundException("A faculty with facultyCode: " + facultyId + " does not exist.");

        List<AssignedClassDto> classes = facultyService.getAssignedClasses(facultyId);
        return ResponseEntity.ok(classes);
    }

}
