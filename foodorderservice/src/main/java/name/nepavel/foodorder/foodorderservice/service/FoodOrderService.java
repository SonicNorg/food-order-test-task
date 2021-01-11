package name.nepavel.foodorder.foodorderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.nepavel.foodorder.foodorderservice.config.RestaClients;
import name.nepavel.foodorder.restaapi.model.FoodItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Slf4j
public class FoodOrderService {
    private final RestaClients clients;

    public List<FoodItem> getMenu() {
        return clients.getClients().stream()
                .flatMap(client -> {
            try {
                ResponseEntity<List<FoodItem>> menu = client.getMenu();
                if (menu.getStatusCode() == HttpStatus.OK && menu.hasBody()) {
                    return menu.getBody().stream();
                } else {
                    log.error("Failed to get menu with status {}!", menu.getStatusCode());
                    return Stream.empty();
                }
            } catch (Exception e) {
                log.error("Failed to get menu!", e);
                return Stream.empty();
            }
        }).collect(toList());
    }
}
