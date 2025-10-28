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
    @Autowired
    SessionService sessionService;

    @Autowired
    SessionReporitory sessionReporitory;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @GetMapping("")
    public ResponseEntity<?> getSessionsBySessionIdAndDate(
            @RequestParam("courseId") int courseId,
            @RequestParam("date") LocalDate date
    ) {
        if (!courseRepository.existsById(courseId))
            throw new ResourceNotFoundException("Course not found with courseId: " + courseId);

        return ResponseEntity.ok(sessionService.getSessionsByCourseIdAndDate(courseId, date));
    }

    @PostMapping("")
    public ResponseEntity<?> createNewSession(
            @RequestParam("courseId") int courseId,
            @RequestParam("facultyCode") String facultyCode
    ) {
        if (!courseRepository.existsById(courseId) || !facultyRepository.existsByCode(facultyCode)){
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "courseId or facultyCode not found."
            ));
        }
        SessionDto sessionDto = sessionService.createNewSession(courseId, facultyCode);
        return ResponseEntity.ok(sessionDto);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable("sessionId") int sessionId){
        if (!sessionReporitory.existsById(sessionId))
            throw new ResourceNotFoundException("Session not found with ID: " + sessionId);

        SessionRegisterDto sessionRegisterDto = sessionService.getSessionRegister(sessionId);
        return ResponseEntity.ok(sessionRegisterDto);
    }

    @PutMapping("")
    public ResponseEntity<?> updateSession(@RequestBody SessionRegisterDto sessionRegisterDto){
        if (!sessionReporitory.existsById(sessionRegisterDto.sessionId()))
            throw new ResourceNotFoundException("Session not found with ID: " + sessionRegisterDto.sessionId());

        sessionService.updateSession(sessionRegisterDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable("sessionId") int sessionId){
        if (!sessionReporitory.existsById(sessionId)) throw new ResourceNotFoundException("Session not found with ID: " + sessionId);

        sessionService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }


}
