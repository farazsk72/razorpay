package com.faraz.razorpay.merchant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchant_webhook_config")
public class MerchantWebhookConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false, length = 500)
    private String targetUrl;   // www.zara.com/webhook/success

    @Column(length = 200)
    private String webhookSecretHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(length = 300)
    private String eventTypes;

}
