package com.example.bookexchange.data.model.files;

import com.example.bookexchange.data.model.enums.TransactionType;
import com.example.bookexchange.data.model.system.Auditable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transaction")
public class Transaction extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_SEQUENCE")
    @SequenceGenerator(sequenceName = "TRANSACTION_SEQUENCE", allocationSize = 1, name = "TRANSACTION_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private Enum<TransactionType> transactionType;
    private Integer amount;

}
