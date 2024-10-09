package ru.gordeev.api.requests.non_crud;

import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.EndpointActions;

public interface BuildInterface<T> extends EndpointActions {
    T triggerBuild(BaseModel model);
    T getBuildById(Long id);
}
