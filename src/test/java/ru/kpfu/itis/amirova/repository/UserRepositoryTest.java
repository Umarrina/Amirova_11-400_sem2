package ru.kpfu.itis.amirova.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.kpfu.itis.amirova.model.Role;
import ru.kpfu.itis.amirova.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setEmail("test@example.com");
        user.setEnabled(true);
        entityManager.persistAndFlush(user);

        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void findByUsernameWithRoles_ShouldFetchRoles() {
        Role role = new Role();
        role.setName("ROLE_USER");
        entityManager.persist(role);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setEmail("role@example.com");
        user.setEnabled(true);
        user.setRoles(List.of(role));
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findByUsernameWithRoles("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getRoles()).hasSize(1);
        assertThat(found.get().getRoles().get(0).getName()).isEqualTo("ROLE_USER");
    }
}
