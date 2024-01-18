package com.example.IPRWCBackendHer.DAO;

import com.example.IPRWCBackendHer.models.PromoCode;
import com.example.IPRWCBackendHer.repository.PromoCodeRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

@Component
public class PromoCodeDao {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeDao(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public void saveToDatabase(PromoCode promoCode) {
        this.promoCodeRepository.save(promoCode);
    }
    public ArrayList<PromoCode> getAllCodes() {
        return (ArrayList<PromoCode>) this.promoCodeRepository.findAll();
    }

    public PromoCode getPromoById(UUID id) {
        return this.promoCodeRepository.findById(id).get();
    }

    public void deleteCodeFromDatabase(UUID id) {
        this.promoCodeRepository.deleteById(id);
    }
}
