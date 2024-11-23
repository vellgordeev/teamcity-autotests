package ru.gordeev.teamcity.api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BuildState {
    @JsonProperty("queued")
    QUEUED,

    @JsonProperty("finished")
    FINISHED,

    @JsonProperty("running")
    RUNNING
}
