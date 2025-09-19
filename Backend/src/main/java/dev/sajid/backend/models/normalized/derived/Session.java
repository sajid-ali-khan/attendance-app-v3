package dev.sajid.backend.models.normalized.derived;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    private int numPresent;
    private int numAbsent;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeStamp;

    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;
}
