package dev.sajid.backend.models.normalized.course;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "programs")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schemes")
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String code;

    private int year;

    @OneToMany(mappedBy = "scheme", fetch = FetchType.LAZY)
    private List<Program> programs = new ArrayList<>();
}
