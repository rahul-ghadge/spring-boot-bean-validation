package com.spring.validation.demo.service;

import com.spring.validation.demo.dto.SuperHeroDto;

import java.util.List;

public interface SuperHeroService {
    SuperHeroDto save(SuperHeroDto superHeroDto);

    List<SuperHeroDto> findAll();

    SuperHeroDto findById(int id);
}
