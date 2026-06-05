package com.company.clothingerp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
