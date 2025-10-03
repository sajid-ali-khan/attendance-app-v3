package dev.sajid.backend.models.normalized.student;

import dev.sajid.backend.models.normalized.course.Branch;
import dev.sajid.backend.models.normalized.derived.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_batches", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"branch_id", "semester", "section"})
})
public class StudentBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private int semester;

    @Column(nullable = false)
    private String section;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "studentBatch", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "studentBatch", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
}
