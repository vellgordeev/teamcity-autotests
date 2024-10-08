package ru.gordeev.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gordeev.api.annotations.Random;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project extends BaseModel {
    @Random
    private String id;
    @Random
    private String name;
    private String locator;
}
