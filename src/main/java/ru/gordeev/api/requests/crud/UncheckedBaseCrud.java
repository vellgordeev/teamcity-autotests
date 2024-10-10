package ru.gordeev.api.requests.crud;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.EndpointActions;
import ru.gordeev.api.requests.Request;

public class UncheckedBaseCrud extends Request implements CrudInterface, EndpointActions {

    public UncheckedBaseCrud(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response create(BaseModel model) {
        return RestAssured
            .given()
            .spec(spec)
            .body(model)
            .post(endpoint.getUrl());
    }

    @Override
    public Response read(String id) {
        return RestAssured
            .given()
            .spec(spec)
            .get(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    public Response update(String id, BaseModel model) {
        return RestAssured
            .given()
            .body(model)
            .spec(spec)
            .put(endpoint.getUrl() + "/id:" + id);
    }

    @Override
    public Response delete(String id) {
        return RestAssured
            .given()
            .spec(spec)
            .delete(endpoint.getUrl() + "/id:" + id);
    }
}
