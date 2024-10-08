package ru.gordeev.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles extends BaseModel {
    private List<Role> role;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Role extends BaseModel {
        @Builder.Default
        private String roleId = "SYSTEM_ADMIN";
        @Builder.Default
        private String scope = "g";
    }
}
