package name.nepavel.foodorder.restaservice.service;

import lombok.RequiredArgsConstructor;
import name.nepavel.foodorder.restaapi.model.FoodItem;
import name.nepavel.foodorder.restaapi.model.OrderItem;
import name.nepavel.foodorder.restaapi.model.OrderResponse;
import name.nepavel.foodorder.restaservice.mapper.FoodMapper;
import name.nepavel.foodorder.restaservice.mapper.OrderMapper;
import name.nepavel.foodorder.restaservice.repository.AssortmentRepository;
import name.nepavel.foodorder.restaservice.repository.FoodEntity;
import name.nepavel.foodorder.restaservice.repository.OrderEntity;
import name.nepavel.foodorder.restaservice.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
@Transactional
public class RestaService {
    private final AssortmentRepository assortmentRepository;
    private final OrderRepository orderRepository;
    private final FoodMapper foodMapper;
    private final OrderMapper orderMapper;


    public List<FoodItem> list() {
        return assortmentRepository.findAll().stream()
                .map(foodMapper::map)
                .collect(toList());
    }

    public OrderResponse order(List<OrderItem> orderItem) {
        Map<Integer, Integer> idsCounts = orderItem.stream().collect(toMap(
                orderItem1 -> orderItem1.getId(), orderItem1 -> orderItem1.getCount())
        );
        Map<FoodEntity, Integer> items = assortmentRepository.findAll().stream()
                .filter(it -> idsCounts.containsKey(it.getFoodId()))
                .collect(toMap(
                        orderItem1 -> orderItem1, orderItem1 -> idsCounts.get(orderItem1.getFoodId()))
                );
        OrderEntity orderEntity = new OrderEntity()
                .setCreatedAt(OffsetDateTime.now())
                .setFoods(items);
        orderEntity.setOrderId(orderRepository.save(orderEntity));
        return orderMapper.mapResponse(orderEntity);
    }

    public List<OrderResponse> getOrders() {
        return orderMapper.mapResponse(orderRepository.list());
    }

    public OrderResponse getOrder(Integer id) {
        return orderMapper.mapResponse(orderRepository.getById(id));
    }
}
