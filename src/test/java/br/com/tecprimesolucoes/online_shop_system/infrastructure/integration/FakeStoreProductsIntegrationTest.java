package br.com.tecprimesolucoes.online_shop_system.infrastructure.integration;

import br.com.tecprimesolucoes.online_shop_system.application.service.GetProductsUseCaseService;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client.FakeStoreProductClient;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.client.FakeStoreProductGateway;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.IntegrationException;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.dto.FakeStoreProductResponse;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.integration.mapper.ExternalProductMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FakeStoreProductsIntegrationTest {

    private HttpServer server;

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void shouldConsumeProductsEndpointWithExpectedPathAndParseResponse() throws IOException {
        AtomicReference<String> requestedPath = new AtomicReference<>();
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/products", exchange -> {
            requestedPath.set(exchange.getRequestURI().getPath());
            byte[] body = """
                    [
                      {
                        "id": 1,
                        "title": "Fjallraven Backpack",
                        "price": 109.95,
                        "description": "bag",
                        "category": "men's clothing",
                        "image": "https://fakestoreapi.com/img/1.jpg"
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

        List<FakeStoreProductResponse> products = client.getProducts();

        assertEquals("/products", requestedPath.get());
        assertEquals(1, products.size());
        assertEquals(1L, products.getFirst().id());
        assertEquals(new BigDecimal("109.95"), products.getFirst().price());
    }

    @Test
    void shouldIntegrateClientGatewayAndUseCaseForProductsFlow() throws IOException {
        AtomicInteger productsCalls = new AtomicInteger(0);
        AtomicInteger productByIdCalls = new AtomicInteger(0);

        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/products", exchange -> {
            productsCalls.incrementAndGet();
            byte[] body = """
                    [
                      {
                        "id": 10,
                        "title": "Mouse Gamer",
                        "price": 100.00,
                        "description": "mouse",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/mouse.jpg"
                      },
                      {
                        "id": 11,
                        "title": "Teclado Mecanico",
                        "price": 250.50,
                        "description": "keyboard",
                        "category": "electronics",
                        "image": "https://fakestoreapi.com/img/keyboard.jpg"
                      }
                    ]
                    """.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        });
        server.createContext("/products/11", exchange -> {
            productByIdCalls.incrementAndGet();
            byte[] body = """
                    {
                      "id": 11,
                      "title": "Teclado Mecanico",
                      "price": 250.50,
                      "description": "keyboard",
                      "category": "electronics",
                      "image": "https://fakestoreapi.com/img/keyboard.jpg"
                    }
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
        ExternalProductMapper mapper = new ExternalProductMapper() {
        };
        FakeStoreProductGateway gateway = new FakeStoreProductGateway(client, mapper);
        GetProductsUseCaseService useCase = new GetProductsUseCaseService(gateway);

        List<Product> products = useCase.execute();
        Optional<Product> byId = gateway.findById(11L);

        assertEquals(2, products.size());
        assertEquals("Mouse Gamer", products.getFirst().getNome());
        assertEquals(new BigDecimal("500.00"), products.getFirst().getPreco());
        assertEquals(10, products.getFirst().getEstoque());
        assertTrue(byId.isPresent());
        assertEquals("Teclado Mecanico", byId.get().getNome());
        assertEquals(new BigDecimal("1252.50"), byId.get().getPreco());
        assertEquals(1, productsCalls.get());
        assertEquals(1, productByIdCalls.get());
    }

    @Test
    void shouldReturnEmptyWhenExternalReturns404ForProductById() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/products/99", exchange -> exchange.sendResponseHeaders(404, -1));
        server.start();

        String baseUrl = "http://localhost:" + server.getAddress().getPort();
        FakeStoreProductClient client = new FakeStoreProductClient(RestClient.builder(), baseUrl, "/products");

        Optional<FakeStoreProductResponse> result = client.getProductById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowIntegrationExceptionWhenExternalReturnsServerError() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/products", exchange -> exchange.sendResponseHeaders(500, -1));
        server.start();

        String baseUrl = "http://localhost:" + server.getAddress().getPort();
        FakeStoreProductClient client = new FakeStoreProductClient(RestClient.builder(), baseUrl, "/products");

        assertThrows(IntegrationException.class, client::getProducts);
    }
}
