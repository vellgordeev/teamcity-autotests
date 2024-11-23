package ru.gordeev.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gordeev.teamcity.api.annotations.Random;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildType extends BaseModel {
    @Random
    private String id;
    @Random
    private String name;
    private Project project;
    private Steps steps;
}
