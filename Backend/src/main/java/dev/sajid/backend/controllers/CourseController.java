package dev.sajid.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.sajid.backend.models.raw.Course;
import dev.sajid.backend.services.CsvProcessingService;
import dev.sajid.backend.services.RawCourseProcessor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {
    @Autowired
    CsvProcessingService csvProcessingService;

    @Autowired
    RawCourseProcessor rawCourseProcessor;


    @PostMapping("/bulkupload")
    public ResponseEntity<Void> courseBulkUpload(@RequestParam("file") MultipartFile file) {
        
        try {
            List<Course> rawCourses =csvProcessingService.process(file, Course.class);
            rawCourseProcessor.processRawCourses(rawCourses);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
}
