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

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Email уже существует");
        }

        User user = UserMapper.toUser(userDto);

        return UserMapper.toDto(
                userRepository.save(user)
        );
    }

    @Override
    public UserDto getById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

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
    public UserDto update(Long id, UserDto userDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        if (userDto.getEmail() != null
                && !userDto.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(userDto.getEmail())) {

            throw new ConflictException(
                    "Email уже используется"
            );
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toDto(
                userRepository.save(user)
        );
    }

    @Override
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь не найден"));

        userRepository.delete(user);
    }
}