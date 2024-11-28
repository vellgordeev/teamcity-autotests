package ru.gordeev.teamcity.api.requests.non_crud;

import ru.gordeev.teamcity.api.requests.EndpointActions;

public interface AgentAuthorizeInterface <T> extends EndpointActions {
    T authorizeAgent(String name, boolean isAuthorize);
}
