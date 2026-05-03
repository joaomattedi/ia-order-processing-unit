package com.orderprocessing.service;

import com.orderprocessing.dto.PedidoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AIParserService {

    // TODO: replace with real AI API call
    public PedidoDTO parsear(String texto) {
        return PedidoDTO.builder()
                .cliente("desconhecido")
                .dataEntrega(LocalDate.now().plusDays(1))
                .itens(List.of(
                        PedidoDTO.ItemDTO.builder()
                                .produto("leite integral")
                                .quantidade(10)
                                .build(),
                        PedidoDTO.ItemDTO.builder()
                                .produto("água")
                                .quantidade(5)
                                .build()
                ))
                .build();
    }
}
