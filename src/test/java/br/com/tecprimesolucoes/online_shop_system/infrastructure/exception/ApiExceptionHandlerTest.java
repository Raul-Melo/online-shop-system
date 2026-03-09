package br.com.tecprimesolucoes.online_shop_system.infrastructure.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new ApiExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldHandleDomainExceptionWithStandardApiError() throws Exception {
        mockMvc.perform(post("/test/domain").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.code").value("EXTERNAL_API_ERROR"))
                .andExpect(jsonPath("$.message").value("Erro ao consumir catálogo externo de produtos."))
                .andExpect(jsonPath("$.path").value("/test/domain"));
    }

    @Test
    void shouldHandleValidationExceptionWithExpectedCode() throws Exception {
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALID-001"))
                .andExpect(jsonPath("$.path").value("/test/validation"));
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @PostMapping("/domain")
        void throwDomain() {
            throw new IntegrationException();
        }

        @PostMapping("/validation")
        void validate(@Valid @RequestBody Request request) {
        }
    }

    record Request(@NotBlank(message = "não pode ser vazio") String name) {
    }
}

