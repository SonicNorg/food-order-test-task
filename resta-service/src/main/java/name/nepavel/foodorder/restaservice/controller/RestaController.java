package name.nepavel.foodorder.restaservice.controller;

import name.nepavel.foodorder.restaapi.model.FoodItem;
import name.nepavel.foodorder.restaapi.model.OrderItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import name.nepavel.foodorder.restaapi.api.RestaurantApi;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RestaController implements RestaurantApi {

    @Override
    public ResponseEntity<List<FoodItem>> menuGet() {
        return null;
    }

    @Override
    public ResponseEntity<List<FoodItem>> orderPost(@Valid List<OrderItem> orderItem) {
        return null;
    }
}
