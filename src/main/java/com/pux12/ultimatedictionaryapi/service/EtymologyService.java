package com.pux12.ultimatedictionaryapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pux12.ultimatedictionaryapi.repository.EtymologyRepository;

@Service
public class EtymologyService {
    @Autowired
    private EtymologyRepository etymologyRepository;


}