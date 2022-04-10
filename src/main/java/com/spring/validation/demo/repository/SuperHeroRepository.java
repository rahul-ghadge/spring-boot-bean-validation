package com.spring.validation.demo.repository;

import com.spring.validation.demo.entities.SuperHero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperHeroRepository extends JpaRepository<SuperHero, Integer> {
}
