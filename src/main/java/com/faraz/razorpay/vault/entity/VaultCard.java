package com.faraz.razorpay.vault.entity;

import com.faraz.razorpay.common.entity.BaseEntity;
import com.faraz.razorpay.common.enums.CardBrand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vault_card")
public class VaultCard extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 4)
    private String lastFour;

    @Column(nullable = false, length = 6)
    private String bin;

    @Column(nullable = false)
    private byte[] encryptedPan;

    @Column(nullable = false)
    private byte[] encryptedDek;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardBrand brand;   // VISA. RUPAY

    @Column(nullable = false)
    private String expriyMonth;

    @Column(nullable = false)
    private String expriyYear;

    @Column(nullable = false)
    private String cardHolderName;

    private LocalDateTime deletedAt;


}
