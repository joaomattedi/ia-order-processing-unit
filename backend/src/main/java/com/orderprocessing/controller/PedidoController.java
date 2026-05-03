package com.orderprocessing.controller;

import com.orderprocessing.dto.PedidoDTO;
import com.orderprocessing.dto.PedidoRequestDTO;
import com.orderprocessing.dto.PedidoResponseDTO;
import com.orderprocessing.service.AIParserService;
import com.orderprocessing.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoController {

    private final AIParserService aiParserService;
    private final PedidoService pedidoService;

    @PostMapping("/pedido")
    public ResponseEntity<PedidoResponseDTO> criarPedido(@Valid @RequestBody PedidoRequestDTO request) {
        PedidoDTO pedidoDTO = aiParserService.parsear(request.getTexto());
        PedidoResponseDTO response = pedidoService.salvar(request.getTexto(), pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
}
