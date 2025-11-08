package dev.sajid.backend.config;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.models.normalized.faculty.FacultyRole;
import dev.sajid.backend.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Check if admin already exists
        if (facultyRepository.findByUsername("admin@gprec.ac.in").isPresent()) {
            return;
        }

        Faculty admin = new Faculty();
        admin.setName("System Admin");
        admin.setCode("admin@gprec.ac.in");
        admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
        admin.setRole(FacultyRole.ADMIN); // if you have roles

        facultyRepository.save(admin);
        System.out.println("✅ Default admin user created: admin@gprec.ac.in");
    }
}

