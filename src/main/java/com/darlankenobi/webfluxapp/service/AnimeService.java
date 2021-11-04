package com.darlankenobi.webfluxapp.service;


import com.darlankenobi.webfluxapp.entity.Anime;
import com.darlankenobi.webfluxapp.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public Flux<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Mono<Anime> insert(Anime anime){
        return animeRepository.insert(anime);
    }

    public Mono<Anime> findById(String id){
        return animeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")));
    }
    public Mono<Void> update(Anime anime){
        return findById(anime.getId())
                .map(animeFromBase -> animeFromBase.withName(anime.getName()))
                .flatMap(this::update)
                .thenEmpty(Mono.empty());
    }
}
