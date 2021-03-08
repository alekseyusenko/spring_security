package application.dao;

import application.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void insertRoles() {
            List<Role> roles = entityManager.createQuery("select r from Role r", Role.class)
                    .getResultList();
            if (roles.isEmpty()) {
                entityManager.persist(new Role(1l, "ROLE_ADMIN"));
                entityManager.persist(new Role(2L, "ROLE_USER"));
            }

    }
}


