package com.oubao.service;

public interface MerchantService {


    void createMerchant(String merchantCode, String merchantKey, int transferMode);

    void updateMerchant(String merchantCode, String merchantKey, int transferMode);
}
