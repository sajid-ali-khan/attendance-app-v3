package dev.sajid.backend.services;

import dev.sajid.backend.dtos.AttendanceRecordDto;
import dev.sajid.backend.dtos.SessionDto;
import dev.sajid.backend.dtos.SessionRegisterDto;
import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.models.normalized.student.Student;
import dev.sajid.backend.repositories.AttendanceRecordRepository;
import dev.sajid.backend.repositories.CourseRepository;
import dev.sajid.backend.repositories.FacultyRepository;
import dev.sajid.backend.repositories.SessionReporitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionService {
    @Autowired
    private SessionReporitory sessionReporitory;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    public Map<Integer, SessionDto> getSessionsByCourseIdAndDate(int courseId, LocalDate date) {
        List<Session> sessions = sessionReporitory.findByCourse_IdAndTimeStampBetween(courseId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());

        return sessions.stream()
                .collect(Collectors.toMap(
                        Session::getId, // key mapper
                        s -> new SessionDto(s.getId(), s.getSessionName(), s.getTimeStamp()),
                        (existing, replacement) -> existing // handle duplicate keys (if any)
                ));
    }

    public SessionDto createNewSession(int courseId, String facultyCode) {
        Session newSession = new Session();
        newSession.setCourse(courseRepository.findById(courseId).get());
        newSession.setFaculty(facultyRepository.findByUsername(facultyCode).get());
        newSession.setTimeStamp(LocalDateTime.now());
        sessionReporitory.save(newSession);
        createAndAddAttendanceRecords(newSession);
        return new SessionDto(newSession.getId(), newSession.getSessionName(), newSession.getTimeStamp());
    }

    public SessionRegisterDto getSessionRegister(int sessionId) {
        Session session = sessionReporitory.findById(sessionId).get();

        Map<Integer, AttendanceRecordDto> studentAttendanceRowMap = new HashMap<>();

        for (AttendanceRecord attendanceRecord : session.getAttendanceRecords()) {
            studentAttendanceRowMap.put(attendanceRecord.getId(), new AttendanceRecordDto(
                    attendanceRecord.getId(),
                    attendanceRecord.getStudent().getRoll(),
                    attendanceRecord.getStudent().getName(),
                    attendanceRecord.isStatus())
            );
        }

        return new SessionRegisterDto(
                sessionId,
                session.getSessionName(),
                session.getNumPresent(),
                session.getTotalCount(),
                session.getTimeStamp(),
                studentAttendanceRowMap
        );
    }

    public void updateSession(SessionRegisterDto sessionRegisterDto){
        Session actualSession = sessionReporitory.findById(sessionRegisterDto.sessionId()).get();
        actualSession.setSessionName(sessionRegisterDto.sessionName());
        actualSession.setNumPresent(sessionRegisterDto.presentCount());
        actualSession.setTimeStamp(LocalDateTime.now());
        updateAttendanceRecords(actualSession, sessionRegisterDto.attendanceRowMap());
    }

    private void updateAttendanceRecords(Session session, Map<Integer, AttendanceRecordDto> attendanceRecordDtoMap){
        List<AttendanceRecord> attendanceRecords = session.getAttendanceRecords();
        for (AttendanceRecord attendanceRecord: attendanceRecords){
            attendanceRecord.setStatus(attendanceRecordDtoMap.get(attendanceRecord.getId()).status());
        }
        attendanceRecordRepository.saveAll(attendanceRecords);
        sessionReporitory.save(session);
    }

    private void createAndAddAttendanceRecords(Session session){
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        List<Student> students = courseRepository.findStudentListById(session.getCourse().getId());

        for (Student student: students){
            attendanceRecords.add(new AttendanceRecord(0, session, student, false));
        }

        attendanceRecordRepository.saveAll(attendanceRecords);
        session.setAttendanceRecords(attendanceRecords);
        session.setTotalCount(attendanceRecords.size());
        sessionReporitory.save(session);
    }

    public void deleteSession(int sessionId) {
        sessionReporitory.deleteById(sessionId);
    }
}
