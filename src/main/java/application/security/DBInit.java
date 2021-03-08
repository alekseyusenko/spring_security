package application.security;

import application.dao.RoleDao;
import application.dao.UserDao;
import application.model.Role;
import application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
public class DBInit {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private RoleDao roleDao;

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @PostConstruct
    private void postConstruct() throws InterruptedException {
        roleDao.insertRoles();
        List<User> userList = userDao.allUsers();
        if (userList.isEmpty()) {
            System.out.println("No users found. Creating default admin user.");
            Thread.sleep(3000);
            System.out.println("Credentials: ");
            System.out.println("username = admin, password = admin");
            Thread.sleep(3000);
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setAdmin(true);
            admin.setRoles(Collections.singleton(new Role(1L, "ROLE_ADMIN")));
            userDao.saveUser(admin);
        }
    }

}
