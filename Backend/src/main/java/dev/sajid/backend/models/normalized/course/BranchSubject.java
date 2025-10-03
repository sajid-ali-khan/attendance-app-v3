package dev.sajid.backend.models.normalized.course;

import dev.sajid.backend.models.normalized.derived.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branch_subjects",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"branch_id", "subject_id", "semester"})})
public class BranchSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private int semester;

    @ToString.Exclude
    @OneToMany(mappedBy = "branchSubject", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();
}
