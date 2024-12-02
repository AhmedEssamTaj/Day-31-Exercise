package com.example.day31exercise.Controller;

import com.example.day31exercise.ApiResponse.ApiResponse;
import com.example.day31exercise.Model.Merchant;
import com.example.day31exercise.Service.MerchantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/merchant")
public class MerchantController {
    private final MerchantService merchantService;


    @GetMapping("/get-all")
    public ResponseEntity getMerchant() {
        return ResponseEntity.status(200).body(merchantService.getMerchant());
    }

    @PostMapping("/add")
    public ResponseEntity addMerchant (@RequestBody @Valid Merchant merchant, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        merchantService.addMerchant(merchant);
        return ResponseEntity.status(200).body(new ApiResponse("Merchant added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateMerchant (@PathVariable Integer id, @RequestBody @Valid Merchant merchant, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = merchantService.updateMerchant(id,merchant);
        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant updated successfully"));
        }
        else {
            return ResponseEntity.status(400).body(new ApiResponse("no Merchant with this id was found"));
        }


    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteMerchant (@PathVariable Integer id){
        boolean result = merchantService.deleteMerchant(id);

        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("Merchant deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("no Merchant with this id was found"));

    }
}
