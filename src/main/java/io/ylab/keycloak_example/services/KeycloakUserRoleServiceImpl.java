package io.ylab.keycloak_example.services;

import io.ylab.keycloak_example.config.properties.KeycloakProperties;
import io.ylab.keycloak_example.core.enums.UserRole;
import io.ylab.keycloak_example.core.utils.KeycloakHandler;
import io.ylab.keycloak_example.services.api.KeycloakUserRoleService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeycloakUserRoleServiceImpl implements KeycloakUserRoleService {
    private final KeycloakProperties properties;
    private final KeycloakHandler keycloak;
    private RoleRepresentation role;
    @Override
    public UserRole getRole(UUID userId) {
        RealmResource realm = keycloak.keycloak().realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        UserResource userResource = usersResource.get(userId.toString());

        ClientRepresentation clientRep = realm.clients().findByClientId(properties.getClientId()).get(0);
        List<RoleRepresentation> clientRoles = userResource.roles().clientLevel(clientRep.getId()).listAll();

        return getRoleIfExist(clientRoles);
    }

    @Override
    public Optional<UserRole> addRoleToUser(UUID userId, UserRole userRole) {
        RealmResource realm = keycloak.keycloak().realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        UserResource userResource = usersResource.get(userId.toString());
        ClientRepresentation clientRep = realm.clients().findByClientId(properties.getClientId()).get(0);
        List<RoleRepresentation> clientRoles = userResource.roles().clientLevel(clientRep.getId()).listAll();

        if(checkRoleIfExist(clientRoles)) {
            return Optional.empty();
        } else {
            role = realm.clients().get(clientRep.getId()).roles().get(userRole.name()).toRepresentation();
            userResource.roles().clientLevel(clientRep.getId()).add(Collections.singletonList(role));
            return Optional.of(userRole);
        }
    }

    @Override
    public void updateRoleToUser(UUID userId, UserRole userRole) {
        RealmResource realm = keycloak.keycloak().realm(properties.getRealm());
        UsersResource usersResource = realm.users();
        UserResource userResource = usersResource.get(userId.toString());
        ClientRepresentation clientRep = realm.clients().findByClientId(properties.getClientId()).get(0);
        List<RoleRepresentation> clientRoles = userResource.roles().clientLevel(clientRep.getId()).listAll();

        deleteRoleIfExist(clientRoles, realm, userResource, clientRep);

        role = realm.clients().get(clientRep.getId()).roles().get(userRole.name()).toRepresentation();
        userResource.roles().clientLevel(clientRep.getId()).add(Collections.singletonList(role));
    }

    private UserRole getRoleIfExist(List<RoleRepresentation> clientRoles) {
        for (UserRole value : UserRole.values()) {
            for (RoleRepresentation clientRole : clientRoles) {
                if (clientRole.getName().equals(value.name())) {
                    return value;
                }
            }
        }
        return null;
    }

    private boolean checkRoleIfExist(List<RoleRepresentation> clientRoles) {
        for (UserRole value : UserRole.values()) {
            for (RoleRepresentation clientRole : clientRoles) {
                if (clientRole.getName().equals(value.name())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void deleteRoleIfExist(
            List<RoleRepresentation> clientRoles,
            RealmResource realm,
            UserResource userResource,
            ClientRepresentation clientRep
    ) {
        for (UserRole value : UserRole.values()) {
            for (RoleRepresentation clientRole : clientRoles) {
                if (clientRole.getName().equals(value.name())) {
                    role = realm.clients().get(clientRep.getId()).roles().get(clientRole.getName()).toRepresentation();
                    userResource.roles().clientLevel(clientRep.getId()).remove(Collections.singletonList(role));
                }
            }
        }
    }
}

