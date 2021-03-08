package application.service;

import application.dao.UserDao;
import application.model.Role;
import application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        return user;
    }

    public List<User> allUsers() {
        return userDao.allUsers();
    }

    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }

    public boolean saveUser(User user) {
        User userFromDB = userDao.loadByUserName(user.getUsername());

        if (userFromDB != null) {
            throw new IllegalStateException("User " + user.getUsername() + " already exists");
        }

        checkRoles(user);
        user.setPassword(user.getPassword());
        userDao.saveUser(user);
        return true;
    }

    private void checkRoles(User user) {
        Collection<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "ROLE_ADMIN"));
        roles.add(new Role(2L, "ROLE_USER"));

        if(user.isUser() && user.isAdmin()) {
            user.setRoles(roles);
        }
        else if (user.isUser()) {
            user.setRoles((Collections.singleton(new Role(2L, "ROLE_USER"))));
        } else if (user.isAdmin()) {
            user.setRoles((Collections.singleton(new Role(1L, "ROLE_ADMIN"))));
        } else {
            throw new IllegalStateException("Choose at least one role");
        }
    }

    public boolean deleteById(Long id) {
        return userDao.deleteById(id);
    }

    public void editUser(User user) {
        checkRoles(user);
        userDao.editUser(user);
    }

}
