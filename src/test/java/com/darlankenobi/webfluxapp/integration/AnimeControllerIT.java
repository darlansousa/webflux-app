package com.darlankenobi.webfluxapp.integration;

import com.darlankenobi.webfluxapp.entity.Anime;
import com.darlankenobi.webfluxapp.repository.AnimeRepository;
import com.darlankenobi.webfluxapp.service.AnimeService;
import com.darlankenobi.webfluxapp.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith({SpringExtension.class})
@WebFluxTest
@Import(AnimeService.class)
public class AnimeControllerIT {

    @MockBean
    private AnimeRepository animeRepositoryMock;

    @Autowired
    private WebTestClient testClient;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockhoundSetup(){

        BlockHound.install();
    }

    @BeforeEach
    public void setUp(){
        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(Flux.just(anime));
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

    @Test
    @DisplayName("When request GET to /animes then returns animes")
    public void whenRequestGETtoAnimesThemReturnsAnimes(){

        testClient.get()
                .uri("/animes")
                .exchange()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(anime.getId())
                .jsonPath("$.[0].name").isEqualTo(anime.getName());

    }


    @Test
    @DisplayName("When request GET to /animes then returns animes Option 2")
    public void whenRequestGETtoAnimesThemReturnsAnimesOption2(){

        testClient.get()
                .uri("/animes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Anime.class)
                .hasSize(1)
                .contains(anime);

    }

    @Test
    @DisplayName("When request GET to /animes/{id} then returns anime")
    public void whenRequestGETtoAnimesIdThemReturnsAnime(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(anime));

        testClient.get()
                .uri("/animes/{id}", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Anime.class)
                .isEqualTo(anime);

    }

    @Test
    @DisplayName("When request GET to /animes/{id} then returns anime")
    public void whenRequestGETtoAnimesIdNonexistentThemReturnsError(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        testClient.get()
                .uri("/animes/{id}", 2)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404);
    }











}
