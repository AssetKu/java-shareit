package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserRepository {

    User save(User user);

    User findById(Long id);

    Collection<User> findAll();

    User update(User user);

    void delete(Long id);
}