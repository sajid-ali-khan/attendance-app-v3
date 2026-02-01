package dev.sajid.backend.config;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.normalized.faculty.FacultyRole;
import dev.sajid.backend.repositories.FacultyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class DataSeeder implements ApplicationRunner {
    private final FacultyRepository facultyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(FacultyRepository facultyRepository, BCryptPasswordEncoder passwordEncoder) {
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        final String adminCode = "admin";
        if (facultyRepository.existsByCode(adminCode)) {
            log.info("Admin faculty already exists (code={}), skipping seeding", adminCode);
            return;
        }

        Faculty admin = new Faculty();
        admin.setCode(adminCode);
        admin.setName("Administrator");
        admin.setRole(FacultyRole.ADMIN);
        admin.setPasswordHash(passwordEncoder.encode("1234"));

        facultyRepository.save(admin);
        log.info("Seeded admin faculty with code={}", adminCode);
    }
}
