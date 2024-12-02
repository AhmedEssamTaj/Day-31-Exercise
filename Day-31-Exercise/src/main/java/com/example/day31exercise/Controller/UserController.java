package com.example.day31exercise.Controller;

import com.example.day31exercise.ApiResponse.ApiResponse;
import com.example.day31exercise.Model.Merchant;
import com.example.day31exercise.Model.Product;
import com.example.day31exercise.Model.User;
import com.example.day31exercise.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all")
    public ResponseEntity getUsers() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @PostMapping("/add")
    public ResponseEntity addUser (@RequestBody @Valid User user, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("user added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateUser (@PathVariable Integer id, @RequestBody @Valid User user, Errors errors){

        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean result = userService.updateUser(id,user);
        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
        }
        else {
            return ResponseEntity.status(400).body(new ApiResponse("no user with this id was found"));
        }


    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser (@PathVariable Integer id){
        boolean result = userService.deleteUser(id);

        if (result){
            return ResponseEntity.status(200).body(new ApiResponse("user deleted successfully"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("no user with this id was found"));

    }

    // ========================= EXTRA Endpoint 1 =========================\\
    // endpoint to add products to the cart
    @PutMapping("/add-to-cart/{userId}/{productId}/{merchantId}")
    public ResponseEntity addToCart (@PathVariable Integer userId, @PathVariable Integer productId, @PathVariable Integer merchantId){
        int result = userService.addToCart(userId,productId,merchantId);
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("product added to cart successfully"));
        }
        if (result == 0){
            return ResponseEntity.status(400).body(new ApiResponse("no user with this id was found"));
        }
        if (result == -1){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not have stock for this product"));
        }
        if (result == -2){
            return ResponseEntity.status(400).body(new ApiResponse("product does not exist"));
        }
        if (result == -3){
            return ResponseEntity.status(400).body(new ApiResponse("product is out of stock"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("something went wrong!"));
    }

    // ========================= EXTRA Endpoint 2 =========================\\
    // Endpoint to checkout the cart
    @PutMapping("/checkout/{userId}")
    public ResponseEntity checkout(@PathVariable Integer userId) {
        int result = userService.checkout(userId);

        if (result == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Checkout successful! All items purchased."));
        }
        if (result == -5) { // Partial failure
            return ResponseEntity.status(200).body(new ApiResponse("Checkout partially successful. Some items could not be purchased."));
        }
        if (result == -6) {
            return ResponseEntity.status(400).body(new ApiResponse("cart is empty. Please add items to your cart before checkout."));
        }
        if (result == -7) {
            return ResponseEntity.status(400).body(new ApiResponse("Checkout failed. No items in your cart could be purchased."));
        }
        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("No user with this ID was found."));
        }
        return ResponseEntity.status(400).body(new ApiResponse("Something went wrong during checkout!"));
    }

    // endpoint for user to buy products
    @PutMapping("/buy/{userId}/{productId}/{merchantId}")
    public ResponseEntity buyProduct (@PathVariable Integer userId, @PathVariable Integer productId, @PathVariable Integer merchantId){
        int result = userService.buyProduct(userId,productId,merchantId);
        if (result == 1){
            return ResponseEntity.status(200).body(new ApiResponse("user buy product successfully"));
        }
        if (result == 0){
            return ResponseEntity.status(400).body(new ApiResponse("no user with this id was found"));
        }
        if (result == -1){
            return ResponseEntity.status(400).body(new ApiResponse("merchant does not have stock"));
        }
        if (result == -2){
            return ResponseEntity.status(400).body(new ApiResponse("product does not exist"));
        }
        if (result == -3){
            return ResponseEntity.status(400).body(new ApiResponse("product is out of stock"));
        }
        if (result == -4){
            return ResponseEntity.status(400).body(new ApiResponse("user does not have enough balance"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("something went wrong!"));
    }

    // ========================= EXTRA Endpoint 3 =========================\\
    //endpoint to for user to add rating to a product
    @PostMapping("/add-rating/{userId}/{productId}/{rating}")
    public ResponseEntity addRating (@PathVariable Integer userId, @PathVariable Integer productId, @PathVariable Double rating){
        int result = userService.addRating(userId,productId,rating);
        if (result == 1) {
            return ResponseEntity.status(200).body(new ApiResponse("Rating added successfully"));
        }
        if (result == 0) {
            return ResponseEntity.status(400).body(new ApiResponse("no user with this id was found"));
        }
        if (result == -1) {
            return ResponseEntity.status(400).body(new ApiResponse("product does not exist"));
        }
        if (result == -2) {
            return ResponseEntity.status(400).body(new ApiResponse("user is not customer"));
        }
        if (result == -3) {
            return ResponseEntity.status(400).body(new ApiResponse("rating must be between 1 and 5"));
        }
//        if (result == -4) {
//            return ResponseEntity.status(400).body(new ApiResponse("user did not buy product"));
//        }
        return ResponseEntity.status(400).body(new ApiResponse("something went wrong!"));

    }

    // ========================= EXTRA Endpoint 5 =========================\\
    @GetMapping("/get-similar/{userId}")
    public ResponseEntity generateSimilarProducts(@PathVariable Integer userId){
        List<Product> similarProducts = userService.generateSimilarProducts(userId);
        if (similarProducts == null) {
            return ResponseEntity.status(400).body(new ApiResponse("history not found"));
        }
        return ResponseEntity.status(200).body(similarProducts);
    }


}
