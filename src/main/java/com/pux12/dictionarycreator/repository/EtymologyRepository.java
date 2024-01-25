package com.pux12.dictionarycreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pux12.dictionarycreator.model.Etymology;

public interface EtymologyRepository extends JpaRepository<Etymology, Long> {
    Etymology findByWord(String word);
}
