package org.mgoes.acme.orders.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    private String id;

    @Column (name = "cd_status")
    private String status;

    @Column (name = "cd_classification")
    private RiskClassification classification;

    @Column (name = "ts_created")
    private LocalDateTime createdAt;

    @Column (name = "ts_finished")
    private LocalDateTime finishedAt;

    @Column (name = "id_customer")
    private String customerId;

    @Column (name = "id_product")
    private Long productId;

    @Column (name = "cd_category")
    private InsuranceCategory category;

    @Column (name = "cd_payment_method")
    private String paymentMethod;

    @Column (name = "cd_sales_channel")
    private String salesChannel;

    @Column (name = "vl_total_monthly_premium", precision = 9, scale = 2)
    private BigDecimal totalMonthlyPremiumAmount;

    @Column (name = "vl_insured", precision = 9, scale = 2)
    private BigDecimal insuredAmount;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Coverage> coverages = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order",  fetch = FetchType.EAGER)
    private List<Assistance> assistances = new ArrayList<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "order",  fetch = FetchType.EAGER)
    private List<HistoryItem> history = new ArrayList<>();

    @Transient
    private OrderState state;

    @PostLoad
    private void loadState(){
        if(this.status != null)
            this.state = OrderState.valueOf(status);
    }

    public void setState(OrderState newState) {

        if(this.state == null && !newState.equals(OrderState.RECEIVED))
            throw new IllegalOrderStateTransitionException("Initial state must be RECEIVED");

        if(this.state != null && !this.state.mayTransitionTo(newState))
            throw new IllegalOrderStateTransitionException("Invalid transition");

        this.state = newState;
        this.status = newState.name();
    }

    public void setInitialState() {
        setState(OrderState.RECEIVED);
    }

    public void setStatus(String status){
        if(Objects.nonNull(this.status))
            throw new IllegalOrderStateTransitionException("You should use setState instead of setting status directly");
        this.state = OrderState.valueOf(status);
    }
}
