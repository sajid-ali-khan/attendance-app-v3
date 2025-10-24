package dev.sajid.backend.controllers;

import dev.sajid.backend.dtos.SessionDto;
import dev.sajid.backend.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    SessionService sessionService;

    @GetMapping("")
    public ResponseEntity<List<SessionDto>> getSessionsBySessionIdAndDate(
            @RequestParam("courseId") int courseId,
            @RequestParam("date") LocalDate date
            ){
        return ResponseEntity.ok(sessionService.getSessionsByCourseIdAndDate(courseId, date));
    }
}
