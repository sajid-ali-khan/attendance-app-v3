package dev.sajid.backend.repositories;

import dev.sajid.backend.dtos.SessionRegisterDto;
import dev.sajid.backend.models.normalized.derived.Session;
import dev.sajid.backend.models.normalized.student.Student;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionReporitory extends JpaRepository<Session, Integer> {
    List<Session> findByCourse_IdAndTimeStampBetween(int courseId, Instant startTime, Instant endTime);

    List<Session> findByCourse_Id(int courseId);
}
