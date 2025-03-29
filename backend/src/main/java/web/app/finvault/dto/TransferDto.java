package web.app.finvault.dto;


import lombok.Data;

@Data
public class TransferDto {

    private long recipientAccountNumber;
    private double amount;
    private String code;
    private String description;

}
