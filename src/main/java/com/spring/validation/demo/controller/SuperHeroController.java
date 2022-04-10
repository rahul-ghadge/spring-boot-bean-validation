package com.spring.validation.demo.controller;

import com.spring.validation.demo.dto.SuperHeroDto;
import com.spring.validation.demo.service.SuperHeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/superhero")
@RequiredArgsConstructor
public class SuperHeroController {

    private final SuperHeroService superHeroService;

    @PostMapping
    public ResponseEntity<SuperHeroDto> save(@RequestBody @Valid SuperHeroDto superHeroDto) {
        return ResponseEntity.ok(superHeroService.save(superHeroDto));
    }

    @GetMapping
    public ResponseEntity<List<SuperHeroDto>> findAll() {
        return ResponseEntity.ok(superHeroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuperHeroDto> findById(@PathVariable int id) {
        return ResponseEntity.ok(superHeroService.findById(id));
    }

}
