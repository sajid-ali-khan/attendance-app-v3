package dev.sajid.backend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sajid.backend.repositories.SchemeRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/schemes")
public class SchemeController {
    final SchemeRepository schemeRepository;

    SchemeController(SchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<String>> getSchemes() {
        return ResponseEntity.ok(schemeRepository.findAllCodes());
    }
    
}
