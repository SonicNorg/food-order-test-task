package name.nepavel.foodorder.restaservice.mapper;

import name.nepavel.foodorder.restaapi.model.OrderResponse;
import name.nepavel.foodorder.restaapi.model.OrderResponseFood;
import name.nepavel.foodorder.restaservice.repository.OrderEntity;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    List<OrderResponse> mapResponse(List<OrderEntity> entities);

    default OrderResponse mapResponse(OrderEntity entity) {
        return new OrderResponse()
                .date(entity.getCreatedAt())
                .id(entity.getOrderId())
                .price(BigDecimal.valueOf(entity.getPrice()))
                .food(entity.getFoods().entrySet().stream().map(
                        foodEntry -> new OrderResponseFood().count(foodEntry.getValue()).foodItem(FoodMapper.MAPPER.map(foodEntry.getKey()))
                ).collect(Collectors.toList()));
    }
}
