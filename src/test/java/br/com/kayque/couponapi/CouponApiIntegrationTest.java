package br.com.kayque.couponapi;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponApiIntegrationTest {

    @Autowired
    MockMvc mvc;
    
    @Autowired
    ObjectMapper mapper;

    // ===================== TESTES DE CRIAÇÃO DE CUPONS ======================

    @Test
    void should_create_coupon_with_all_required_fields() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "ABC123");
        body.put("description", "Cupom de teste");
        body.put("discountValue", 10.5);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());
        body.put("published", false);

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.code", is("ABC123")))
            .andExpect(jsonPath("$.description", is("Cupom de teste")))
            .andExpect(jsonPath("$.status", is("INACTIVE")))
            .andExpect(jsonPath("$.published", is(false)))
            .andExpect(jsonPath("$.redeemed", is(false)));
    }

    @Test
    void should_create_coupon_as_published() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "PUB123");
        body.put("description", "Cupom publicado");
        body.put("discountValue", 5.0);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());
        body.put("published", true);

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status", is("ACTIVE")))
            .andExpect(jsonPath("$.published", is(true)));
    }

    @Test
    void should_normalize_code_removing_special_characters() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "A@1-2#B$3C"); // Após normalização: A12B3C (6 chars)
        body.put("description", "Teste normalização");
        body.put("discountValue", 10.5);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code", is("A12B3C")));
    }

    @Test
    void should_reject_code_with_less_than_6_alphanumeric_characters() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "@@!!"); // Após normalização: "" (0 chars)
        body.put("description", "Teste inválido");
        body.put("discountValue", 10.5);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        System.out.println("=== TESTE: Código com caracteres especiais ===");
        System.out.println("Input code: @@!!");
        System.out.println("Esperado após normalização: '' (vazio)");
        System.out.println("Esperado: HTTP 400 Bad Request");

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andDo(result -> System.out.println("Response Status: " + result.getResponse().getStatus()))
            .andDo(result -> System.out.println("Response Body: " + result.getResponse().getContentAsString()))
            .andExpect(status().isBadRequest());
        
        System.out.println("=== TESTE PASSOU! ===\n");
    }

    @Test
    void should_reject_code_with_more_than_6_alphanumeric_characters() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "ABCDEFGH"); // 8 caracteres alfanuméricos
        body.put("description", "Teste inválido");
        body.put("discountValue", 10.5);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_reject_discount_value_below_minimum() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "MIN001");
        body.put("description", "Desconto muito baixo");
        body.put("discountValue", 0.4); // Mínimo é 0.5
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_accept_minimum_discount_value() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "MIN002");
        body.put("description", "Desconto mínimo aceito");
        body.put("discountValue", 0.5); // Exatamente o mínimo
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated());
    }

    @Test
    void should_reject_expiration_date_in_the_past() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "PAST01");
        body.put("description", "Data no passado");
        body.put("discountValue", 10.0);
        body.put("expirationDate", Instant.now().minus(1, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_reject_missing_code() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("description", "Sem código");
        body.put("discountValue", 10.0);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_reject_missing_description() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "NODSCR");
        body.put("discountValue", 10.0);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_reject_missing_discount_value() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "NODISC");
        body.put("description", "Sem desconto");
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_reject_missing_expiration_date() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("code", "NOEXP1");
        body.put("description", "Sem expiração");
        body.put("discountValue", 10.0);

        mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isBadRequest());
    }

    // ===================== TESTES DE EXCLUSÃO DE CUPONS =====================

    @Test
    void should_soft_delete_coupon() throws Exception {
        // Criar cupom
        Map<String, Object> body = new HashMap<>();
        body.put("code", "DEL001");
        body.put("description", "Cupom para deletar");
        body.put("discountValue", 10.0);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());
        body.put("published", true);

        String response = mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String id = mapper.readTree(response).get("id").asText();

        // Deletar cupom
        mvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        // Verificar que ainda é possível consultar (soft delete)
        mvc.perform(get("/coupon/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("DELETED")));
    }

    @Test
    void should_reject_double_delete() throws Exception {
        // Criar cupom
        Map<String, Object> body = new HashMap<>();
        body.put("code", "DEL002");
        body.put("description", "Cupom para duplo delete");
        body.put("discountValue", 10.0);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());

        String response = mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String id = mapper.readTree(response).get("id").asText();

        // Primeiro delete - OK
        mvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isNoContent());

        // Segundo delete - deve retornar 409 Conflict
        mvc.perform(delete("/coupon/{id}", id))
            .andExpect(status().isConflict());
    }

    // ===================== TESTES DE CONSULTA DE CUPONS =====================

    @Test
    void should_get_coupon_by_id() throws Exception {
        // Criar cupom
        Map<String, Object> body = new HashMap<>();
        body.put("code", "GET001");
        body.put("description", "Cupom para buscar");
        body.put("discountValue", 15.0);
        body.put("expirationDate", Instant.now().plus(30, ChronoUnit.DAYS).toString());
        body.put("published", true);

        String response = mvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        String id = mapper.readTree(response).get("id").asText();

        // Buscar cupom
        mvc.perform(get("/coupon/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(id)))
            .andExpect(jsonPath("$.code", is("GET001")))
            .andExpect(jsonPath("$.description", is("Cupom para buscar")))
            .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    void should_return_404_for_non_existent_coupon() throws Exception {
        mvc.perform(get("/coupon/{id}", "00000000-0000-0000-0000-000000000000"))
            .andExpect(status().isNotFound());
    }
}
