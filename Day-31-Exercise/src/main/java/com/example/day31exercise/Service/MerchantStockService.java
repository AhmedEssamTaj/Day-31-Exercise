package com.example.day31exercise.Service;

import com.example.day31exercise.Model.Merchant;
import com.example.day31exercise.Model.MerchantStock;
import com.example.day31exercise.Model.Product;
import com.example.day31exercise.Repository.MerchantRepository;
import com.example.day31exercise.Repository.MerchantStockRepository;
import com.example.day31exercise.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MerchantStockService {

    private final MerchantStockRepository merchantStockRepository;
    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;

    // READ
    public List<MerchantStock> getMerchantStock () {
        return merchantStockRepository.findAll();
    }

    // CREATE
    public int addMerchantStock (MerchantStock merchantStock){
        boolean productExists = false;
        boolean merchantExists = false;
        List<Merchant> merchants = merchantRepository.findAll();
        List<Product> products = productRepository.findAll();

        // check if product id exist
        for (Product p : products){
            if(p.getProduct_id().equals(merchantStock.getProduct_id())){
                productExists = true;
            }
        }

        // check if merchant exist
        for (Merchant m : merchants){
            if(m.getMerchant_id().equals(merchantStock.getMerchant_id())){
                merchantExists = true;
            }
        }
        if (!productExists){
            return -1;
        }else if (!merchantExists){
            return -2;
        }else {
            merchantStockRepository.save(merchantStock);
            return 1;
        }

    }

    // UPDATE
    public int updateMerchantStock (Integer id, MerchantStock merchantStock){

        MerchantStock oldMerchant = merchantStockRepository.getById(id);
        boolean productExists = false;
        boolean merchantExists = false;
        List<Merchant> merchants = merchantRepository.findAll();
        List<Product> products = productRepository.findAll();

        for (Product p : products){
            if(p.getProduct_id().equals(merchantStock.getProduct_id())){
                productExists = true;
            }
        }
        for (Merchant m : merchants){
            if(m.getMerchant_id().equals(merchantStock.getMerchant_id())){
                merchantExists = true;
            }
        }

        if (!productExists){
            return -1;
        }
        if (!merchantExists){
            return -2;
        }

        oldMerchant.setMerchant_id(merchantStock.getMerchant_id());
        oldMerchant.setProduct_id(merchantStock.getProduct_id());
        oldMerchant.setStock(merchantStock.getStock());
        merchantStockRepository.save(oldMerchant);
        return 1 ;
    }

    // Delete
    public boolean deleteMerchantStock (Integer id ){
        MerchantStock oldMerchantStock = merchantStockRepository.getById(id);
        if (oldMerchantStock == null){
            return false;
        }
        merchantStockRepository.delete(oldMerchantStock);
        return true;
    }

    // method to add more stocks
    public int addMoreStocks (Integer merchantId, Integer productId, Integer stock){
        List<MerchantStock> merchantStock = merchantStockRepository.findAll();
        boolean merchantStockExist = false;
        boolean productExists = false;


        Merchant merchant = merchantRepository.getById(merchantId);
        if (merchant == null){
            return 0;
        }

        for (MerchantStock m : merchantStock){
            if (m.getMerchant_id().equals(merchantId)){
                merchantStockExist = true; // change it to true becuse this merchant has stock
                if (m.getProduct_id().equals(productId)){
                    productExists = true;
                }
            }
        }
        if (!merchantStockExist){
            return -1; // -1  --> no stock for this merchant
        }
        if (!productExists){
            return -2; // -2 --> this merchant does not have  stock for this product
        }
        for (MerchantStock m : merchantStock){
            if (m.getProduct_id().equals(productId) && m.getMerchant_id().equals(merchantId) ){
                m.setStock(stock);
                merchantStockRepository.save(m);
                return 1; // 1 --> stock is updated successfully
            }
        }
        return 5;

    }

    // ========================= EXTRA METHOD 4 =========================\\
    // discount all the products under this merchant
    public int discountProductsByMerch (Integer merchantId,Integer percentage){
        if (percentage <=0 || percentage > 100){
            return-1;// percentage must be between 1 and 100
        }
        List<Merchant> merchants = merchantRepository.findAll();

        boolean merchantExists = false;
        for (Merchant m : merchants){
            if (m.getMerchant_id().equals(merchantId)){
                merchantExists = true;
            }
        }
        if (!merchantExists){
            return 0; // merchant does not have any product in stock
        }
        for (MerchantStock m : merchantStockRepository.findAll()){
            if (m.getMerchant_id().equals(merchantId)){
                for (Product p : productRepository.findAll()){
                    if (p.getProduct_id().equals(m.getProduct_id())){
                        Double discountedPrice = p.getProduct_price()-(p.getProduct_price() * ((double) percentage /100));
                        p.setProduct_price(discountedPrice);
                        productRepository.save(p);
                        break;
                    }
                }
            }

        }
        return 1;
    }


}
