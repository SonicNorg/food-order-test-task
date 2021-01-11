package name.nepavel.foodorder.foodorderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.nepavel.foodorder.foodorderservice.service.FoodOrderService;
import name.nepavel.foodorder.restaapi.model.FoodItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoodOrderServiceController {
    private final FoodOrderService service;

    @GetMapping("/menu")
    public List<FoodItem> getMenu() {
        return service.getMenu();
    }
}
