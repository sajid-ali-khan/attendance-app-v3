package dev.sajid.backend.repositories;

import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {
}
