package br.com.tecprimesolucoes.online_shop_system.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final BigDecimal preco;
    private final Integer estoque;
    private final String imagem;

    private Product(Long id,
                    String nome,
                    String descricao,
                    BigDecimal preco,
                    Integer estoque,
                    String imagem) {
        validate(id, nome, preco, estoque);

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.imagem = imagem;
    }

    public static Product restore(Long id,
                                    String nome,
                                    String descricao,
                                    BigDecimal preco,
                                    Integer estoque,
                                    String imagem) {
        return new Product(id, nome, descricao, preco, estoque, imagem);
    }

    private static void validate(Long id, String nome, BigDecimal preco, Integer estoque) {
        if (id == null) {
            throw new IllegalArgumentException("Id do produto é obrigatório.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (preco == null) {
            throw new IllegalArgumentException("Preço do produto é obrigatório.");
        }
        if (estoque == null || estoque < 0) {
            throw new IllegalArgumentException("Estoque do produto é inválido.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public String getImagem() {
        return imagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}