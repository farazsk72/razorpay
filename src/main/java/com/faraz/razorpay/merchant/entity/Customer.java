package com.faraz.razorpay.merchant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String contactNumber;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
