package org.mgoes.acme.orders.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    private UUID id;

    @Column (name = "cd_status")
    private String status;

    @Column (name = "ts_created")
    private LocalDateTime created;

    @Column (name = "ts_finished")
    private LocalDateTime finished;

    @Column (name = "id_customer")
    private UUID customerId;

    @Column (name = "id_product")
    private Long productId;

    @Column (name = "cd_category")
    private String category;

    @Column (name = "cd_payment_method")
    private String paymentMethod;

    @Column (name = "cd_sales_channel")
    private String salesChannel;

    @Column (name = "vl_total_monthly_premium", precision = 9, scale = 2)
    private BigDecimal totalMonthlyPremiumAmount;

    @Column (name = "vl_insured", precision = 9, scale = 2)
    private BigDecimal insuredAmount;

    @OneToMany(mappedBy = "order")
    private List<Coverage> coverages;

    @OneToMany(mappedBy = "order")
    private List<Assistance> assistances;

    @OneToMany(mappedBy = "order")
    private List<HistoryItem> history;
}
