package ru.gordeev.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gordeev.api.enums.BuildState;
import ru.gordeev.api.enums.BuildStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Build extends BaseModel {
    private BuildType buildType;
    private Long id;
    private BuildState state;
    private BuildStatus status;
    private String href;
    private String waitReason;
}
