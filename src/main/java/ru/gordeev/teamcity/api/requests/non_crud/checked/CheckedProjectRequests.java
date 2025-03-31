package ru.gordeev.teamcity.api.requests.non_crud.checked;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.Project;
import ru.gordeev.teamcity.api.models.Projects;
import ru.gordeev.teamcity.api.requests.crud.CheckedBase;
import ru.gordeev.teamcity.api.requests.non_crud.SearchProjectsInterface;

import java.util.Map;

/**
 * Specific request handler for Project endpoints, supporting CRUD (via inheritance)
 * and project searching (via SearchProjectsInterface).
 */
public class CheckedProjectRequests extends CheckedBase<Project> implements SearchProjectsInterface {

    public CheckedProjectRequests(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        if (endpoint != Endpoint.PROJECTS) {
            throw new IllegalArgumentException("CheckedProjectRequests can only be used with PROJECTS endpoint");
        }
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