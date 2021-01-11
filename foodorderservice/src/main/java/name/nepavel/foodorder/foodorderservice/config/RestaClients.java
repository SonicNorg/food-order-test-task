package name.nepavel.foodorder.foodorderservice.config;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.nepavel.foodorder.foodorderservice.obs.Observer;
import name.nepavel.foodorder.restaapi.api.RestaurantApi;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Import(FeignClientsConfiguration.class)
public class RestaClients implements Observer {
    private final Config config;
    private final List<RestaurantApi> clients = Collections.synchronizedList(new ArrayList<>());
    private final Decoder decoder;
    private final Encoder encoder;
    private final Client client;
    private final Contract contract;

    @PostConstruct
    public void init() {
        onChange();
    }

    @Override
    public void onChange() {
        synchronized (clients) {
            clients.clear();
            config.getNames().forEach(name -> clients.add(createFeign(name)));
        }
    }

    public List<RestaurantApi> getClients() {
        return List.copyOf(clients);
    }

    private RestaurantApi createFeign(String name) {
        return Feign.builder()
                .client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .target(RestaurantApi.class, String.format("http://%s", name));
    }
}
