package br.com.tecprimesolucoes.online_shop_system.domain.model;

import br.com.tecprimesolucoes.online_shop_system.domain.enums.PaymentMethod;
import br.com.tecprimesolucoes.online_shop_system.infrastructure.exception.InvalidPaymentMethodException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final String orderNumber;
    private final String customerName;
    private final String customerEmail;
    private final String customerAddress;
    private final PaymentMethod paymentMethod;
    private final BigDecimal totalAmount;
    private final String status;
    private final LocalDateTime createdAt;
    private final List<OrderItem> items;

    private Order(UUID id,
                  String orderNumber,
                  String customerName,
                  String customerEmail,
                  String customerAddress,
                  PaymentMethod paymentMethod,
                  String status,
                  LocalDateTime createdAt,
                  List<OrderItem> items) {
        validate(orderNumber, customerName, customerEmail, customerAddress, paymentMethod, items);

        this.id = id == null ? UUID.randomUUID() : id;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.items = List.copyOf(items);
        this.totalAmount = calcularTotal(items);
    }

    public static Order create(String orderNumber,
                              String customerName,
                              String customerEmail,
                              String customerAddress,
                              PaymentMethod paymentMethod,
                              List<OrderItem> items) {
        return new Order(
                null,
                orderNumber,
                customerName,
                customerEmail,
                customerAddress,
                paymentMethod,
                "CREATED",
                null,
                items
        );
    }

    public static Order restore(UUID id,
                                  String orderNumber,
                                  String customerName,
                                  String customerEmail,
                                  String customerAddress,
                                  PaymentMethod paymentMethod,
                                  String status,
                                  LocalDateTime createdAt,
                                  List<OrderItem> items) {
        return new Order(
                id,
                orderNumber,
                customerName,
                customerEmail,
                customerAddress,
                paymentMethod,
                status,
                createdAt,
                items
        );
    }

    private static void validate(String orderNumber,
                                String customerName,
                                String customerEmail,
                                String customerAddress,
                                PaymentMethod paymentMethod,
                                List<OrderItem> items) {
        if (orderNumber == null || orderNumber.isBlank()) {
            throw new IllegalArgumentException("Número do pedido é obrigatório.");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new IllegalArgumentException("E-mail do cliente é obrigatório.");
        }
        if (customerAddress == null || customerAddress.isBlank()) {
            throw new IllegalArgumentException("Endereço do cliente é obrigatório.");
        }
        if (paymentMethod == null) {
            throw new InvalidPaymentMethodException();
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve possuir ao menos um item.");
        }
    }

    private static BigDecimal calcularTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}