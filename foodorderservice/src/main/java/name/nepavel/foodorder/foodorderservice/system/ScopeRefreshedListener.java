package name.nepavel.foodorder.foodorderservice.system;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.nepavel.foodorder.foodorderservice.config.Config;
import name.nepavel.foodorder.foodorderservice.obs.Observable;
import name.nepavel.foodorder.foodorderservice.obs.Observer;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScopeRefreshedListener implements ApplicationListener<RefreshScopeRefreshedEvent>, Observable {
    private final Config config;
    private final List<Observer> listeners = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {
        log.info("Event: {}, config: {}", event, config.getNames());
        listeners.forEach(observer -> {
            try {
                observer.onChange();
            } catch (Exception e) {
                log.error("Failed to notify observer!", e);
            }
        });
    }

    @Override
    public void listen(Observer observer) {
        listeners.add(observer);
    }
}
