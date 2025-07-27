package org.mgoes.acme.orders.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tb_coverage")
public class Coverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ds_name")
    private String name;

    @Column (name = "vl_amount", precision = 9, scale = 2)
    private BigDecimal amount;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_order")
    private Order order;
}
