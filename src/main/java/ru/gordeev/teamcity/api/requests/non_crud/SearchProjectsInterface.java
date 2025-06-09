package ru.gordeev.teamcity.api.requests.non_crud;

import ru.gordeev.teamcity.api.models.Projects;
import ru.gordeev.teamcity.api.requests.EndpointActions;

import java.util.Map;

public interface SearchProjectsInterface extends EndpointActions {
    Projects searchProject(Map<String, String> searchParams);
}
