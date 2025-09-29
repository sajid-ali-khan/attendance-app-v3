package dev.sajid.backend.models.normalized.derived;

import dev.sajid.backend.models.normalized.course.ProgramSubject;
import dev.sajid.backend.models.normalized.student.StudentBatch;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_batch_id", nullable = false)
    private StudentBatch studentBatch;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_subject_id", nullable = false)
    private ProgramSubject programSubject;

    @ToString.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Session> sessions;

    @ToString.Exclude
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<CourseAssignment> courseAssignments = new ArrayList<>();
}
