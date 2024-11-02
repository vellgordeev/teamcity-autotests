package ru.gordeev.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gordeev.api.annotations.Random;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Steps extends BaseModel {
    private Integer count;
    @Random
    private List<Step> step;
}
