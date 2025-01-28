package com.samda.card.game.repository;

import com.samda.card.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepo extends JpaRepository<Game,Integer> {
}
