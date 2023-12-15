package io.ylab.keycloak_example.services;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserAuthorizeDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.core.enums.UserRole;
import io.ylab.keycloak_example.services.api.IKeycloakAccessService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeycloakAccessServiceImpl implements IKeycloakAccessService {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;
    private static final String FIO = "fio";
    private static final String ROLE = "role";

    @Override
    public KeycloakServiceResponseDTO<UserCreateDTO> register(UserCreateDTO dto) {
        List<CredentialRepresentation> credentials = setCredentialRepresentations(dto);
        UserRepresentation user = setUserRepresentation(dto, credentials);

        RealmResource realm = keycloak.realm(properties.getRealm());
        UsersResource usersResource =  realm.users();

        Response response = usersResource.create(user);

        return new KeycloakServiceResponseDTO<>(dto, response);
    }

    @Override
    public String authorize(UserAuthorizeDTO dto) {
        Keycloak userKeycloak = KeycloakBuilder.builder()
                .serverUrl(properties.getUrlAuth())
                .realm(properties.getRealm())
                .grantType(OAuth2Constants.PASSWORD)
                .username(dto.getLogin())
                .password(dto.getPassword())
                .clientId(properties.getAdminClientId())
                .clientSecret(properties.getAdminClientSecret())
                .build();
        return userKeycloak.tokenManager().getAccessTokenString();
    }

    /**
     * Sets up a UserRepresentation object based on the provided UserCreateDTO and credentials.
     * The role is assigned by system automatically. It"s "USER"
     *
     * @param dto The UserCreateDTO containing user details
     * @param credentials The list of CredentialRepresentation objects for the user
     * @return The configured UserRepresentation object
     */
    private static UserRepresentation setUserRepresentation(UserCreateDTO dto, List<CredentialRepresentation> credentials) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setEmailVerified(true);
        user.singleAttribute(FIO, dto.getFio());
        user.singleAttribute(ROLE, UserRole.USER.name());
        user.setCredentials(credentials);
        return user;
    }

    /**
     * Sets up CredentialRepresentation objects based on the provided UserCreateDTO.
     *
     * @param dto The UserCreateDTO containing user details
     * @return The list of CredentialRepresentation objects
     */
    private static List<CredentialRepresentation> setCredentialRepresentations(UserCreateDTO dto) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(dto.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        // Учётных данных (Credentials) может быть несколько, поэтому их нужно добавлять списком
        List<CredentialRepresentation> credentials = new ArrayList<>();
        credentials.add(credentialRepresentation);
        return credentials;
    }

}
