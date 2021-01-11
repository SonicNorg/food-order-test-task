package name.nepavel.foodorder.restaservice.repository;

import lombok.NoArgsConstructor;
import lombok.Singular;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class OrderEntity {
    private Integer orderId;
    private OffsetDateTime createdAt;
    @Singular
    private Map<FoodEntity, Integer> foods = new HashMap<>();

    public Float getPrice() {
        return foods.entrySet().stream().reduce(0f, (itemPrice, foodEntry) -> itemPrice + foodEntry.getKey().getPrice() * foodEntry.getValue(), Float::sum);
    }

    public Integer getOrderId() {
        return orderId;
    }

    public OrderEntity setOrderId(Integer orderId) {
        this.orderId = orderId;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Map<FoodEntity, Integer> getFoods() {
        return foods;
    }

    public OrderEntity setFoods(Map<FoodEntity, Integer> foods) {
        this.foods = foods;
        return this;
    }
}
