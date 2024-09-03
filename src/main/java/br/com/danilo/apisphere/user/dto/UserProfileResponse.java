package br.com.danilo.apisphere.user.dto;

import br.com.danilo.apisphere.user.User;

public record UserProfileResponse(
        String name,
        String bio,
        String email,
        String avatar
) {
    public UserProfileResponse(User user) {
        this(user.getName(), user.getBio(), user.getEmail(), user.getAvatar());
    }
}
