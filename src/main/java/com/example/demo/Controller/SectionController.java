package com.example.demo.Controller;

import com.example.demo.Model.Section;
import com.example.demo.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping
    public ResponseEntity<Section> addSection(@RequestBody Section section) {
        Section newSection = sectionService.addSection(section);
        return ResponseEntity.ok(newSection);
    }

    @GetMapping
    public ResponseEntity<List<Section>> getAllSections() {
        List<Section> sections = sectionService.getAllSections();
        return ResponseEntity.ok(sections);
    }
}
