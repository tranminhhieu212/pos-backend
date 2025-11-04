package com.pos.service;

import com.pos.domain.StoreStatus;
import com.pos.exception.UserException;
import com.pos.modal.Store;
import com.pos.modal.User;
import com.pos.payload.dto.StoreDto;

import java.util.List;

public interface StoreService {

    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById (Long id) throws Exception;
    List<StoreDto> getAllStores();
    Store getStoreByAdmin() throws Exception, UserException;
    StoreDto updateStore(Long id, StoreDto storeDto) throws UserException, Exception;
    StoreDto getStoreByEmployee() throws UserException;
    void deleteStore(Long id) throws UserException, Exception;
    StoreDto moderateStore(Long id, StoreStatus status) throws Exception;
}
