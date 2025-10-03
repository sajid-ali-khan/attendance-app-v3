package dev.sajid.backend.models.normalized.course;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.sajid.backend.models.normalized.student.StudentBatch;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branches",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"scheme_id", "branch_code"})
})
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int branchCode;

    @Column(nullable = false)
    private String shortForm;

    private String fullForm;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme scheme;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<StudentBatch> studentBatches = new ArrayList<>();


    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private List<BranchSubject> branchSubjects = new ArrayList<>();

}
