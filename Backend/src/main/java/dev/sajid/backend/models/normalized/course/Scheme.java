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
