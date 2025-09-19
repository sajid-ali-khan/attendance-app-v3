package dev.sajid.backend.models.normalized.derived;

import dev.sajid.backend.models.normalized.course.ProgramSubject;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_batch_id", nullable = false)
    private StudentBatch studentBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_subject_id", nullable = false)
    private ProgramSubject programSubject;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Session> sessions;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseAssignment> courseAssignments = new ArrayList<>();
}
