package dev.sajid.backend.repositories;

import dev.sajid.backend.models.normalized.derived.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionReporitory extends JpaRepository<Session, Integer> {
    List<Session> findByCourse_IdAndTimeStampBetween(int courseId, LocalDateTime startTime, LocalDateTime endTime);
}
