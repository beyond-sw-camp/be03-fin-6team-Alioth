package com.alioth.server.domain.dummy.dto.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleListDTO {
    private Long id;
    private String name;

    public SimpleListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}