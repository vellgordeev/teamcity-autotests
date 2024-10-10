package ru.gordeev.api.requests;

import io.restassured.specification.RequestSpecification;
import ru.gordeev.api.enums.Endpoint;

public abstract class Request {
    /**
     * Request is a class that describes the variable parameters of a request, such as:
     * specification, endpoint (relative URL, model).
     */
    protected final RequestSpecification spec;
    protected final Endpoint endpoint;

    public Request(RequestSpecification spec, Endpoint endpoint) {
        this.spec = spec;
        this.endpoint = endpoint;
    }
}
