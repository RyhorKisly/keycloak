package io.ylab.keycloak_example.services;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.core.dto.UserDTO;
import io.ylab.keycloak_example.core.dto.UserUpdateDTO;
import io.ylab.keycloak_example.core.enums.UserRole;
import io.ylab.keycloak_example.core.utils.KeycloakHandler;
import io.ylab.keycloak_example.services.api.KeycloakUserRoleService;
import io.ylab.keycloak_example.services.api.KeycloakUserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final KeycloakProperties properties;
    private final KeycloakUserRoleService roleService;
    private final KeycloakHandler keycloak;
    private final JwtDecoder jwtDecoder;
    private static final String FIO = "fio";

    @Override
    public KeycloakServiceResponseDTO<UserCreateDTO> create(UserCreateDTO dto) {
        List<CredentialRepresentation> credentials = setCredentialRepresentations(dto.getPassword());
        UserRepresentation user = setUserRepresentation(dto, credentials);

        RealmResource realm = keycloak.keycloak().realm(properties.getRealm());

        UsersResource usersResource =  realm.users();
        Response response = usersResource.create(user);

        Keycloak userKeycloak = setKeycloakUser(dto);
        String token = userKeycloak.tokenManager().getAccessTokenString();
        Jwt jwt = jwtDecoder.decode(token);
        String userId = jwt.getClaim("sub");

        roleService.addRoleToUser(UUID.fromString(userId), UserRole.USER);

        return new KeycloakServiceResponseDTO<>(dto, response);
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        RealmResource realm = keycloak.keycloak().realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        UserResource userResource = usersResource.get(userId.toString());
        UserRepresentation user = userResource.toRepresentation();

        return setUserDTO(user);
    }

    @Override
    public List<UserDTO> getUsers() {
        RealmResource realm = keycloak.keycloak().realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        List<UserRepresentation> users = usersResource.list();
        List<UserDTO> dtos = new ArrayList<>();

        for (UserRepresentation user : users) {
            dtos.add(setUserDTO(user));
        }

        return dtos;
    }

    @Override
    public UserDTO updateUser(UUID userId, UserUpdateDTO userUpdateDTO) {
        RealmResource realmResource = keycloak.keycloak().realm(properties.getRealm());
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = usersResource.get(userId.toString()).toRepresentation();

        List<CredentialRepresentation> credentials = setCredentialRepresentations(userUpdateDTO.getPassword());
        updateUserRepresentation(userUpdateDTO, credentials, user);
        usersResource.get(userId.toString()).update(user);

        String fio = setFioAttribute(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setFio(fio);
        userDTO.setLogin(user.getUsername());
        userDTO.setEmail(user.getEmail());

        roleService.updateRoleToUser(userId, userUpdateDTO.getRole());
        userDTO.setRole(userUpdateDTO.getRole());

        return userDTO;
    }

    /**
     * Sets up a UserRepresentation object based on the provided UserCreateDTO and credentials.
     * The role is assigned by user
     *
     * @param dto The UserCreateDTO containing user details
     * @param credentials The list of CredentialRepresentation objects for the user
     * @return The configured UserRepresentation object
     */
    private UserRepresentation setUserRepresentation(UserCreateDTO dto, List<CredentialRepresentation> credentials) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setEmailVerified(true);
        user.singleAttribute(FIO, dto.getFio());
        user.setCredentials(credentials);
        return user;
    }

    private Keycloak setKeycloakUser(UserCreateDTO dto) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.getUrlAuth())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .username(dto.getLogin())
                .password(dto.getPassword())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();
    }

    private UserDTO setUserDTO(UserRepresentation user) {
        String fio = setFioAttribute(user);
        UserRole role = roleService.getRole(UUID.fromString(user.getId()));

        UserDTO dto = new UserDTO();
        dto.setFio(fio);
        dto.setLogin(user.getUsername());
        dto.setEmail(user.getEmail());
        if(role != null) {
            dto.setRole(role);
        }
        return dto;
    }

    private void updateUserRepresentation(
            UserUpdateDTO dto, List<CredentialRepresentation> credentials, UserRepresentation user
    ) {
        user.setEnabled(true);
        user.setUsername(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setEmailVerified(true);
        user.singleAttribute(FIO, dto.getFio());
        user.setCredentials(credentials);
    }

    /**
     * Sets up CredentialRepresentation objects based on the provided UserCreateDTO.
     *
     * @param password of the User containing user details
     * @return The list of CredentialRepresentation objects
     */
    private List<CredentialRepresentation> setCredentialRepresentations(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        // Учётных данных (Credentials) может быть несколько, потому их нужно добавлять списком
        List<CredentialRepresentation> credentials = new ArrayList<>();
        credentials.add(credentialRepresentation);
        return credentials;
    }

    /**
     * Retrieves the FIO attribute from the user representation.
     *
     * @param user The user representation
     * @return The value of the FIO attribute
     */
    private static String setFioAttribute(UserRepresentation user) {
        Map<String, List<String>> attributes = user.getAttributes();
        List<String> attributeFio;
        String fio = "";
        if(attributes != null) {
            if (attributes.containsKey("fio")) {
                attributeFio = attributes.get("fio");
                fio = attributeFio.get(0);
            }
        }
        return fio;
    }

}
