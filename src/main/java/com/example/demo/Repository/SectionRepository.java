package com.example.demo.Repository;

import com.example.demo.Model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT DISTINCT LOWER(s.name) FROM Section s")
    List<String> findDistinctSectionNamesIgnoreCase();
}
