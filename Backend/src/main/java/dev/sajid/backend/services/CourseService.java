package dev.sajid.backend.services;

import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    public LocalDate getCourseStartDate(int courseId){
        List<Session> sessionList = courseRepository.findById(courseId).get().getSessions();
        log.debug(String.valueOf(sessionList.get(0).getTimeStamp()));
        sessionList.sort(Comparator.comparing(Session::getTimeStamp));
        if (sessionList.isEmpty() || sessionList.get(0).getTimeStamp() == null){
            log.debug("Timestamp is null");
            return LocalDateTime.now().toLocalDate();
        }
        log.debug("Timestamp is not null, it is: {}", sessionList.get(0).getTimeStamp());
        return sessionList.get(0).getTimeStamp().toLocalDate();
    }
}
