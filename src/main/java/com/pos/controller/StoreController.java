package com.pos.controller;

import com.pos.configuration.JwtConstant;
import com.pos.domain.StoreStatus;
import com.pos.exception.UserException;
import com.pos.modal.Store;
import com.pos.modal.User;
import com.pos.payload.dto.StoreDto;
import com.pos.payload.mapper.StoreMapper;
import com.pos.payload.response.ApiResponse;
import com.pos.service.StoreService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;
    private final StoreMapper storeMapper;

    @PostMapping
    public ResponseEntity<StoreDto> crateStore(
            @RequestBody StoreDto storeDto,
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException {

        User user = userService.getCurrentUser();
        return ResponseEntity.ok(storeService.createStore(storeDto, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(
            @PathVariable(name = "id", required = true) Long id,
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStores(
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoreByAdmin(
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        return ResponseEntity.ok(storeMapper.toDto(storeService.getStoreByAdmin()));
    }

    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoreByEmployee(
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        return ResponseEntity.ok(storeService.getStoreByEmployee());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreByEmployee(
            @PathVariable(name = "id", required = true) Long id,
            @RequestBody StoreDto storeDto,
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        return ResponseEntity.ok(storeService.updateStore(id, storeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(
            @PathVariable(name = "id", required = true) Long id,
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        storeService.deleteStore(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Store deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}/moderate")
    public ResponseEntity<StoreDto> moderateStore(
            @PathVariable(name = "id", required = true) Long id,
            @RequestParam(name = "status") StoreStatus status,
            @RequestHeader(JwtConstant.JWT_HEADER) String jwt
    ) throws UserException, Exception {

        StoreDto store = storeService.moderateStore(id, status);
        return ResponseEntity.ok(store);
    }
}
