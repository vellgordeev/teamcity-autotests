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
public class User extends BaseModel {
    private String id;
    @Random
    private String username;
    @Random
    private String password;
    private Roles roles;
}
