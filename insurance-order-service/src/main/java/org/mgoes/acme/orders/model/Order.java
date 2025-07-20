package org.mgoes.acme.orders.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private LocalDateTime createdAt;

    @Column (name = "ts_finished")
    private LocalDateTime finishedAt;

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

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order")//,cascade = CascadeType.PERSIST) //
    private List<Coverage> coverages = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order")//,cascade = CascadeType.PERSIST)
    private List<Assistance> assistances = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order")//, cascade = CascadeType.PERSIST)
    private List<HistoryItem> history = new ArrayList<>();
}
