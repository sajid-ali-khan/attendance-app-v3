package dev.sajid.backend.services;

import dev.sajid.backend.dtos.ClassDto;
import dev.sajid.backend.dtos.SessionDto;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.repositories.SessionReporitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionReporitory sessionReporitory;
    public List<SessionDto> getSessionsByCourseIdAndDate(int courseId, LocalDate date){
        List<Session> sessions = sessionReporitory.findByCourse_IdAndTimeStampBetween(courseId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        return sessions.stream()
                .map(s -> {
                        String subjectName = s.getCourse().getBranchSubject().getSubject().getFullForm();
                        String subjectCode = s.getCourse().getBranchSubject().getSubject().getShortForm();
                        String subject = subjectCode + " - " + subjectName;

                        String branchName = s.getCourse().getBranchSubject().getBranch().getShortForm();
                        int semester = s.getCourse().getStudentBatch().getSemester();
                        String section = s.getCourse().getStudentBatch().getSection();
                    LocalDateTime createdAt = s.getTimeStamp();

                    ClassDto classDto = new ClassDto(subject, branchName, semester, section);
                    return new SessionDto(classDto, createdAt);
                })
                .distinct()
                .toList();
    }
}
