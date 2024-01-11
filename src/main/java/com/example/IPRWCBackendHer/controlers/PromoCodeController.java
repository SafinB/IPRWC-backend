package com.example.IPRWCBackendHer.controlers;

import com.example.IPRWCBackendHer.DAO.PromoCodeDao;
import com.example.IPRWCBackendHer.DAO.UserDao;
import com.example.IPRWCBackendHer.models.ApiResponse;
import com.example.IPRWCBackendHer.models.Product;
import com.example.IPRWCBackendHer.models.PromoCode;
import com.example.IPRWCBackendHer.models.User;
import com.example.IPRWCBackendHer.services.PromoCheck;
import jakarta.persistence.criteria.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/promocode")
public class PromoCodeController {
    private final PromoCodeDao promoCodeDao;
    private final PromoCheck promoCheck;
    private final UserDao userDao;

    public PromoCodeController(PromoCodeDao promoCodeDao, PromoCheck promoCheck, UserDao userDao) {
        this.promoCodeDao = promoCodeDao;
        this.promoCheck = promoCheck;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<ArrayList<Order>> promoCodes() {
        ArrayList<PromoCode> promoCodes = this.promoCodeDao.getAllCodes();
        return new ApiResponse(HttpStatus.ACCEPTED, promoCodes);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponse<Product> promoCodes(@PathVariable UUID id) {
        PromoCode promoCode = this.promoCodeDao.getPromoById(id);
        return new ApiResponse(HttpStatus.ACCEPTED, promoCode);
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponse postPromoCodes(@RequestBody PromoCode promoCode) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.findByEmail(email).get();
        if (!this.userDao.isUserAdmin(user)) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to add new promocodes");
        }
        promoCode.setActive(true);
        this.promoCodeDao.saveToDatabase(promoCode);
        return new ApiResponse(HttpStatus.ACCEPTED, "You posted some data!");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse deletePromoCode(@PathVariable UUID id) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.findByEmail(email).get();
        if (!this.userDao.isUserAdmin(user)) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to delete promocodes");
        }
        this.promoCodeDao.deleteCodeFromDatabase(id);
        return new ApiResponse(HttpStatus.ACCEPTED, "You deleted some data!");
    }

    @RequestMapping(value = "/toggle-status/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResponse togglePromoCodeStatus(@PathVariable UUID id, @RequestBody Map<String, Boolean> statusRequest) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDao.findByEmail(email).orElse(null);

        if (user == null || !this.userDao.isUserAdmin(user)) {
            return new ApiResponse(HttpStatus.UNAUTHORIZED, "Only Admins Are Allowed to toggle promocode status");
        }

        boolean newStatus = statusRequest.get("active");
        PromoCode promoCode = this.promoCodeDao.getPromoById(id);

        if (promoCode != null) {
            promoCode.setActive(newStatus);
            this.promoCodeDao.saveToDatabase(promoCode);
            return new ApiResponse(HttpStatus.ACCEPTED, "Promocode status toggled successfully");
        } else {
            return new ApiResponse(HttpStatus.NOT_FOUND, "Promocode not found");
        }
    }
}