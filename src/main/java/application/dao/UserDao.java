package application.dao;

import application.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public User loadByUserName(String username) {
        try {
            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.username = :username ";

            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("username", username);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> allUsers() {
        return entityManager
                .createQuery("select a from User a", User.class)
                .getResultList();
    }

    public User findUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    public boolean deleteById(Long id) {
        int isSuccessful = entityManager.createQuery("delete from User u where u.id=:id")
                .setParameter("id", id)
                .executeUpdate();
        if (isSuccessful == 0) {
            return false;
        }
        return true;
    }


    public void saveUser(User user) {
        entityManager.persist(user);
    }

    public void editUser(User user) {
        entityManager.merge(user);
    }
}
