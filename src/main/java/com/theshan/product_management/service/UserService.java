package com.theshan.product_management.service;

import com.theshan.product_management.dto.Response;
import com.theshan.product_management.dto.user.*;
import com.theshan.product_management.entity.Role;
import com.theshan.product_management.entity.User;
import com.theshan.product_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    /**
     * Method will create a user.
     * @return response object
     */
    public Response createUser(UserCreationRequest userCreationRequest) {
        Optional<User> userByEmail = this.userRepository.findByEmail(userCreationRequest.getEmail());

        userCreationRequest.getRoles().forEach(role -> {
            this.roleService.findByName(role.getName());
        });

        if (userByEmail.isPresent()) {
            throw new IllegalStateException("User with email " + userCreationRequest.getEmail() + " already exists.");
        }

        User userBuilder = User.userBuilder()
                .firstName(userCreationRequest.getFirstName())
                .lastName(userCreationRequest.getLastName())
                .email(userCreationRequest.getEmail())
                .password(userCreationRequest.getPassword())
                .isEnabled(userCreationRequest.isEnabled())
                .roles(userCreationRequest.getRoles())
                .registeredAt(new Date())
                .build();

        User savedUser = this.userRepository.save(userBuilder);
        UserResponse userResponse = this.modelMapper.map(savedUser, UserResponse.class);

        return Response.responseBuilder()
                .flag(true)
                .code(HttpStatus.CREATED.value())
                .message("User created.")
                .data(userResponse)
                .build();
    }

    /**
     * Method will register a user.
     * @param userRegistrationRequest
     * @return response object
     */
    @Transactional
    public Response registerUser(UserRegistrationRequest userRegistrationRequest) {
        Optional<User> userByEmail = this.userRepository.findByEmail(userRegistrationRequest.getEmail());

        if (userByEmail.isPresent()) {
            throw new IllegalStateException("User with email " + userRegistrationRequest.getEmail() + " already exists.");
        }

        Role roleUser = this.roleService.findByName("USER");

        User userBuilder = User.userBuilder()
                .firstName(userRegistrationRequest.getFirstName())
                .lastName(userRegistrationRequest.getLastName())
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .roles(List.of(roleUser))
                .isEnabled(true)
                .registeredAt(new Date())
                .build();

        User savedUser = this.userRepository.save(userBuilder);
        UserResponse userResponse = this.modelMapper.map(savedUser, UserResponse.class);

        return Response.responseBuilder()
                .flag(true)
                .code(HttpStatus.CREATED.value())
                .message("User registered.")
                .data(userResponse)
                .build();
    }

    /**
     * Method will return all inactive and active users.
     * @return response object
     */
    public Response findAllUsers() {
        List<User> allUsers = this.userRepository.findAll();
        if (allUsers.size() > 0) {
            List<UserResponse> userResponseList = new ArrayList<>();
            for (User user : allUsers) {
                UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
                userResponseList.add(userResponse);
            }
            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.FOUND.value())
                    .message("All users.")
                    .data(userResponseList)
                    .build();
        } else {
            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No users.")
                    .data(null)
                    .build();
        }
    }

    /**
     * Method will return all active users.
     * @return response object
     */
    public Response findAllActiveUsers() {
        List<User> activeUserList = this.userRepository.findByIsEnabled(true);

        if (activeUserList.size() > 0) {
            List<UserResponse> userResponseList = new ArrayList<>();
            for (User user : activeUserList) {
                UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
                userResponseList.add(userResponse);
            }

            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.FOUND.value())
                    .message("All active users.")
                    .data(userResponseList)
                    .build();
        } else {
            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No active users.")
                    .data(null)
                    .build();
        }
    }

    /**
     * Method will return all inactive users.
     * @return response object
     */
    public Response findAllInactiveUsers() {
        List<User> activeUserList = this.userRepository.findByIsEnabled(false);

        if (activeUserList.size() > 0) {
            List<UserResponse> userResponseList = new ArrayList<>();
            for (User user : activeUserList) {
                UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
                userResponseList.add(userResponse);
            }

            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.FOUND.value())
                    .message("All inactive users.")
                    .data(userResponseList)
                    .build();
        } else {
            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("No active users.")
                    .data(null)
                    .build();
        }

    }

    /**
     * Method will return the user by its email.
     * If user does not exist throw a objectNotFoundException - This exception is not created yet. Will create in the future.
     * @param email
     * @return response object
     */
    public Response findUserByEmail(String email) {
        Optional<User> userByEmail = this.userRepository.findByEmail(email);

        try {
            if (userByEmail.isEmpty()) {
                throw new IllegalStateException("User with email " + email + " does not exists.");
            }

            UserResponse userResponse = this.modelMapper.map(userByEmail.get(), UserResponse.class);

            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.FOUND.value())
                    .message("User")
                    .data(userResponse)
                    .build();
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    /**
     * Method will return a user by its id.
     * If user does not exist throw a objectNotFoundException - This exception is not created yet. Will create in the future.
     * @param userId
     * @return response object
     */
    public Response findUserById(int userId) {
        Optional<User> userById = this.userRepository.findById(userId);

        try {
            if (userById.isEmpty()) {
                throw new IllegalStateException("User with id " + userId + " does not exists.");
            }

            UserResponse userResponse = this.modelMapper.map(userById.get(), UserResponse.class);

            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.FOUND.value())
                    .message("User with user id " + userId + ".")
                    .data(userResponse)
                    .build();

        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    /**
     * Method will enable the user by its id. If user already enabled then no need to enable the user again. if not enable the user.
     * If user does not exist throw a objectNotFoundException - This exception is not created yet. Will create in the future.
     * @param userId
     * @return response object
     */
    @Transactional
    public Response enableUserById(int userId) {
        Optional<User> userById = this.userRepository.findById(userId);

        try {
            if (userById.isEmpty()) {
                throw new IllegalStateException("User with id " + userId + " does not exists.");
            }

            if (userById.get().getIsEnabled()) {
                return Response.responseBuilder()
                        .flag(true)
                        .code(HttpStatus.FOUND.value())
                        .message("User with user id" + 1 + " is already enabled.")
                        .build();
            } else {
                userById.get().setIsEnabled(true);
                this.userRepository.save(userById.get());

                return Response.responseBuilder()
                        .flag(true)
                        .code(HttpStatus.FOUND.value())
                        .message("User with user id" + 1 + " is enabled.")
                        .build();
            }

        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    /**
     * Method will disable the user by its id. If user already disabled then no need to disable the user again. if not enable the user.
     * If user does not exist throw a objectNotFoundException - This exception is not created yet. Will create in the future.
     * @param userId
     * @return response object
     */
    @Transactional
    public Response disableUserById(int userId) {
        Optional<User> userById = this.userRepository.findById(userId);

        try {
            if (userById.isEmpty()) {
                throw new IllegalStateException("User with id " + userId + " does not exists.");
            }

            if (userById.get().getIsEnabled()) {
                return Response.responseBuilder()
                        .flag(true)
                        .code(HttpStatus.FOUND.value())
                        .message("User with user id" + 1 + " is already disabled.")
                        .build();
            } else {
                userById.get().setIsEnabled(false);
                this.userRepository.save(userById.get());

                return Response.responseBuilder()
                        .flag(true)
                        .code(HttpStatus.FOUND.value())
                        .message("User with user id" + 1 + " is disabled.")
                        .build();
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    /**
     * This method will update the user by user
     * If user does not exist throw a objectNotFoundException - This exception is not created yet. Will create in the future.
     * @param userId
     * @param userUpdateRequest
     * @return response object
    **/
    @Transactional
    public Response updateUserByUserUsingUserId(int userId, UserUpdateRequest userUpdateRequest) {
        Optional<User> optionalUser = this.userRepository.findById(userId);

        try {
            if (optionalUser.isEmpty()) {
                throw new IllegalStateException("User with id " + userId + " does not exists.");
            }

            optionalUser.get().setFirstName(userUpdateRequest.getFirstName());
            optionalUser.get().setLastName(userUpdateRequest.getLastName());
            optionalUser.get().setEmail(userUpdateRequest.getEmail());
            optionalUser.get().setPassword(userUpdateRequest.getPassword());
            optionalUser.get().setUpdatedAt(new Date());
            User savedUser = this.userRepository.save(optionalUser.get());

            UserResponse userResponse = this.modelMapper.map(savedUser, UserResponse.class);

            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.OK.value())
                    .message("User with user id " + userId + " is updated.")
                    .data(userResponse)
                    .build();

        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    /**
     * This method will update the user only by admin
     * If user does not exist throw a objectNotFoundException - This exception is not created yet. Will create in the future.
     * @param userId
     * @param userUpdateRequestByAdmin
     * @return response object
     */
    @Transactional
    public Response updateUserByAdminUsingUserId(int userId, UserUpdateRequestByAdmin userUpdateRequestByAdmin) {
        Optional<User> optionalUser = this.userRepository.findById(userId);

        try {
            if (optionalUser.isEmpty()) {
                throw new IllegalStateException("User with id " + userId + " does not exists.");
            }

            optionalUser.get().setFirstName(userUpdateRequestByAdmin.getFirstName());
            optionalUser.get().setLastName(userUpdateRequestByAdmin.getLastName());
            optionalUser.get().setEmail(userUpdateRequestByAdmin.getEmail());
            optionalUser.get().setIsEnabled(userUpdateRequestByAdmin.isEnabled());
            optionalUser.get().setRoles(userUpdateRequestByAdmin.getRoles());
            optionalUser.get().setUpdatedAt(new Date());
            User savedUser = this.userRepository.save(optionalUser.get());

            UserResponse userResponse = this.modelMapper.map(savedUser, UserResponse.class);

            return Response.responseBuilder()
                    .flag(true)
                    .code(HttpStatus.OK.value())
                    .message("User with user id " + userId + " is updated.")
                    .data(userResponse)
                    .build();

        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
