//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.*;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//import ru.kpfu.itis.amirova.dto.UserDto;
//import ru.kpfu.itis.amirova.model.User;
//import ru.kpfu.itis.amirova.repository.UserRepository;
//import ru.kpfu.itis.amirova.repository.UserRepositoryHibernate;
//import ru.kpfu.itis.amirova.service.UserService;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = UserServiceTest.TestConfig.class)
//@TestPropertySource("classpath:persistence-test.properties")
//@Transactional
//public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;  // теперь Spring сможет его создать
//
//    @Test
//    public void testCreateAndFind() {
//        User user = new User();
//        user.setUsername("test");
//        userService.create(user);
//
//        UserDto found = userService.findByUsername("test");
//        assertNotNull(found);
//        assertEquals("test", found.getUsername());
//    }
//
//    @Test
//    public void testUpdate() {
//        User user = new User();
//        user.setUsername("old");
//        userService.create(user);
//        Long id = userService.findByUsername("old").getId();
//
//        User updateUser = new User();
//        updateUser.setId(id);
//        updateUser.setUsername("new");
//        userService.update(updateUser);
//
//        UserDto found = userService.findByUsername("new");
//        assertNotNull(found);
//        assertEquals(id, found.getId());
//    }
//
//    @Test
//    public void testDelete() {
//        User user = new User();
//        user.setUsername("del");
//        userService.create(user);
//        Long id = userService.findByUsername("del").getId();
//
//        User delUser = new User();
//        delUser.setId(id);
//        userService.delete(delUser);
//
//        assertNull(userService.findByUsername("del"));
//    }
//
//    @Test
//    public void testFindAll() {
//        userService.create(createUser("a"));
//        userService.create(createUser("b"));
//        assertEquals(2, userService.findAll().size());
//    }
//
//    private User createUser(String name) {
//        User u = new User();
//        u.setUsername(name);
//        return u;
//    }
//
//    @Configuration
//    @EnableTransactionManagement
//    @EnableJpaRepositories("ru.kpfu.itis.amirova.repository")
//    @ComponentScan("ru.kpfu.itis.amirova.service")  // сканируем сервис, чтобы создать его бин
//    public static class TestConfig {
//
//        @Bean
//        public DataSource dataSource() {
//            return new org.springframework.jdbc.datasource.DriverManagerDataSource(
//                    "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
//                    "sa",
//                    ""
//            );
//        }
//
//        @Bean
//        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//            em.setDataSource(dataSource());
//            em.setPackagesToScan("ru.kpfu.itis.amirova.model");
//            em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//            Properties props = new Properties();
//            props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//            props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//            props.setProperty("hibernate.show_sql", "true");
//            em.setJpaProperties(props);
//            return em;
//        }
//
//        @Bean
//        public PlatformTransactionManager transactionManager() {
//            return new JpaTransactionManager(entityManagerFactory().getObject());
//        }
//
//        // ✅ Мок для UserRepositoryHibernate, который требует конструктор UserService
//        @Bean
//        public UserRepositoryHibernate userRepositoryHibernate() {
//            return Mockito.mock(UserRepositoryHibernate.class);
//        }
//    }
//}