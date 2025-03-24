package web.app.finvault.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.List;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    // Unique identifier for the account, generated using UUID strategy
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;

    // Current balance of the account
    private double balance;

    // Name of the account
    private String accountName;

    // Unique account number, marked as nullable false to ensure uniqueness
    @Column(unique = true, nullable = false)
    private long accountNumber;

    // Currency associated with the account
    private String currency;

    // Code associated with the account (e.g., ISO currency code)
    private String code;

    // Label or description for the account
    private String label;

    // Symbol associated with the account (e.g., currency symbol)
    private String symbol;

    // Timestamp of the last update to the account, automatically updated by Hibernate
    @UpdateTimestamp
    private long updatedAt;

    // Timestamp of the account creation, automatically updated by Hibernate
    @CreationTimestamp
    private long createdAt;

    // Owner of the account, mapped to the User entity using a foreign key
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore // To prevent infinite recursion during JSON serialization
    private User owner;

    // List of transactions associated with the account, mapped to the Transaction entity using a foreign key
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
