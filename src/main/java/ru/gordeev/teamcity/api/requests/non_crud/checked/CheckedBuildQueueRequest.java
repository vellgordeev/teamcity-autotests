package ru.gordeev.teamcity.api.requests.non_crud.checked;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import ru.gordeev.teamcity.api.enums.Endpoint;
import ru.gordeev.teamcity.api.models.BaseModel;
import ru.gordeev.teamcity.api.models.Build;
import ru.gordeev.teamcity.api.requests.EndpointActions;
import ru.gordeev.teamcity.api.requests.Request;
import ru.gordeev.teamcity.api.requests.non_crud.BuildQueueInterface;

public class CheckedBuildQueueRequest extends Request implements BuildQueueInterface<Build>, EndpointActions {

    public CheckedBuildQueueRequest(RequestSpecification spec, Endpoint endpoint) {
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
