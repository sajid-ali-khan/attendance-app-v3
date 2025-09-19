package dev.sajid.backend.models.normalized.student;

import dev.sajid.backend.models.normalized.course.Program;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_batches")
public class StudentBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(nullable = false)
    private int semester;

    @OneToMany(mappedBy = "student_batch", fetch = FetchType.LAZY)
    private List<Student> students;
}
