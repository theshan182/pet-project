package com.theshan.product_management.service;

import com.theshan.product_management.dto.Response;
import com.theshan.product_management.dto.user.*;
import com.theshan.product_management.entity.Role;
import com.theshan.product_management.entity.User;
import com.theshan.product_management.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    UserService userService;

    List<Role> roles;
    List<User> users;
    List<User> emptyUserList;

    @BeforeEach
    void setUp() {
        Role roleAdmin = Role.roleBuilder()
                .name("ADMIN")
                .createdAt(new Date())
                .build();

        Role roleModerator = Role.roleBuilder()
                .name("MODERATOR")
                .createdAt(new Date())
                .build();

        this.roles = new ArrayList<>();
        this.roles.add(roleAdmin);
        this.roles.add(roleModerator);

        User userOne = User.userBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmaill.com")
                .password("123")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        User userTwo = User.userBuilder()
                .firstName("Bill")
                .lastName("Gates")
                .email("bill@gmaill.com")
                .password("123")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        User userThree = User.userBuilder()
                .firstName("Kushan")
                .lastName("Weerakokdy")
                .email("kay@gmaill.com")
                .password("123")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        User userFour = User.userBuilder()
                .firstName("Jonathan")
                .lastName("Essex")
                .email("jonathan@gmaill.com")
                .password("123")
                .isEnabled(false)
                .roles(this.roles)
                .build();

        User userFive = User.userBuilder()
                .firstName("Johnny")
                .lastName("Timberlake")
                .email("johnnny@gmaill.com")
                .password("123")
                .isEnabled(false)
                .roles(this.roles)
                .build();

        this.users = new ArrayList<>();
        this.users.add(userOne);
        this.users.add(userTwo);
        this.users.add(userThree);
        this.users.add(userFour);
        this.users.add(userFive);

        this.emptyUserList = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldCreateUser() {
        UserCreationRequest createUser = UserCreationRequest.userCreationRequestBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("213")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        User userBuild = User.userBuilder()
                .firstName(createUser.getFirstName())
                .lastName(createUser.getLastName())
                .email(createUser.getEmail())
                .password(createUser.getPassword())
                .isEnabled(createUser.isEnabled())
                .roles(createUser.getRoles())
                .build();

        UserResponse userResponse = this.modelMapper.map(userBuild, UserResponse.class);

        when(this.userRepository.save(userBuild)).thenReturn(userBuild);
        when(this.modelMapper.map(userBuild, UserResponse.class)).thenReturn(userResponse);

        Response response = this.userService.createUser(createUser);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getMessage()).isEqualTo("User created.");
        assertThat(response.getData()).isEqualTo(userResponse);
    }

    @Test
    public void shouldNotCreateUserBecauseEmailAlreadyExists() {
        UserCreationRequest createUser = UserCreationRequest.userCreationRequestBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("213")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        when(this.userRepository.findByEmail(createUser.getEmail())).thenThrow(new IllegalStateException("User with email " + createUser.getEmail() + " already exists."));

        assertThrows(IllegalStateException.class, () -> this.userService.createUser(createUser));
    }

    @Test
    public void shouldRegisterUser() {
        UserRegistrationRequest registerUser = UserRegistrationRequest.userRegistrationRequestBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("213")
                .build();

        Role roleUser = Role.roleBuilder()
                .name("USER")
                .createdAt(new Date())
                .build();

        User userBuild = User.userBuilder()
                .firstName(registerUser.getFirstName())
                .lastName(registerUser.getLastName())
                .email(registerUser.getEmail())
                .password(registerUser.getPassword())
                .isEnabled(true)
                .roles(List.of(roleUser))
                .build();

        UserResponse userResponse = this.modelMapper.map(userBuild, UserResponse.class);

        when(this.modelMapper.map(userBuild, UserResponse.class)).thenReturn(userResponse);
        when(this.roleService.findByName(roleUser.getName())).thenReturn(roleUser);
        when(this.userRepository.save(userBuild)).thenReturn(userBuild);

        Response response = this.userService.registerUser(registerUser);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getMessage()).isEqualTo("User registered.");
        assertThat(response.getData()).isEqualTo(userResponse);
    }

    @Test
    public void shouldNotRegisterUserBecauseEmailAlreadyExists() {
        UserRegistrationRequest registerUser = UserRegistrationRequest.userRegistrationRequestBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("213")
                .build();

        when(this.userRepository.findByEmail(registerUser.getEmail())).thenThrow(new IllegalStateException("User with email " + registerUser.getEmail() + " already exists."));

        assertThrows(IllegalStateException.class, () -> this.userService.registerUser(registerUser));
    }

    @Test
    public void shouldReturnAllUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : this.users) {
            UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
            userResponseList.add(userResponse);
            when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);
        }

        when(this.userRepository.findAll()).thenReturn(this.users);

        Response response = this.userService.findAllUsers();

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("All users.");
        assertThat(response.getData()).isEqualTo(userResponseList);
    }

    @Test
    public void shouldNotReturnUsers() {
        when(this.userRepository.findAll()).thenReturn(this.emptyUserList);

        Response response = this.userService.findAllUsers();

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getMessage()).isEqualTo("No users.");
        assertThat(response.getData()).isEqualTo(null);
    }

    @Test
    public void shouldReturnAllEnabledUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : this.users) {
            UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
            userResponseList.add(userResponse);
            when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);
        }

        when(this.userRepository.findByIsEnabled(true)).thenReturn(this.users);

        Response response = this.userService.findAllActiveUsers();

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("All active users.");
        assertThat(response.getData()).isEqualTo(userResponseList);
    }

    @Test
    public void shouldReturnAllDisabledUsers() {
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : this.users) {
            UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
            userResponseList.add(userResponse);
            when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);
        }

        when(this.userRepository.findByIsEnabled(false)).thenReturn(this.users);

        Response response = this.userService.findAllInactiveUsers();

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("All inactive users.");
        assertThat(response.getData()).isEqualTo(userResponseList);
    }

    @Test
    public void shouldReturnUserByEmail() {
        User user = User.userBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("213")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);

        when(this.userRepository.findByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        Response response = this.userService.findUserByEmail("john@gmail.com");

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("User");
        assertThat(response.getData()).isEqualTo(userResponse);
    }

    @Test
    public void shouldNotReturnUserByEmail() {
        String email = "john@gmail.com";
        when(this.userRepository.findByEmail(email)).thenThrow(new IllegalStateException("User with email " + email + " does not exist."));

        assertThrows(IllegalStateException.class, () -> this.userService.findUserByEmail(email));
    }

    @Test
    public void shouldReturnUserById() {
        User user = User.userBuilder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .email("john@gmail.com")
                .password("213")
                .isEnabled(true)
                .roles(this.roles)
                .build();

        UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);

        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));
        when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        Response response = this.userService.findUserById(1);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("User with user id " + 1 + ".");
        assertThat(response.getData()).isEqualTo(userResponse);
    }

    @Test
    public void shouldNotReturnUserByUserId() {
        int userId = 1;
        when(this.userRepository.findById(userId)).thenThrow(new IllegalStateException("User with id " + userId + " does not exist."));

        assertThrows(IllegalStateException.class, () -> this.userService.findUserById(userId));
    }

    @Test
    public void shouldEnableUserById() {
        User user = User.userBuilder()
                .id(1)
                .firstName("Johnny")
                .lastName("Timberlake")
                .email("johnnny@gmaill.com")
                .password("123")
                .isEnabled(false)
                .build();

        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));
        when(this.userRepository.save(user)).thenReturn(user);

        Response response = this.userService.enableUserById(1);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("User with user id" + 1 + " is enabled.");
        assertThat(response.getData()).isEqualTo(null);
    }

    @Test
    public void shouldDisableUserById() {
        User user = User.userBuilder()
                .id(1)
                .firstName("Johnny")
                .lastName("Timberlake")
                .email("johnnny@gmaill.com")
                .password("123")
                .isEnabled(true)
                .build();

        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));
        when(this.userRepository.save(user)).thenReturn(user);

        Response response = this.userService.disableUserById(1);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getMessage()).isEqualTo("User with user id" + 1 + " is disabled.");
        assertThat(response.getData()).isEqualTo(null);
    }

    @Test
    public void shouldDeleteUserById() {
        User user = User.userBuilder()
                .id(1)
                .firstName("Johnny")
                .lastName("Timberlake")
                .email("johnnny@gmaill.com")
                .password("123")
                .isEnabled(true)
                .build();

//        when(this.userRepository.deleteById(1)).then();
    }

    @Test
    public void shouldUpdateUserByUserUsingUserId() {
        Role roleAdmin = Role.roleBuilder()
                .name("ADMIN")
                .createdAt(new Date())
                .build();

        UserUpdateRequest updateUser = UserUpdateRequest.userUpdateRequest()
                .firstName("Johnny")
                .lastName("Timberlake")
                .email("johnnny@gmaill.com")
                .password("123")
                .build();

        User user = User.userBuilder()
                .id(1)
                .firstName(updateUser.getFirstName())
                .lastName(updateUser.getLastName())
                .email(updateUser.getEmail())
                .password(updateUser.getPassword())
                .roles(List.of(roleAdmin))
                .isEnabled(true)
                .build();

        UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);

        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));
        when(this.userRepository.save(user)).thenReturn(user);
        when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        Response response = this.userService.updateUserByUserUsingUserId(1, updateUser);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getMessage()).isEqualTo("User with user id " + 1 + " is updated.");
        assertThat(response.getData()).isEqualTo(userResponse);
    }

    @Test
    public void shouldUpdateUserByAdminUsingUserId() {
        Role roleAdmin = Role.roleBuilder()
                .name("ADMIN")
                .createdAt(new Date())
                .build();

        UserUpdateRequestByAdmin updateUser = UserUpdateRequestByAdmin.userUpdateRequestByAdminBuilder()
                .firstName("Johnny")
                .lastName("Timberlake")
                .email("johnnny@gmaill.com")
                .isEnabled(true)
                .roles(List.of(roleAdmin))
                .build();

        User user = User.userBuilder()
                .id(1)
                .firstName(updateUser.getFirstName())
                .lastName(updateUser.getLastName())
                .email(updateUser.getEmail())
                .isEnabled(updateUser.isEnabled())
                .roles(updateUser.getRoles())
                .build();

        UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);

        when(this.userRepository.findById(1)).thenReturn(Optional.of(user));
        when(this.userRepository.save(user)).thenReturn(user);
        when(this.modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        Response response = this.userService.updateUserByAdminUsingUserId(1, updateUser);

        assertThat(response.isFlag()).isEqualTo(true);
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getMessage()).isEqualTo("User with user id " + 1 + " is updated.");
        assertThat(response.getData()).isEqualTo(userResponse);
    }
}