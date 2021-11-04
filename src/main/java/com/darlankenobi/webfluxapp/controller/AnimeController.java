package com.darlankenobi.webfluxapp.controller;

import com.darlankenobi.webfluxapp.entity.Anime;
import com.darlankenobi.webfluxapp.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("animes")
@Slf4j
public class AnimeController {

    private final AnimeService animeService;


    @GetMapping
    public Flux<Anime> findall(){
        return this.animeService.findAll();
    }

    @PostMapping
    public Mono<Anime> insert(@Valid @RequestBody Anime anime){
        return this.animeService.insert(anime);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> update(@Valid @RequestBody Anime anime){
        return this.animeService.update(anime);
    }

    @GetMapping("/{id}")
    public Mono<Anime> insert(@PathVariable String id){
        return this.animeService.findById(id);
    }




}
