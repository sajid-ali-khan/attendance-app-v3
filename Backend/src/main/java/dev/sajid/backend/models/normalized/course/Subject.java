package dev.sajid.backend.models.normalized.course;

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
@Table(name = "subjects")
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

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<ProgramSubject> programSubjects = new ArrayList<>();
}
