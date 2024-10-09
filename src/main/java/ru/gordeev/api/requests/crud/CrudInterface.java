package ru.gordeev.api.requests.crud;

import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.requests.EndpointActions;

public interface CrudInterface extends EndpointActions {
    Object create(BaseModel model);
    Object read(String id);
    Object update(String id, BaseModel model);
    Object delete(String id);
}
