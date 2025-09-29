package dev.sajid.backend.models.normalized.course;

import dev.sajid.backend.models.normalized.student.StudentBatch;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringExclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "programs")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ToString.Exclude
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree degree = Degree.BTech;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ToString.Exclude
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<StudentBatch> studentBatches = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<ProgramSubject> programSubjects = new ArrayList<>();
}
