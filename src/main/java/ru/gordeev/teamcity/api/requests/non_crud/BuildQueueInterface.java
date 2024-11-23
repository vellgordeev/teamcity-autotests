package ru.gordeev.teamcity.api.requests.non_crud;

import ru.gordeev.teamcity.api.models.BaseModel;
import ru.gordeev.teamcity.api.requests.EndpointActions;

public interface BuildQueueInterface<T> extends EndpointActions {
    T triggerBuild(BaseModel model);
    T getBuildById(Long id);
}
