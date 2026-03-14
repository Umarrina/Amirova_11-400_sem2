package ru.kpfu.itis.amirova.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.amirova.model.Role;
import ru.kpfu.itis.amirova.repository.RoleRepository;

@Component
public class RoleInitializer {
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }
}
