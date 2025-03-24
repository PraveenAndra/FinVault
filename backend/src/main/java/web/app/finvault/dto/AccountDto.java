package web.app.finvault.dto;


import lombok.Data;

@Data
public class AccountDto {
    private String accountId;
    private String code;
    private String label;

    private char symbol;

}
