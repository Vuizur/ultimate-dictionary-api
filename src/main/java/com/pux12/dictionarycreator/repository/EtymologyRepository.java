package com.pux12.dictionarycreator.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pux12.dictionarycreator.model.Etymology;

public interface EtymologyRepository extends JpaRepository<Etymology, Long> {
    List<Etymology> findByWord(String word);

    // find by source wiktionary code, paginated
    Page<Etymology> findBySourceWiktionaryCode(String source_wiktionary_code, Pageable pageable);

}
