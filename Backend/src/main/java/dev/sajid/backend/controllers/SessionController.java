package dev.sajid.backend.controllers;

import dev.sajid.backend.dtos.SessionDto;
import dev.sajid.backend.dtos.SessionRegisterDto;
import dev.sajid.backend.exceptions.ResourceNotFoundException;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.FacultyRepository;
import dev.sajid.backend.repositories.SessionReporitory;
import dev.sajid.backend.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final SessionReporitory sessionReporitory;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    public SessionController(SessionService sessionService, SessionReporitory sessionReporitory, CourseRepository courseRepository, FacultyRepository facultyRepository) {
        this.sessionService = sessionService;
        this.sessionReporitory = sessionReporitory;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getSessionsBySessionIdAndDate(
            @RequestParam("courseId") int courseId,
            @RequestParam("date") LocalDate date
    ) {
        checkCourseExistence(courseId);
        return ResponseEntity.ok(sessionService.getSessionsByCourseIdAndDate(courseId, date));
    }

    @PostMapping("")
    public ResponseEntity<?> createNewSession(
            @RequestParam("courseId") int courseId,
            @RequestParam("facultyCode") String facultyCode
    ) {
        checkCourseExistence(courseId);
        if (!facultyRepository.existsByCode(facultyCode))
            throw new ResourceNotFoundException("Faculty not found with ID: " + facultyCode);

        SessionDto sessionDto = sessionService.createNewSession(courseId, facultyCode);
        return ResponseEntity.ok(sessionDto);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable("sessionId") int sessionId) {
        checkSessionExistence(sessionId);
        SessionRegisterDto sessionRegisterDto = sessionService.getSessionRegister(sessionId);
        return ResponseEntity.ok(sessionRegisterDto);
    }

    @PutMapping("")
    public ResponseEntity<?> updateSession(@RequestBody SessionRegisterDto sessionRegisterDto) {
        checkSessionExistence(sessionRegisterDto.sessionId());
        sessionService.updateSession(sessionRegisterDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable("sessionId") int sessionId) {
        checkSessionExistence(sessionId);
        sessionService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }

    private void checkSessionExistence(int sessionId) {
        if (!sessionReporitory.existsById(sessionId))
            throw new ResourceNotFoundException("Session not found with ID: " + sessionId);
    }

    private void checkCourseExistence(int courseId){
        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
    }


}
