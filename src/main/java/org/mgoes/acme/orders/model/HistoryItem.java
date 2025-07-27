package org.mgoes.acme.orders.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_history")
public class HistoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cd_status")
    private String status;

    @Column (name = "ts_registered")
    private LocalDateTime timestamp;

    @Column (name = "ds_additional_info")
    private String additionalInfo;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "id_order", updatable = false, insertable = false)
    private Order order;

    @Column(name = "id_order")
    private String idOrder;
}
