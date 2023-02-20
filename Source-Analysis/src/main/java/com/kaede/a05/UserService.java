package com.kaede.a05;

import lombok.Data;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {
    private UserDao userDao;
    // @Autowired
    // public UserService(UserDao userDao) {
    //     this.userDao = userDao;
    // }
    public UserService(ObjectProvider<UserDao> userDao) {
        this.userDao = userDao.getIfUnique();
    }
}