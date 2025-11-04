package com.pos.payload.dto;

import com.pos.domain.StoreStatus;
import com.pos.modal.StoreContact;
import com.pos.modal.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreDto {

    private Long id;

    private String brand;

    private User storeAdmin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String description;

    private String storeType;

    private StoreStatus status;

    private StoreContact contact;
}
