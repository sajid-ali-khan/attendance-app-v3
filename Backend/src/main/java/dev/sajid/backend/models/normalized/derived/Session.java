package dev.sajid.backend.models.normalized.derived;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sessionName = "Untitled";

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    private int numPresent;
    private int totalCount;

    private LocalDateTime timeStamp = LocalDateTime.now();

    @ToString.Exclude
    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AttendanceRecord> attendanceRecords = new ArrayList<>();
}
