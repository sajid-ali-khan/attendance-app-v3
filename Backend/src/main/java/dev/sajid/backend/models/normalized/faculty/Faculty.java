package dev.sajid.backend.models.normalized.faculty;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.sajid.backend.models.normalized.derived.CourseAssignment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Enumerated(EnumType.STRING)
    private FacultyRole role = FacultyRole.TEACHER;

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
