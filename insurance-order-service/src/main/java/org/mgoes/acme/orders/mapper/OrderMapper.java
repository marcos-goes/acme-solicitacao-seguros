package org.mgoes.acme.orders.mapper;

import org.mapstruct.Mapper;
import org.mgoes.acme.orders.model.Assistance;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.openapi.model.OrderCreate;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrderEntity(OrderCreate orderCreate);

    default List<Assistance> toAssistenceList(List<String> assistances){
        return assistances.stream()
                .map(assist -> new Assistance(null, assist, null))
                .toList();
    }
}
