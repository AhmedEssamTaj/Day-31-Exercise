package com.example.day31exercise.Service;

import com.example.day31exercise.Model.*;
import com.example.day31exercise.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final MerchantStockRepository merchantStockRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final HistoryRepository historyRepository;
    // READ
    public List<User> getUsers() {

        return userRepository.findAll();
    }

    // CREATE
    public void addUser(User user) {
        userRepository.save(user);
    }

    // UPDATE
    public boolean updateUser(Integer id, User user) {

        User oldUser = userRepository.findById(id).get();

        if (oldUser != null) {
           oldUser.setUser_name(user.getUser_name());
           oldUser.setPassword(user.getPassword());
           oldUser.setEmail(user.getEmail());
           oldUser.setEmail(user.getEmail());
           oldUser.setBalance(user.getBalance());
           userRepository.save(oldUser);

            return true;
        }
        return false;
    }

    // DELETE
    public boolean deleteUser(Integer id) {
        User oldUser = userRepository.findById(id).get();

        if(oldUser != null){
            userRepository.delete(oldUser);
            return true;
        }
        return false;
    }

    // method to buy a product
    public int buyProduct (Integer userId ,Integer productId, Integer merchantId){

        boolean userIdExists = false;
        boolean productExists = false;
        boolean merchantIdExists = false;
        int userRefId =-1;
        int productRefId =-1;
        int merchantStockRefId =-1;

        for (User u : userRepository.findAll()) {
            if (u.getUser_id().equals(userId)){
                userIdExists = true;
                userRefId = u.getUser_id();
            }
        }
        if (!userIdExists){
            return 0; // 0 --> user does not exist
        }

        for (MerchantStock m : merchantStockRepository.findAll()) {
            if (m.getMerchant_id().equals(merchantId) && m.getProduct_id().equals(productId)){
                merchantIdExists = true;
                merchantStockRefId= m.getMerchant_stock_id();
                if(m.getStock() <1){
                    return -3; // product is out of stock
                }
            }
        }
        if (!merchantIdExists){
            return -1; // merchant does not exist
        }

        for (Product p : productRepository.findAll()) {
            if (p.getProduct_id().equals(productId)){
                productExists = true;
                productRefId = p.getProduct_id();
            }
        }
        if (!productExists){
            return -2; // product does not exist
        }

        if (userRepository.getReferenceById(userRefId).getBalance() < productRepository.getReferenceById(productRefId).getProduct_id()){
            return -4; // user does not have enough balance
        }

        User oldUser = userRepository.getReferenceById(userRefId);
        oldUser.setBalance(oldUser.getBalance() - productRepository.getReferenceById(productRefId).getProduct_price());
        userRepository.save(oldUser);

        MerchantStock oldMerchantStock = merchantStockRepository.getReferenceById(merchantStockRefId);
        oldMerchantStock.setStock(oldMerchantStock.getStock() - 1);
        merchantStockRepository.save(oldMerchantStock);

        History history = new History();
        history.setProduct_id(productRefId);
        history.setUser_id(userRefId);
        historyRepository.save(history);
        return 1; // product bought successfully

    }

    // ========================= EXTRA METHOD 1 =========================\\
    // method to add product to cart
    public int addToCart(Integer userId ,Integer productId, Integer merchantId){
        boolean userIdExists = false;
        boolean productExists = false;
        boolean merchantIdExists = false;
        int userRefId =-1;
        int productRefId =-1;
        int merchantStockRefId =-1;
        for (User u : userRepository.findAll()) {
            if (u.getUser_id().equals(userId)){
                userIdExists = true;
                userRefId= u.getUser_id();
            }
        }
        if (!userIdExists){
            return 0;// user does not exist
        }
        for (MerchantStock m : merchantStockRepository.findAll()) {
            if (m.getMerchant_id().equals(merchantId) && m.getProduct_id().equals(productId)){
                merchantIdExists = true;
                merchantStockRefId= m.getMerchant_stock_id();
            }
        }
        if (!merchantIdExists){
            return -1; // merchent does not have stock for this product
        }
        for (Product p : productRepository.findAll()) {
            if (p.getProduct_id().equals(productId)){
                productExists = true;
                productRefId = p.getProduct_id();
            }
        }
        if (!productExists){
            return -2;// product does not exist
        }
        if (merchantStockRepository.getReferenceById(merchantStockRefId).getStock() == 0){
            return -3;
        }
//        users.get(userIndex).getCart().add(productService.products.get(productIndex));
        Cart cart = new Cart();
        cart.setUser_id(userId);
        cart.setProduct_id(productId);
        cartRepository.save(cart);
        return 1;
    }

    // ========================= EXTRA METHOD 2 =========================\\
    // method to checkout cart
    public int checkout(Integer userId) {
        // Check if user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0; // User does not exist
        }


        List<Cart> allCarts = cartRepository.findAll();
        List<Cart> userCart = new ArrayList<>();
        for (Cart cart : allCarts) {
            if (cart.getUser_id().equals(userId)) {
                userCart.add(cart);
            }
        }
        if (userCart.isEmpty()) {
            return -6; // Cart is empty
        }

        boolean anySuccessful = false;
        List<Cart> failedProducts = new ArrayList<>();


        for (Cart cartItem : userCart) {
            boolean purchaseSuccess = false;

            for (MerchantStock stock : merchantStockRepository.findAll()) {
                if (stock.getProduct_id().equals(cartItem.getProduct_id()) && stock.getStock() > 0) {
                    // Attempt to buy the product
                    int result = buyProduct(userId, cartItem.getProduct_id(), stock.getMerchant_id());

                    if (result == 1) {
                        purchaseSuccess = true; // Purchase successful
                        anySuccessful = true;
                        break;
                    }
                }
            }

            if (!purchaseSuccess) { // Purchase failed
                failedProducts.add(cartItem); // Save failed purchases
            }
        }


        if (anySuccessful) {
            for (Cart cartItem : userCart) {
                if (!failedProducts.contains(cartItem)) {
                    cartRepository.delete(cartItem);
                }
            }
        }


        if (!anySuccessful) {
            return -7;
        }

        if (!failedProducts.isEmpty()) {
            System.out.println("Failed to buy the following products:");
            for (Cart failedProduct : failedProducts) {
                System.out.println(" * Product ID: " + failedProduct.getProduct_id());
            }
            return -5;
        }

        return 1;
    }

    // ========================= EXTRA METHOD 3 =========================\\
    // method for user to add rating to a product ..
    public int addRating (Integer userId ,Integer productId,Double rating){
        if(rating<1 || rating>5){
            return -3;// rating must be between 1 and 5
        }
        boolean userIdExists = false;
        int userRefId = -1;
        boolean productExists = false;
        int productRefId = -1;
        for (User u : userRepository.findAll()) {
            if (u.getUser_id().equals(userId)) {
                userIdExists = true;
                userRefId = u.getUser_id();
                break;
            }
        }
        if (!userIdExists) {
            return 0; // user does not exist
        }

        for (Product p : productRepository.findAll()) {
            if (p.getProduct_id().equals(productId)) {
                productExists = true;
                productRefId = p.getProduct_id();
                break;
            }
        }
        if (!productExists) {
            return -1; // product does not exist
        }
        if (userRepository.getReferenceById(userRefId).getRole().equals("Admin")) {
            return -2; // user is an admin and should not be able to rate
        }

        double totalOfRatings =   productRepository.getReferenceById(productRefId).getRatingCount() *   productRepository.getReferenceById(productRefId).getAverageRating();
        Product oldProduct = productRepository.getReferenceById(productRefId);
        oldProduct.setRatingCount(oldProduct.getRatingCount() + 1);
        double newAvg = (totalOfRatings + rating)/oldProduct.getRatingCount();
        newAvg = (double) Math.round(newAvg * 10) /10;
        oldProduct.setAverageRating(newAvg);
        productRepository.save(oldProduct);
        return 1; // rating added successfully

    }

    // ========================= EXTRA METHOD 5 =========================\\

    public List<Product> generateSimilarProducts(Integer userId) {

        List<History> userHistory = historyRepository.findAll();


        List<History> filteredHistory = new ArrayList<>();
        for (History history : userHistory) {
            if (history.getUser_id().equals(userId)) {
                filteredHistory.add(history);
            }
        }


        if (filteredHistory.isEmpty()) {
            return null;
        }


        Map<Integer, Integer> categoryCount = new HashMap<>();
        for (History history : filteredHistory) {
            Product product = productRepository.findById(history.getProduct_id()).orElse(null);
            if (product != null) {
                Integer categoryId = product.getCategory_id();
                categoryCount.put(categoryId, categoryCount.getOrDefault(categoryId, 0) + 1);
            }
        }


        Integer mostPurchasedCategory = null;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : categoryCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPurchasedCategory = entry.getKey();
            }
        }


        List<Product> similarProducts = new ArrayList<>();
        if (mostPurchasedCategory != null) {
            for (Product product : productRepository.findAll()) {
                if (product.getCategory_id().equals(mostPurchasedCategory)) {
                    similarProducts.add(product);
                }
            }
        }

        return similarProducts;
    }

}


