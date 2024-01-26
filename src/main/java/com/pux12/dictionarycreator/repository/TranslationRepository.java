package com.pux12.dictionarycreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pux12.dictionarycreator.model.Translation;

public interface TranslationRepository extends JpaRepository<Translation, Long> {

}
