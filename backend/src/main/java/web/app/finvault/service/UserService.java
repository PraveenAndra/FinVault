package web.app.finvault.service;

import web.app.finvault.dto.UserDto;
import web.app.finvault.entity.User;

import java.util.Map;

public interface UserService {

    User registerUser(UserDto user);
    Map<String,Object> authenticateUser(UserDto user);

}
