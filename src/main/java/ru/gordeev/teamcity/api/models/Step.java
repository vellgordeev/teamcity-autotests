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
public class Step extends BaseModel {
    private String id;
    private String name;
    @Builder.Default
    private String type = "simpleRunner";
    @Random
    private Properties properties;
}
