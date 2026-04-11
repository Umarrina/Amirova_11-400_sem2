package ru.kpfu.itis.amirova.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.kpfu.itis.amirova.model.Role;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired private RoleRepository roleRepository;
    @Autowired private TestEntityManager em;

    @Test
    void findByName_ReturnsRole() {
        Role r = new Role();
        r.setName("ROLE_ADMIN");
        em.persistAndFlush(r);
        assertThat(roleRepository.findByName("ROLE_ADMIN")).isPresent();
    }

    @Test
    void findByName_NotFound_ReturnsEmpty() {
        assertThat(roleRepository.findByName("FAKE")).isEmpty();
    }
}