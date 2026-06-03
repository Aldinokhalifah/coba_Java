package com.warung.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class LoginRequest {
    @NotBlank(message="Email wajib diisi")
    @Size(max = 256, message = "Email maksimal 50 karakter")
    @Email
    String email;

    @NotBlank(message="Password wajib diisi")
    @Size(min= 6, max = 256, message = "Password minimal 6 karakter")
    String password;
}