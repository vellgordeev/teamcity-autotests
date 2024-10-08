package ru.gordeev.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.gordeev.api.models.BaseModel;
import ru.gordeev.api.models.BuildType;
import ru.gordeev.api.models.Project;
import ru.gordeev.api.models.User;

@AllArgsConstructor
@Getter
public enum Endpoint {
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;
}
