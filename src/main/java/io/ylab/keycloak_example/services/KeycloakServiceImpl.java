package io.ylab.keycloak_example.services;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import io.ylab.keycloak_example.core.dto.KeycloakServiceResponseDTO;
import io.ylab.keycloak_example.core.dto.UserCreateDTO;
import io.ylab.keycloak_example.services.api.IKeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements IKeycloakService {

    private final KeycloakProperties properties;
    private final Keycloak keycloak;
    private static final String FIO = "fio";
    private static final String ROLE = "role";

    @Override
    public KeycloakServiceResponseDTO<UserCreateDTO> createByUser(UserCreateDTO dto) {
        List<CredentialRepresentation> credentials = setCredentialRepresentations(dto);
        UserRepresentation user = setUserRepresentation(dto, credentials);

        RealmResource realm = keycloak.realm(properties.getRealm());
        UsersResource usersResource =  realm.users();

        Response response = usersResource.create(user);

        return new KeycloakServiceResponseDTO<>(dto, response);
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        RealmResource realm = keycloak.realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        UserResource userResource = usersResource.get(userId);
        return userResource.toRepresentation();
    }

    @Override
    public List<UserRepresentation> getUsers() {

        RealmResource realm = keycloak.realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        return usersResource.list();
    }

    @Override
    public UserRepresentation updateUser(String userId, UserCreateDTO dto) {
        //todo нужно бы сделать верификацию изменений через админа (запросом на мыло)
        RealmResource realmResource = keycloak.realm(properties.getRealm());
        UsersResource usersResource = realmResource.users();

        UserRepresentation userRepresentation = usersResource.get(userId).toRepresentation();
        if(!userRepresentation.getUsername().equals(dto.getLogin())) {
            //todo выкинуть исключение
        }

        List<CredentialRepresentation> credentials = setCredentialRepresentations(dto);

        // Обновление данных пользователя
        userRepresentation = setUserRepresentation(dto, credentials);

        usersResource.get(userId).update(userRepresentation);

        return userRepresentation;
    }

    /**
     * Sets up a UserRepresentation object based on the provided UserCreateDTO and credentials.
     * The role is assigned by user
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
        user.singleAttribute(ROLE, dto.getRole().name());
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
