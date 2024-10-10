package ru.gordeev.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projects extends BaseModel {
    private Integer count;
    private String href;
    private String nextHref;
    private String prevHref;
    private List<Project> project;
}
