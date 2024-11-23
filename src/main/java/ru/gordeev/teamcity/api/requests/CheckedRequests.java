package ru.gordeev.teamcity.api.requests;

import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.BaseModel;
import ru.gordeev.teamcity.api.requests.crud.CheckedBase;
import ru.gordeev.teamcity.api.requests.crud.CrudInterface;

import java.lang.reflect.Constructor;
import java.util.EnumMap;

public class CheckedRequests {
    private static final Logger logger = LogManager.getLogger(CheckedRequests.class);
    private final EnumMap<Endpoint, EndpointActions> requests = new EnumMap<>(Endpoint.class);

    public CheckedRequests(RequestSpecification spec) {
        for (Endpoint endpoint : Endpoint.values()) {
            try {
                EndpointActions requestHandler;
                if (endpoint.getActionsClass() == CrudInterface.class) {
                    requestHandler = new CheckedBase<>(spec, endpoint);
                } else {
                    Constructor<? extends EndpointActions> constructor =
                        endpoint.getCheckedRequestHandlerClass()
                            .getConstructor(RequestSpecification.class, Endpoint.class);
                    requestHandler = constructor.newInstance(spec, endpoint);
                }
                requests.put(endpoint, requestHandler);
            } catch (Exception e) {
                logger.error("Failed to instantiate request handler for endpoint: {}", endpoint.getUrl(), e);
                throw new RuntimeException("Failed to instantiate request handler for endpoint: " + endpoint, e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> CheckedBase<T> getRequest(Endpoint endpoint) {
        EndpointActions action = requests.get(endpoint);
        if (action instanceof CheckedBase) {
            return (CheckedBase<T>) action;
        } else {
            String message = "Endpoint " + endpoint + " does not support CRUD operations with CheckedBase. Please provide correct implementation.";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends EndpointActions> T getRequest(Endpoint endpoint, Class<T> actionsClass) {
        EndpointActions action = requests.get(endpoint);
        if (actionsClass.isInstance(action)) {
            return (T) action;
        } else {
            String message = "Invalid action class for endpoint " + endpoint;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }
}

