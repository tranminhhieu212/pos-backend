package com.pos.modal;

import com.pos.domain.StoreStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @OneToOne
    private User storeAdmin; // -> store_admin_id;

    private LocalDateTime createdAt; // -> created_at
    private LocalDateTime updatedAt; // -> updated_dt

    private String description;

    private String storeType;

    private StoreStatus status;

    @Embedded
    private StoreContact contact = new StoreContact(); // -> address, email, phone

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StoreStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
