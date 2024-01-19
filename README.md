Команды в терминале кейклока для экспорта данных:

- cd /opt/keycloak/bin
- pwd
- ls
- ./kc.sh export --file /tmp/keycloak.json

Далее копируем файл keycloak.json в папку "keycloak", которая расположена в корневой директории проекта с целью дальнейшего импорта данных обратно в новый контейнер кейклока.
_____________


Необходимые настройки keycloak при отсутствии импорта:

1. Создание realm;
2. Создание claims;
3. Получение информации о secret key клиента
4. Добавить урлы: root url, home url, valid redirect urls, web origins, admin url;
5. Добавление ролей пользователей в client, (при необходимости realm). 
6. Bзменения в capability config: on/off client authentication, client account role, direct access grant, standart flow, implicit flow и т.д.;
7. В service accounts roles обязательно добавить (realm-admin - даёт права от realm проводить любые действия от лица текущего client);
8. Добавить/удалить мапперы для создания атрибутов пользователя в токене (client scopes -> clientId -> add mapper)
9. Выбрать алгоритм шифрования токена (при необходимости)
10. Увеличить время жизни access token and refresh token.

Пункт 4 получился захардкоженным. В зависимости от того как поднять (локально или в докер) там необходимо менять url.
_____________

Запуск приложения:

Для запуска приложения в docker, необходимо предварительно выполнить команду в консоли корневой папки проекта: "mvn clean package", затем "docker-compose-up".

Для запуска самого сервиса взаимодейчтвия с keycloak локально, необходимо запустить исполняемый файл:
"keycloak_example/src/main/java/io/ylab/keycloak_example/KeycloakExampleApplication.java" и указать profile: local
