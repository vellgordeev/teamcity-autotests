package ru.gordeev.api.requests.non_crud.checked;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.gordeev.api.enums.Endpoint;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.models.Build;
import ru.gordeev.api.requests.EndpointActions;
import ru.gordeev.api.requests.Request;
import ru.gordeev.api.requests.non_crud.BuildInterface;

public class CheckedBuildRequests extends Request implements BuildInterface<Build>, EndpointActions {

    public CheckedBuildRequests(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Override
    public Build triggerBuild(BaseModel model) {
        return RestAssured
            .given()
            .spec(spec)
            .body(model)
            .post(endpoint.getUrl())
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(Build.class);
    }

    @Override
    public Build getBuildById(Long id) {
        return RestAssured
            .given()
            .spec(spec)
            .get("/app/rest/builds/id:" + id)
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .extract().as(Build.class);
    }
}
