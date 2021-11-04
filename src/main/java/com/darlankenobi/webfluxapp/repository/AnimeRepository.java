package com.darlankenobi.webfluxapp.repository;

import com.darlankenobi.webfluxapp.entity.Anime;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends ReactiveMongoRepository<Anime, String> {

}
