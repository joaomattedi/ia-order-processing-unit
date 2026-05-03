package com.orderprocessing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderprocessing.dto.PedidoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIParserService {

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";

    public PedidoDTO parsear(String texto) {
        String prompt = buildPrompt(texto);
        String responseText = callGemini(prompt);
        return parseResponse(responseText);
    }

    private String buildPrompt(String texto) {
        return String.format("""
                Extraia as informações do pedido abaixo e retorne SOMENTE um JSON válido, sem markdown, sem explicações.
                Formato esperado:
                {
                  "cliente": "nome do cliente ou 'desconhecido' se não mencionado",
                  "itens": [{"produto": "nome do produto", "quantidade": número}],
                  "data_entrega": "YYYY-MM-DD"
                }
                Hoje é %s. Interprete datas relativas como 'amanhã', 'hoje', 'semana que vem' com base nessa data.
                Pedido: %s
                """, LocalDate.now(), texto);
    }

    private String callGemini(String prompt) {
        try {
            String body = objectMapper.writeValueAsString(
                Map.of("contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", prompt)
                    ))
                ))
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL))
                    .header("Content-Type", "application/json")
                    .header("X-goog-api-key", geminiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            return root.at("/candidates/0/content/parts/0/text").asText();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao chamar Gemini API: " + e.getMessage(), e);
        }
    }

    private PedidoDTO parseResponse(String jsonText) {
        try {
            String clean = jsonText.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();
            JsonNode node = objectMapper.readTree(clean);

            List<PedidoDTO.ItemDTO> itens = new ArrayList<>();
            for (JsonNode item : node.get("itens")) {
                itens.add(PedidoDTO.ItemDTO.builder()
                        .produto(item.get("produto").asText())
                        .quantidade(item.get("quantidade").asInt())
                        .build());
            }

            return PedidoDTO.builder()
                    .cliente(node.get("cliente").asText("desconhecido"))
                    .dataEntrega(LocalDate.parse(node.get("data_entrega").asText()))
                    .itens(itens)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Falha ao parsear resposta da IA: " + e.getMessage(), e);
        }
    }

    /*
    // Mock - kept for reference
    public PedidoDTO parsearMock(String texto) {
        return PedidoDTO.builder()
                .cliente("desconhecido")
                .dataEntrega(LocalDate.now().plusDays(1))
                .itens(List.of(
                        PedidoDTO.ItemDTO.builder().produto("leite integral").quantidade(10).build(),
                        PedidoDTO.ItemDTO.builder().produto("água").quantidade(5).build()
                ))
                .build();
    }
    */
}
