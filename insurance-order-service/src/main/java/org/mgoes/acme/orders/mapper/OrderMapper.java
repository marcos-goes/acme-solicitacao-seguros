package org.mgoes.acme.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mgoes.acme.orders.model.Assistance;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.openapi.model.OrderCreate;
import org.mgoes.acme.orders.openapi.model.OrderCreateResponse;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class );

    OrderCreateResponse toOrderCreateResponse(Order order);

    Order toOrderEntitySimple(OrderCreate dto);

    default List<Assistance> toAssistenceList(List<String> assistances){
        return assistances.stream()
                .map(assist -> new Assistance(null, assist, null))
                .toList();
    }

    default Order toOrderEntity(OrderCreate dto){
        var order = toOrderEntitySimple(dto);
        for (var item : order.getHistory()) item.setOrder(order);
        for (var assist : order.getAssistances()) assist.setOrder(order);
        for (var coverage : order.getCoverages()) coverage.setOrder(order);
        return order;
    }

//    @Mapping(target = "history[].timestamp", source = "history[].registered" )
    org.mgoes.acme.orders.openapi.model.Order toApiOrder(Order order);

    default List<String> toStringList(List<Assistance> assistances){
        return assistances.stream()
                .map(Assistance::getName)
                .toList();
    }
}
