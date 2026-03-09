package br.com.tecprimesolucoes.online_shop_system.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class OrderItem {

    private final UUID id;
    private final Long productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final Integer quantity;
    private final BigDecimal lineTotal;

    private OrderItem(UUID id,
                      Long productId,
                      String productName,
                      BigDecimal unitPrice,
                      Integer quantity) {
        validate(productId, productName, unitPrice, quantity);

        this.id = id == null ? UUID.randomUUID() : id;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.lineTotal = calcularLineTotal(unitPrice, quantity);
    }

    public static OrderItem create(Long productId,
                                  String productName,
                                  BigDecimal unitPrice,
                                  Integer quantity) {
        return new OrderItem(null, productId, productName, unitPrice, quantity);
    }

    public static OrderItem restore(UUID id,
                                      Long productId,
                                      String productName,
                                      BigDecimal unitPrice,
                                      Integer quantity) {
        if (id == null) {
            throw new IllegalArgumentException("Id do item é obrigatório para restauração.");
        }
        return new OrderItem(id, productId, productName, unitPrice, quantity);
    }

    private static void validate(Long productId,
                                String productName,
                                BigDecimal unitPrice,
                                Integer quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Id do produto é obrigatório.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (unitPrice == null || unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("Preço unitário é inválido.");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
    }

    private static BigDecimal calcularLineTotal(BigDecimal unitPrice, Integer quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity.longValue()));
    }

    public UUID getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
