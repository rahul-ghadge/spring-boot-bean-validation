package com.spring.validation.demo.service.impl;

import com.spring.validation.demo.dto.SuperHeroDto;
import com.spring.validation.demo.entities.SuperHero;
import com.spring.validation.demo.exception.SuperHeroNotFound;
import com.spring.validation.demo.repository.SuperHeroRepository;
import com.spring.validation.demo.service.SuperHeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperHeroServiceImpl implements SuperHeroService {

    private final SuperHeroRepository superHeroRepository;

    @Override
    public SuperHeroDto save(SuperHeroDto superHeroDto) {
        SuperHero superHero = new SuperHero();
        BeanUtils.copyProperties(superHeroDto, superHero);

        superHero = superHeroRepository.save(superHero);

        superHeroDto.setId(superHero.getId());

        return superHeroDto;
    }

    @Override
    public List<SuperHeroDto> findAll() {
        List<SuperHeroDto> superHeroDtoList = new ArrayList<>();
        List<SuperHero> superHeroes = superHeroRepository.findAll();
        if (!CollectionUtils.isEmpty(superHeroes)) {
            superHeroes.forEach(superHero -> {
                SuperHeroDto superHeroDto = new SuperHeroDto();
                BeanUtils.copyProperties(superHero, superHeroDto);
                superHeroDtoList.add(superHeroDto);
            });
        }
        return superHeroDtoList;
    }

    @Override
    public SuperHeroDto findById(int id) {
        SuperHero superHero = superHeroRepository.findById(id)
                .orElseThrow(() -> new SuperHeroNotFound("Super hero not found for id: " + id));
        SuperHeroDto superHeroDto = new SuperHeroDto();
        BeanUtils.copyProperties(superHero, superHeroDto);
        return superHeroDto;
    }

    @Override
    public SuperHeroDto update(int id, SuperHeroDto superHeroDto) {
        SuperHero superHero = superHeroRepository.findById(id)
                .orElseThrow(() -> new SuperHeroNotFound("Super hero not found for id: " + id));

        BeanUtils.copyProperties(superHeroDto, superHero);
        superHero = superHeroRepository.save(superHero);
        superHeroDto.setId(superHero.getId());
        return superHeroDto;    }

    @Override
    public void delete(int id) {
        SuperHero superHero = superHeroRepository.findById(id)
                .orElseThrow(() -> new SuperHeroNotFound("Super hero not found for id: " + id));

        superHeroRepository.delete(superHero);
    }

}
