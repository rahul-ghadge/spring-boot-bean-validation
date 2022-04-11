package com.spring.validation.demo.exception;

public class SuperHeroNotFound extends RuntimeException {
    public SuperHeroNotFound(String errMessage) {
        super(errMessage);
    }
}
