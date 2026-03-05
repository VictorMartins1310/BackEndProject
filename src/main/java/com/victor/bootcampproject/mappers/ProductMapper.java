package com.victor.bootcampproject.mappers;

import com.victor.bootcampproject.dto.ProductDTO;
import com.victor.bootcampproject.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductDTO toDto (Product dto);
    @Mapping(target = "productBought", ignore = true)
    @Mapping(target = "productID", ignore = true)
    Product toEntity (ProductDTO entity);
}