package com.pux12.dictionarycreator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pux12.dictionarycreator.model.Form;

public interface FormRepository extends JpaRepository<Form, Long> {

}