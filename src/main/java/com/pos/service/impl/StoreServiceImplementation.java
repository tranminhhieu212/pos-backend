package com.pos.service.impl;

import com.pos.domain.StoreStatus;
import com.pos.exception.UserException;
import com.pos.modal.Store;
import com.pos.modal.StoreContact;
import com.pos.modal.User;
import com.pos.payload.dto.StoreDto;
import com.pos.payload.mapper.StoreMapper;
import com.pos.repository.StoreRepository;
import com.pos.service.StoreService;
import com.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImplementation implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;
    private final StoreMapper storeMapper;

    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = storeMapper.toEntity(storeDto);
        store.setStoreAdmin(user);
        return storeMapper.toDto(storeRepository.save(store));
    }

    @Override
    public StoreDto getStoreById(Long id) throws Exception {

        Store store = storeRepository.findById(id).orElseThrow(() -> new Exception("Store not found!"));
        return storeMapper.toDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {

        return storeRepository.findAll().stream().map(storeMapper::toDto).toList();
    }

    @Override
    public Store getStoreByAdmin() throws Exception, UserException {

        User admin = userService.getCurrentUser();
        if(admin == null) throw new UserException("Admin not exist");
        Store store = storeRepository.findByStoreAdminId(admin.getId());
        if (store == null) throw new Exception("Store not exist");
        return store;
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) throws UserException, Exception {

        User user = userService.getCurrentUser();
        Store existedStore = storeRepository.findByStoreAdminId(user.getId());

        if(existedStore == null) throw new Exception("Store not found");

        storeMapper.updateFromDto(storeDto, existedStore);

        if(storeDto.getStoreType() != null) {
            existedStore.setStoreType(String.valueOf(storeDto.getStoreAdmin()));
        }

        if(storeDto.getContact() != null) {
            StoreContact storeContact = StoreContact.builder()
                    .address(storeDto.getContact().getAddress())
                    .email(storeDto.getContact().getEmail())
                    .phone(storeDto.getContact().getPhone())
                    .build();

            existedStore.setContact(storeContact);
        }

        return storeMapper.toDto(storeRepository.save(existedStore));
    }

    @Override
    public StoreDto getStoreByEmployee() throws UserException {

        User user = userService.getCurrentUser();
        if(user == null) throw new UserException("Permission denied");

        return storeMapper.toDto(user.getStore());
    }

    @Override
    public void deleteStore(Long id) throws UserException, Exception {

        Store store = getStoreByAdmin();
        if(store == null) throw new Exception("Store not found!");

        storeRepository.deleteById(id);
    }

    @Override
    public StoreDto moderateStore(Long id, StoreStatus status) throws Exception {

        Store store = storeRepository.findById(id).orElseThrow(() -> new Exception("Store not found!"));
        store.setStatus(status);

        return storeMapper.toDto(storeRepository.save(store));
    }
}
