package ru.gordeev.teamcity.api.requests.non_crud.unchecked;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.requests.EndpointActions;
import ru.gordeev.teamcity.api.requests.Request;
import ru.gordeev.teamcity.api.requests.non_crud.AgentAuthorizeInterface;

public class UncheckedAgentAuthorizeRequest extends Request implements AgentAuthorizeInterface<Response>, EndpointActions {

    public UncheckedAgentAuthorizeRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Response authorizeAgent(String name, boolean isAuthorize) {
        return RestAssured
                .given()
                .spec(spec)
                .accept(ContentType.ANY)
                .contentType(ContentType.TEXT)
                .pathParam("name", name)
                .body(isAuthorize)
                .put(endpoint.getUrl());
    }

}
