package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.api;

import br.com.tecprimesolucoes.online_shop_system.application.service.GetProductsUseCaseService;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper.ProductResponseMapper;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client.FakeStoreProductClient;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client.FakeStoreProductGateway;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.mapper.ExternalProductMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIntegrationTest {

    private HttpServer server;

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void shouldExposeProductsEndpointIntegratedWithExternalClient() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/products", exchange -> {
            byte[] body = """
                    [
                      {
                        "id": 5,
                        "title": "Headset",
                        "price": 50.00,
                        "description": "audio",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/headset.jpg"
                      }
                    ]
                    """.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.start();

        String baseUrl = "http://localhost:" + server.getAddress().getPort();
        FakeStoreProductClient client = new FakeStoreProductClient(RestClient.builder(), baseUrl, "/products");
        ExternalProductMapper externalMapper = new ExternalProductMapper() {
        };
        FakeStoreProductGateway gateway = new FakeStoreProductGateway(client, externalMapper);
        GetProductsUseCaseService useCase = new GetProductsUseCaseService(gateway);
        ProductResponseMapper responseMapper = Mappers.getMapper(ProductResponseMapper.class);

        ProductController controller = new ProductController(useCase, responseMapper);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].nome").value("Headset"))
                .andExpect(jsonPath("$[0].preco").value(250.00))
                .andExpect(jsonPath("$[0].estoque").value(10));
    }
}
