package com.pux12.dictionarycreator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pux12.dictionarycreator.model.Etymology;
import com.pux12.dictionarycreator.repository.EtymologyRepository;

@Service
public class EtymologyService {
    @Autowired
    private EtymologyRepository etymologyRepository;


}