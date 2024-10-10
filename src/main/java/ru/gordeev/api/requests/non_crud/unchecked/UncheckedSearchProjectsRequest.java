package ru.gordeev.api.requests.non_crud.unchecked;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.requests.EndpointActions;
import ru.gordeev.api.requests.Request;
import ru.gordeev.api.requests.non_crud.SearchProjectsInterface;

import java.util.Map;

public class UncheckedSearchProjectsRequest extends Request implements SearchProjectsInterface<Response>, EndpointActions {

    public UncheckedSearchProjectsRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response searchProject(Map<String, String> searchParams) {
        return RestAssured
            .given()
            .spec(spec)
            .queryParams(searchParams)
            .get(endpoint.getUrl());
    }
}
