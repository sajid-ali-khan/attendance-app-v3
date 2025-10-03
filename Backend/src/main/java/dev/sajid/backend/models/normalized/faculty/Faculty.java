package dev.sajid.backend.models.normalized.faculty;

import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private int code;

    private String name;

    private String gender;

    @JsonIgnore
    private String salutation;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "faculty", fetch = FetchType.LAZY)
    private List<CourseAssignment> courseAssignments;

//    the list of sessions is not mapped because the sessions belong to course. Think of the lab scenarios, where anyone can take the attendance, but also everyone assigned the course should see all the sessions.
}
