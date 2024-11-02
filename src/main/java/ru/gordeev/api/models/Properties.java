package ru.gordeev.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gordeev.api.annotations.Random;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Properties extends BaseModel {
    private List<Property> property = new ArrayList<>();

    public Properties addProperty(String name, String value) {
        property.add(new Property(name, value));
        return this;
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Property extends BaseModel {
        @Random
        private String name;
        @Random
        private String value;
    }
}
