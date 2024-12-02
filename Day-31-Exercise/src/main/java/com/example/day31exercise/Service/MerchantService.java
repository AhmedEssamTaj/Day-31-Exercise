package com.example.day31exercise.Service;

import com.example.day31exercise.Model.Merchant;
import com.example.day31exercise.Repository.MerchantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class MerchantService {
    private MerchantRepository merchantRepository;


   // READ
    public List<Merchant> getMerchant () {
        return merchantRepository.findAll();
    }

   // CREATE
    public void addMerchant (Merchant merchant){
        merchantRepository.save(merchant);
    }

    // UPDATE
    public boolean updateMerchant (Integer id, Merchant merchant){
        Merchant oldMerchant = merchantRepository.getById(id);
        if(oldMerchant != null){
            oldMerchant.setMerchant_name(merchant.getMerchant_name());
            merchantRepository.save(oldMerchant);
            return true;
        }
        return false;
    }

    // DELETE
    public boolean deleteMerchant (Integer id ){
      Merchant oldMerchant = merchantRepository.getById(id);

        if(oldMerchant != null){
            merchantRepository.delete(oldMerchant);
            return true;
        }
        return false;
    }

}
