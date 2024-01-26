package com.pux12.dictionarycreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pux12.dictionarycreator.model.Sense;

public interface SenseRepository extends JpaRepository<Sense, Long> {

}
