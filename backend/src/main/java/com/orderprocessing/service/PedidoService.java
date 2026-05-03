package com.orderprocessing.service;

import com.orderprocessing.dto.PedidoDTO;
import com.orderprocessing.dto.PedidoResponseDTO;
import com.orderprocessing.entity.ItemPedido;
import com.orderprocessing.entity.Pedido;
import com.orderprocessing.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public PedidoResponseDTO salvar(String textoOriginal, PedidoDTO pedidoDTO) {
        Pedido pedido = Pedido.builder()
                .cliente(pedidoDTO.getCliente())
                .dataEntrega(pedidoDTO.getDataEntrega())
                .textoOriginal(textoOriginal)
                .build();

        List<ItemPedido> itens = pedidoDTO.getItens().stream()
                .map(itemDTO -> ItemPedido.builder()
                        .produto(itemDTO.getProduto())
                        .quantidade(itemDTO.getQuantidade())
                        .pedido(pedido)
                        .build())
                .collect(Collectors.toList());

        pedido.setItens(itens);

        Pedido saved = pedidoRepository.save(pedido);

        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + id));
        return toResponseDTO(pedido);
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<PedidoResponseDTO.ItemPedidoDTO> itensDTO = pedido.getItens().stream()
                .map(item -> PedidoResponseDTO.ItemPedidoDTO.builder()
                        .id(item.getId())
                        .produto(item.getProduto())
                        .quantidade(item.getQuantidade())
                        .build())
                .collect(Collectors.toList());

        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .cliente(pedido.getCliente())
                .dataEntrega(pedido.getDataEntrega())
                .textoOriginal(pedido.getTextoOriginal())
                .itens(itensDTO)
                .criadoEm(pedido.getCriadoEm())
                .build();
    }
}
