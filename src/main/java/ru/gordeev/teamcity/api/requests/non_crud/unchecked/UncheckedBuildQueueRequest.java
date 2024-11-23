package ru.gordeev.teamcity.api.requests.non_crud.unchecked;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.BaseModel;
import ru.gordeev.teamcity.api.requests.EndpointActions;
import ru.gordeev.teamcity.api.requests.Request;
import ru.gordeev.teamcity.api.requests.non_crud.BuildQueueInterface;

public class UncheckedBuildQueueRequest extends Request implements BuildQueueInterface<Response>, EndpointActions {

    public UncheckedBuildQueueRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response triggerBuild(BaseModel model) {
        return RestAssured
            .given()
            .spec(spec)
            .body(model)
            .post(endpoint.getUrl());
    }

    @Override
    public Response getBuildById(Long id) {
        return RestAssured
            .given()
            .spec(spec)
            .get("/app/rest/builds/id:" + id);
    }
}
