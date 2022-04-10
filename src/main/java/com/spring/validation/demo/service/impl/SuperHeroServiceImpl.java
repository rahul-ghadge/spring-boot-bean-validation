package com.spring.validation.demo.service.impl;

import com.spring.validation.demo.dto.SuperHeroDto;
import com.spring.validation.demo.entities.SuperHero;
import com.spring.validation.demo.repository.SuperHeroRepository;
import com.spring.validation.demo.service.SuperHeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                BeanUtils.copyProperties(superHeroDto, superHero);
                superHeroDtoList.add(superHeroDto);
            });
        }
        return superHeroDtoList;
    }

    @Override
    public SuperHeroDto findById(int id) {
        Optional<SuperHero> superHeroOptional = superHeroRepository.findById(id);
        SuperHeroDto superHeroDto = new SuperHeroDto();
        superHeroOptional.ifPresent(superHero -> BeanUtils.copyProperties(superHeroDto, superHero));
        return null;
    }

}
