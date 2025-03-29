package web.app.finvault.dto;


import lombok.Data;

@Data
public class ConvertDto {

    private String fromCurrencyCode;
    private String toCurrencyCode;
    private double amount;
}
