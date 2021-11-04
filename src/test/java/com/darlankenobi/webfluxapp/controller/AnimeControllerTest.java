package com.darlankenobi.webfluxapp.controller;

import com.darlankenobi.webfluxapp.entity.Anime;
import com.darlankenobi.webfluxapp.service.AnimeService;
import com.darlankenobi.webfluxapp.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

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
        BDDMockito.when(animeServiceMock.findAll())
                .thenReturn(Flux.just(anime));


    }

    @Test
    @DisplayName("find All returns a flux of anime")
    public void findAll_ReturnFluxOfAnime_WhenSuccessful(){
        StepVerifier.create(animeController.findall())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

}