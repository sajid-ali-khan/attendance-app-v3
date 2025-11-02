package dev.sajid.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.sajid.backend.models.normalized.course.Scheme;

public interface SchemeRepository extends JpaRepository<Scheme, Integer>{
    Optional<Scheme> findByCode(String code);

    boolean existsByCode(String schemeCode);

    @Query("select s.id from Scheme s where s.code = :code")
    Integer findIdByCode(@Param("code") String code);

    @Query("select s.code from Scheme s")
    List<String> findAllCodes();
}
