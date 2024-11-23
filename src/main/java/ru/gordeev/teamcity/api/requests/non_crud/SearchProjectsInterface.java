package ru.gordeev.teamcity.api.requests.non_crud;

import ru.gordeev.teamcity.api.requests.EndpointActions;

import java.util.Map;

public interface SearchProjectsInterface<T> extends EndpointActions {
    T searchProject(Map<String, String> searchParams);
}
