package ru.gordeev.api.models;

import lombok.Data;

@Data
public class TestData {
    private Project project;
    private User user;
    private BuildType buildType;
}
