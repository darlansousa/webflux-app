package com.darlankenobi.webfluxapp.service;

import com.darlankenobi.webfluxapp.entity.Anime;
import com.darlankenobi.webfluxapp.repository.AnimeRepository;
import com.darlankenobi.webfluxapp.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepository;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockhoundSetup(){

        BlockHound.install();
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0); //NOSONAR
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @BeforeEach
    public void setUp(){
        BDDMockito.when(animeRepository.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(anime));
    }

    @Test
    @DisplayName("find All returns a flux of anime")
    public void findAll_ReturnFluxOfAnime_WhenSuccessful(){
        StepVerifier.create(animeService.findAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }


    @Test
    @DisplayName("findById returns Mono with anime when it exists")
    public void findById_ReturnMonoOfAnime_WhenExists(){
        StepVerifier.create(animeService.findById("1"))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns Mono with anime does not exists")
    public void findById_ReturnMonoOfAnime_WhenEmptyMono(){
        BDDMockito.when(animeRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(animeService.findById("1"))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("save create an Anime when successful")
    public void save_CreateAnAnimeWhenSuccessful(){

        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();

        BDDMockito.when(animeRepository.insert(animeToBeSaved))
                .thenReturn(Mono.just(anime));


        StepVerifier.create(animeService.insert(animeToBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }




}