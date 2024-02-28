package com.suleware.microservices.cards.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.suleware.microservices.cards.constants.CardsConstants;
import com.suleware.microservices.cards.dto.CardsDto;
import com.suleware.microservices.cards.entity.Cards;
import com.suleware.microservices.cards.exception.CardAlreadyExistsException;
import com.suleware.microservices.cards.exception.ResourceNotFoundException;
import com.suleware.microservices.cards.mapper.CardsMapper;
import com.suleware.microservices.cards.repository.CardsRepository;
import com.suleware.microservices.cards.service.ICardService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardServiceImpl implements ICardService {

    private CardsRepository repository;

    @Override
    public void createCard(String mobileNumber) {

        Optional<Cards> oCards = repository.findByMobileNumber(mobileNumber);

        if (oCards.isPresent()) {
            throw new CardAlreadyExistsException(mobileNumber);
        }
        repository.save(createNewCard(mobileNumber));

    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Optional<Cards> oCards = repository.findByMobileNumber(mobileNumber);

        if (!oCards.isPresent()) {
            throw new ResourceNotFoundException("fetch", "mobileNumber", mobileNumber);
        }

        return CardsMapper.mapToCardsDto(oCards.get(), new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Optional<Cards> oCards = repository.findByMobileNumber(cardsDto.getMobileNumber());

        if (!oCards.isPresent()) {
            throw new ResourceNotFoundException("update", "mobileNumber", cardsDto.getMobileNumber());
        }

        repository.save(CardsMapper.mapToCards(cardsDto, oCards.get()));
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Optional<Cards> oCards = repository.findByMobileNumber(mobileNumber);

        if (!oCards.isPresent()) {
            throw new ResourceNotFoundException("delete", "mobileNumber", mobileNumber);
        }
        repository.deleteById(oCards.get().getCardId());
        return true;
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
}
