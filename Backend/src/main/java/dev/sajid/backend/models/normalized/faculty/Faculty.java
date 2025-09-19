package dev.sajid.backend.models.normalized.faculty;

import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String code;

    private String name;
    private String gender;
    private String salutation;

    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<CourseAssignment> courseAssignments;

//    the list of sessions is not mapped because the sessions belong to course. Think of the lab scenarios, where anyone can take the attendance, but also everyone assigned the course should see all the sessions.
}
