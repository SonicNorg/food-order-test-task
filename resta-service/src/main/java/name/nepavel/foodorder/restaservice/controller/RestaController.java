package name.nepavel.foodorder.restaservice.controller;

import lombok.RequiredArgsConstructor;
import name.nepavel.foodorder.restaapi.model.FoodItem;
import name.nepavel.foodorder.restaapi.model.OrderItem;
import name.nepavel.foodorder.restaservice.service.RestaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import name.nepavel.foodorder.restaapi.api.RestaurantApi;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestaController implements RestaurantApi {
    private final RestaService restaService;

    @Override
    public ResponseEntity<List<FoodItem>> menuGet() {
        return ResponseEntity.ok(restaService.list());
    }

    @Override
    public ResponseEntity<List<FoodItem>> orderPost(@Valid List<OrderItem> orderItem) {
        return null;
    }
}
