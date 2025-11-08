package dev.sajid.backend.services.auth;

import dev.sajid.backend.models.normalized.faculty.Faculty;
import dev.sajid.backend.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FacultyDetailsService implements UserDetailsService {
    FacultyRepository facultyRepository;

    public FacultyDetailsService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Faculty> faculty = facultyRepository.findByUsername(username);
        if (faculty.isEmpty())
            throw new UsernameNotFoundException("The username " + username + " not found.");
        return faculty.get();
    }
}
