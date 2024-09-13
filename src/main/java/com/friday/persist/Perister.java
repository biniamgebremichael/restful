package com.friday.persist;

import com.friday.User;

import java.util.List;

public interface Perister {

    int add(User user);

    int update(User user);

    int delete(String email);

    User getByEmail(String email);

    List<User> getAll();

    List<User> search(String query) ;
}
