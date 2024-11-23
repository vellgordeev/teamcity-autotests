package ru.gordeev.teamcity.api.requests.crud;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.BaseModel;
import ru.gordeev.teamcity.api.requests.EndpointActions;
import ru.gordeev.teamcity.api.requests.Request;
import ru.gordeev.teamcity.api.requests.non_crud.SearchProjectsInterface;

import java.util.Map;

public class UncheckedBase extends Request implements CrudInterface, SearchProjectsInterface<Response>, EndpointActions {

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
    public Response read(String locator) {
        return RestAssured
            .given()
            .spec(spec)
            .get(endpoint.getUrl() + "/" + locator);
    }

    @Override
    public Response update(String locator, BaseModel model) {
        return RestAssured
            .given()
            .body(model)
            .spec(spec)
            .put(endpoint.getUrl() + "/" + locator);
    }

    @Override
    public Response delete(String locator) {
        return RestAssured
            .given()
            .spec(spec)
            .delete(endpoint.getUrl() + "/" + locator);
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
