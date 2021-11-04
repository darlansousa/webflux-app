package com.darlankenobi.webfluxapp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@Data
public class Anime {

    @Id
    private String id;
    @NotNull
    @NotEmpty(message = "The name of this anime cannot be empty")
    private String name;

}
