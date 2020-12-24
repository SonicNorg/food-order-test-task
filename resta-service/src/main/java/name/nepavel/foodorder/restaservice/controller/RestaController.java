package name.nepavel.foodorder.restaservice.controller;

import lombok.RequiredArgsConstructor;
import name.nepavel.foodorder.restaapi.api.RestaurantApi;
import name.nepavel.foodorder.restaapi.model.FoodItem;
import name.nepavel.foodorder.restaapi.model.OrderItem;
import name.nepavel.foodorder.restaapi.model.OrderResponse;
import name.nepavel.foodorder.restaservice.service.RestaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaController implements RestaurantApi {
    private final RestaService restaService;

    @Override
    public ResponseEntity<List<FoodItem>> getMenu() {
        return ResponseEntity.ok(restaService.list());
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok(restaService.getOrders());
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderById(Integer id) {
        return ResponseEntity.ok(restaService.getOrder(id));
    }

    @Override
    public ResponseEntity<OrderResponse> createOrder(@Valid List<OrderItem> orderItem) {
        return ResponseEntity.ok(restaService.order(orderItem));
    }

}
