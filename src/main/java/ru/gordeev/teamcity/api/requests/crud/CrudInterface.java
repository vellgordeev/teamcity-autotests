package ru.gordeev.teamcity.api.requests.crud;

import ru.gordeev.teamcity.api.models.BaseModel;
import ru.gordeev.teamcity.api.requests.EndpointActions;

public interface CrudInterface extends EndpointActions {
    Object create(BaseModel model);
    Object read(String id);
    Object update(String id, BaseModel model);
    Object delete(String id);
}
