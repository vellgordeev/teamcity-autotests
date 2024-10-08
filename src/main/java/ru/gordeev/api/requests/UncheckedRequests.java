package ru.gordeev.api.requests;

import io.restassured.specification.RequestSpecification;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.requests.unchecked.UncheckedBase;

import java.util.EnumMap;

public class UncheckedRequests {
    private final EnumMap<Endpoint, UncheckedBase> requests = new EnumMap<>(Endpoint.class);

    public UncheckedRequests(RequestSpecification spec) {
        for (var endpoint: Endpoint.values()) {
            requests.put(endpoint, new UncheckedBase(spec, endpoint));
        }
    }

    public UncheckedBase getRequest(Endpoint endpoint) {
        return requests.get(endpoint);
    }
}
