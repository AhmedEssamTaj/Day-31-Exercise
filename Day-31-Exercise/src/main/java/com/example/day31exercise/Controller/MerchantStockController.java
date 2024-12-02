package com.example.day31exercise.Controller;

import com.example.day31exercise.ApiResponse.ApiResponse;
import com.example.day31exercise.Model.MerchantStock;
import com.example.day31exercise.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/merchant-stock")
public class MerchantStockController {
    private final MerchantStockService merchantStockService;


    @GetMapping("/get-all")
    public ResponseEntity getMerchantStock() {
        return ResponseEntity.status(200).body(merchantStockService.getMerchantStock());
    }

    // endpoint to add a new MerchantStock
    @PostMapping("/add")
    public ResponseEntity addMerchantStock (@RequestBody @Valid MerchantStock merchantStock, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        int result = merchantStockService.addMerchantStock(merchantStock);
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStock added successfully"));
        }else if (result == -1){
            return ResponseEntity.status(400).body(new ApiResponse("product does not exist! MerchantStock not added successfully"));
        }else if (result == -2){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not exist! merchantStock not added successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("something went wrong"));

    }

    // endpoint to update existing MerchantStock
    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchantStock (@PathVariable Integer id, @RequestBody @Valid MerchantStock merchantStock, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        int result = merchantStockService.updateMerchantStock(id,merchantStock);
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStock updated successfully"));
        }
        else if (result == -2){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not exist! merchantStock not added successfully"));
        }else if (result == -1){
            return ResponseEntity.status(400).body(new ApiResponse("product does not exist! MerchantStock not added successfully"));
        }else {
            return ResponseEntity.status(400).body(new ApiResponse("something went wrong"));
        }


    }

    // endpoint to delete a MerchantStock
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchantStock (@PathVariable Integer id){
        boolean result = merchantStockService.deleteMerchantStock(id);

        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("MerchantStock deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("no MerchantStock with this id was found"));

    }


    // endpoint for merchant to add more stocks
    @PutMapping("/update-stocks/{merchantId}/{productId}/{stocks}")
    public ResponseEntity AddMoreStocks (@PathVariable Integer merchantId, @PathVariable Integer productId,@PathVariable Integer stocks){

        int result = merchantStockService.addMoreStocks(merchantId,productId,stocks);
        if (result == 0){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not exist"));
        }
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("stocks added successfully"));
        }
        if (result == -1){
            return ResponseEntity.status(400).body(new ApiResponse("No stocks for this merchant "));

        }
        if (result == -2){
            return ResponseEntity.status(400).body(new ApiResponse("this merchant does not have  stock for this product"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("somthing went wrong!"));

    }

    // ========================= EXTRA Endpoint 4 =========================\\
    // endpoint to discount all the products in stock for a merchant
    @PutMapping("/discount-merch/{merchantId}/{percentage}")
    public ResponseEntity discountProductsByMerch (@PathVariable Integer merchantId, @PathVariable Integer percentage){
        int result = merchantStockService.discountProductsByMerch(merchantId,percentage);
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("discount applied successfully on all products"));
        }
        if (result == 0){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not have any product in stock"));
        }
        if (result == -1){
            return ResponseEntity.status(400).body(new ApiResponse("percentage must be between 1 and 100 "));
        }
        return ResponseEntity.status(400).body(new ApiResponse("something went wrong"));
    }
}


