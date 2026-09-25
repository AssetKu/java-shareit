package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(user ->
                        user.getEmail().equalsIgnoreCase(userDto.getEmail()));

        if (emailExists) {
            throw new ConflictException("Email already exists");
        }

        User user = UserMapper.toUser(userDto);

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        return UserMapper.toDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto update(Long id, UserDto dto) {

        User user = userRepository.findById(id);

        if (dto.getEmail() != null) {

            boolean emailExists = userRepository.findAll().stream()
                    .filter(u -> !u.getId().equals(id))
                    .anyMatch(u ->
                            u.getEmail().equalsIgnoreCase(dto.getEmail()));

            if (emailExists) {
                throw new ConflictException("Email already exists");
            }

            user.setEmail(dto.getEmail());
        }

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        return UserMapper.toDto(userRepository.update(user));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
}