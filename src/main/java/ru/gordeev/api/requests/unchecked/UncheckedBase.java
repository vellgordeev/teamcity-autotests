package ru.gordeev.api.requests.unchecked;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.CrudInterface;
import ru.gordeev.api.requests.Request;

public class UncheckedBase extends Request implements CrudInterface {

    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
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
