package com.pulawskk.dbsorder.api.v1.mapper;

import com.pulawskk.dbsorder.api.v1.model.OrderDto;
import com.pulawskk.dbsorder.domain.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

//    @Mapping(source = "userId", target = "userId")
    OrderDto orderToOrderDto(Order order);

    @Mappings({
            @Mapping(source = "userId", target = "userId"),
            @Mapping(source = "burgerId", target = "burgerId")
    })
    Order orderDtoToOrder(OrderDto orderDto);
}
