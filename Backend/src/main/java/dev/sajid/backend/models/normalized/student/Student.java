package dev.sajid.backend.models.normalized.student;

import dev.sajid.backend.models.normalized.derived.AttendanceRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String roll;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_batch_id", nullable = false)
    private StudentBatch studentBatch;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;
}
