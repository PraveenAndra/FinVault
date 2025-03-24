package web.app.finvault.dto;


import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private long phoneNumber;
    private String gender;
    private Date dateOfBirth;

}
