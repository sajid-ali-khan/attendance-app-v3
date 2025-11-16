package dev.sajid.backend.repositories;

import dev.sajid.backend.models.normalized.derived.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    List<Session> findByCourse_IdAndCreatedAtBetween(int courseId, Instant startTime, Instant endTime);

    List<Session> findByCourse_Id(int courseId);
}
