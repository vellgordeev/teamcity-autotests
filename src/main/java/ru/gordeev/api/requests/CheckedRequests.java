package ru.gordeev.api.requests;

import io.restassured.specification.RequestSpecification;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.checked.CheckedBase;

import java.util.EnumMap;

@SuppressWarnings("unchecked")
public class CheckedRequests {
    private final EnumMap<Endpoint, CheckedBase<?>> requests = new EnumMap<>(Endpoint.class);

    public CheckedRequests(RequestSpecification spec) {
        for (var endpoint: Endpoint.values()) {
            requests.put(endpoint, new CheckedBase<>(spec, endpoint));
        }
    }

    public <T extends BaseModel> CheckedBase<T> getRequest(Endpoint endpoint) {
        return (CheckedBase<T>) requests.get(endpoint);
    }
}
