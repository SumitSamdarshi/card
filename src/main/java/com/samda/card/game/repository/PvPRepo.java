package com.samda.card.game.repository;

import com.samda.card.game.entity.PvP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PvPRepo extends JpaRepository<PvP,Integer> {
}
