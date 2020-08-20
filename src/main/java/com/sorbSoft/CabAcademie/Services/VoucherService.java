package com.sorbSoft.CabAcademie.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Log4j2
public class VoucherService {

    private List<String> tempStorage = new ArrayList<>();

    public String createVoucher() {
        String voucher = UUID.randomUUID().toString();
        tempStorage.add(voucher);

        return voucher;
    }

    public List<String> getAllVouchers() {
        return tempStorage;
    }

    public boolean verify(String voucherId) {
        return tempStorage.contains(voucherId);
    }

    public boolean apply(String voucherId) {
        return tempStorage.remove(voucherId);
    }

    public boolean delete(String voucherId) {
        return tempStorage.remove(voucherId);
    }
}

