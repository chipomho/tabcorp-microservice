package au.com.tabcorp.data.repository;

import au.com.tabcorp.data.DataBootstrap;
import au.com.tabcorp.data.TestBootstrap;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes={TestBootstrap.class, DataBootstrap.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = { "classpath:application.yaml" })
public abstract class AbstractRepositoryTest {


}
