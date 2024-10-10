package ru.gordeev.api.requests.non_crud.checked;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.Projects;
import ru.gordeev.api.requests.EndpointActions;
import ru.gordeev.api.requests.Request;
import ru.gordeev.api.requests.non_crud.SearchProjectsInterface;

import java.util.Map;

public class CheckedSearchProjectsRequest extends Request implements SearchProjectsInterface<Projects>, EndpointActions {

    public CheckedSearchProjectsRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Projects searchProject(Map<String, String> searchParams) {
        return RestAssured
            .given()
            .spec(spec)
            .queryParams(searchParams)
            .get(endpoint.getUrl())
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(Projects.class);
    }
}
