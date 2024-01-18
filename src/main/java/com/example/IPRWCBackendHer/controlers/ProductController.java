package com.example.IPRWCBackendHer.controlers;

import com.example.IPRWCBackendHer.DAO.ProductDao;
import com.example.IPRWCBackendHer.DAO.UserDao;
import com.example.IPRWCBackendHer.models.ApiResponse;
import com.example.IPRWCBackendHer.models.Product;
import com.example.IPRWCBackendHer.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductDao productDao;
    private final UserDao userDao;

    public ProductController(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<ArrayList<Product>> products() {
        ArrayList<Product> products = this.productDao.getAllProducts();
        return new ApiResponse<>(HttpStatus.ACCEPTED, products);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Product> product(@PathVariable UUID id) {
        Product product = this.productDao.getProductById(id);
        return new ApiResponse<>(HttpStatus.ACCEPTED, product);
    }

    @RequestMapping(value = "/insert{id}", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse<Product> createProduct(@RequestBody Product product) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.findByEmail(email).get();
        if (!this.userDao.isUserAdmin(user)){
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to add new products");
        }
        this.productDao.saveToDatabase(product);
        return new ApiResponse<>(HttpStatus.ACCEPTED, "You posted some data!");
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse<Product> updateProduct(@RequestBody Product product) {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.findByEmail(email).get();
        if (!this.userDao.isUserAdmin(user)){
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to add new products");
        }
        this.productDao.updateProduct(product);
        return new ApiResponse<>(HttpStatus.ACCEPTED, "You posted some data!");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse<Product> deleteProduct(@PathVariable UUID id) {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userDao.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (this.userDao.isUserAdmin(user)) {
                productDao.deleteProductById(id);
            } else {
                return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to delete products");
            }
        } else {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "User not found");
        }
        return new ApiResponse<>(HttpStatus.ACCEPTED, "You deleted some data!");
    }
}
