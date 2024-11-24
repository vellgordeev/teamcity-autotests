package ru.gordeev.teamcity.api.requests.non_crud.checked;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.requests.EndpointActions;
import ru.gordeev.teamcity.api.requests.Request;
import ru.gordeev.teamcity.api.requests.non_crud.AgentAuthorizeInterface;

public class CheckedAgentAuthorizeRequest extends Request implements AgentAuthorizeInterface<ValidatableResponse>, EndpointActions {

    public CheckedAgentAuthorizeRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public ValidatableResponse authorizeAgent(String name, boolean isAuthorize) {
        return RestAssured
                .given()
                .spec(spec)
                .accept(ContentType.ANY)
                .contentType(ContentType.TEXT)
                .pathParam("name", name)
                .body(isAuthorize)
                .put(endpoint.getUrl())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.containsString("true"));
    }
}
