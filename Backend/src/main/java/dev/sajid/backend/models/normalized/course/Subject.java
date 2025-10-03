package dev.sajid.backend.models.normalized.course;

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
@Table(name = "subjects", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"short_form", "full_form"})
})
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String shortForm;

    @Column(nullable = false)
    private String fullForm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    List<BranchSubject> branchSubjects = new ArrayList<>();
}
