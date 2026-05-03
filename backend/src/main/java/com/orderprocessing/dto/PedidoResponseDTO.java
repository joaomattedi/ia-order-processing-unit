package com.orderprocessing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cliente")
    private String cliente;

    @JsonProperty("data_entrega")
    private LocalDate dataEntrega;

    @JsonProperty("texto_original")
    private String textoOriginal;

    @JsonProperty("itens")
    private List<ItemPedidoDTO> itens;

    @JsonProperty("criado_em")
    private LocalDateTime criadoEm;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("produto")
        private String produto;

        @JsonProperty("quantidade")
        private Integer quantidade;
    }
}
