package br.com.tecprimesolucoes.online_shop_system.infrastructure.controller.mapper;

import br.com.tecprimesolucoes.online_shop_system.application.dto.ProductResponse;
import br.com.tecprimesolucoes.online_shop_system.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductResponseMapper {


    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "descricao", source = "descricao")
    @Mapping(target = "preco", source = "preco")
    @Mapping(target = "estoque", source = "estoque")
    @Mapping(target = "imagem", source = "imagem")
    ProductResponse toResponse(Product product);

}