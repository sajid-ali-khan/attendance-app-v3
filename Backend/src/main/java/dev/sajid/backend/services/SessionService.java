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
import dev.sajid.backend.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    private final CourseRepository courseRepository;

    private final FacultyRepository facultyRepository;

    private final AttendanceRecordRepository attendanceRecordRepository;

    public SessionService(SessionRepository sessionRepository, CourseRepository courseRepository, FacultyRepository facultyRepository, AttendanceRecordRepository attendanceRecordRepository) {
        this.sessionRepository = sessionRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    public Map<Integer, SessionDto> getSessionsByCourseIdAndDate(int courseId, LocalDate date) {
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        Instant start = date.atStartOfDay(zone).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(zone).toInstant();
        List<Session> sessions = sessionRepository.findByCourse_IdAndCreatedAtBetween(courseId, start, end);
        return sessions.stream()
                .collect(Collectors.toMap(
                        Session::getId, // key mapper
                        s -> new SessionDto(s.getId(), s.getSessionName(), s.getCreatedAt()),
                        (existing, replacement) -> existing // handle duplicate keys (if any)
                ));
    }

    public SessionDto createNewSession(int courseId, String facultyCode) {
        Session newSession = new Session();
        newSession.setCourse(courseRepository.findById(courseId).get());
        newSession.setFaculty(facultyRepository.findByUsername(facultyCode).get());
        sessionRepository.save(newSession);
        createAndAddAttendanceRecords(newSession);
        return new SessionDto(newSession.getId(), newSession.getSessionName(), newSession.getUpdatedAt());
    }

    public SessionRegisterDto getSessionRegister(int sessionId) {
        Session session = sessionRepository.findById(sessionId).get();

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
                session.getUpdatedAt(),
                new TreeMap<>(studentAttendanceRowMap)
        );
    }

    public void updateSession(SessionRegisterDto sessionRegisterDto){
        Session actualSession = sessionRepository.findById(sessionRegisterDto.sessionId()).get();
        actualSession.setSessionName(sessionRegisterDto.sessionName());
        actualSession.setNumPresent(sessionRegisterDto.presentCount());
        updateAttendanceRecords(actualSession, sessionRegisterDto.attendanceRowMap());
    }

    private void updateAttendanceRecords(Session session, Map<Integer, AttendanceRecordDto> attendanceRecordDtoMap){
        List<AttendanceRecord> attendanceRecords = session.getAttendanceRecords();
        for (AttendanceRecord attendanceRecord: attendanceRecords){
            attendanceRecord.setStatus(attendanceRecordDtoMap.get(attendanceRecord.getId()).status());
        }
        attendanceRecordRepository.saveAll(attendanceRecords);
        sessionRepository.save(session);
    }

    private void createAndAddAttendanceRecords(Session session){
        List<AttendanceRecord> attendanceRecords = new ArrayList<>();
        List<Student> students = courseRepository.findStudentListById(session.getCourse().getId());
        students.sort(Comparator.comparing(Student::getRoll));
        for (Student student: students){
            attendanceRecords.add(new AttendanceRecord(0, session, student, false));
        }

        attendanceRecordRepository.saveAll(attendanceRecords);
        session.setAttendanceRecords(attendanceRecords);
        session.setTotalCount(attendanceRecords.size());
        sessionRepository.save(session);
    }

    public void deleteSession(int sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}
