package user_profile_service.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-profile-service/api/profiles/foo-bar")
public class FooBarController {
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public String list() {
        return "Listando algo";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create() {
        return "Cadastrando algo";
    }
}
