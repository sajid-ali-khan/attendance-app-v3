package dev.sajid.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.models.normalized.course.Scheme;
import dev.sajid.backend.repositories.SchemeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/schemes")
public class SchemeController {
    final SchemeRepository schemeRepository;

    SchemeController(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Scheme>> getMethodName() {
        return ResponseEntity.ok(schemeRepository.findAll());
    }
    
}
