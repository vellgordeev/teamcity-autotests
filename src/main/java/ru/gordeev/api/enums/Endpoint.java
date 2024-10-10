package ru.gordeev.api.enums;

import lombok.Getter;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.models.Build;
import ru.gordeev.api.models.BuildType;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.models.Projects;
import ru.gordeev.api.models.User;
import ru.gordeev.api.requests.EndpointActions;
import ru.gordeev.api.requests.crud.CrudInterface;
import ru.gordeev.api.requests.non_crud.BuildQueueInterface;
import ru.gordeev.api.requests.non_crud.SearchProjectsInterface;
import ru.gordeev.api.requests.non_crud.checked.CheckedBuildQueueRequest;
import ru.gordeev.api.requests.non_crud.checked.CheckedSearchProjectsRequest;
import ru.gordeev.api.requests.non_crud.unchecked.UncheckedBuildQueueRequest;
import ru.gordeev.api.requests.non_crud.unchecked.UncheckedSearchProjectsRequest;

@Getter
public enum Endpoint {
    // CRUD endpoints
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class, CrudInterface.class),
    PROJECTS("/app/rest/projects", Project.class, CrudInterface.class),
    USERS("/app/rest/users", User.class, CrudInterface.class),

    // Non-CRUD endpoints with specific request handlers
    BUILD_QUEUE("/app/rest/buildQueue", Build.class,
        BuildQueueInterface.class, CheckedBuildQueueRequest.class,
        UncheckedBuildQueueRequest.class),
    SEARCH_PROJECTS("/app/rest/projects", Projects.class,
        SearchProjectsInterface.class, CheckedSearchProjectsRequest.class,
        UncheckedSearchProjectsRequest.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;
    private final Class<? extends EndpointActions> actionsClass;

    // These fields are only applicable for non-CRUD endpoints
    private final Class<? extends EndpointActions> checkedRequestHandlerClass;
    private final Class<? extends EndpointActions> uncheckedRequestHandlerClass;

    // Constructor for CRUD endpoints
    Endpoint(String url, Class<? extends BaseModel> modelClass,
             Class<? extends EndpointActions> actionsClass) {
        this(url, modelClass, actionsClass, null, null);
    }

    // Constructor for non-CRUD endpoints
    Endpoint(String url, Class<? extends BaseModel> modelClass,
             Class<? extends EndpointActions> actionsClass,
             Class<? extends EndpointActions> checkedRequestHandlerClass,
             Class<? extends EndpointActions> uncheckedRequestHandlerClass) {
        this.url = url;
        this.modelClass = modelClass;
        this.actionsClass = actionsClass;
        this.checkedRequestHandlerClass = checkedRequestHandlerClass;
        this.uncheckedRequestHandlerClass = uncheckedRequestHandlerClass;
    }
}
