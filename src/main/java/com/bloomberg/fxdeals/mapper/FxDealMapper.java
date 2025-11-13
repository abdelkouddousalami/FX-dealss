package com.bloomberg.fxdeals.mapper;

import com.bloomberg.fxdeals.dto.FxDealRequest;
import com.bloomberg.fxdeals.dto.FxDealResponse;
import com.bloomberg.fxdeals.entity.FxDeal;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FxDealMapper {

    public FxDeal toEntity(FxDealRequest request) {
        if (request == null) {
            return null;
        }

        FxDeal deal = new FxDeal();
        deal.setDealUniqueId(request.getDealUniqueId());
        deal.setFromCurrencyIso(request.getFromCurrencyIso());
        deal.setToCurrencyIso(request.getToCurrencyIso());
        deal.setDealTimestamp(request.getDealTimestamp());
        deal.setDealAmount(request.getDealAmount());

        return deal;
    }

    public FxDealResponse toResponse(FxDeal deal) {
        if (deal == null) {
            return null;
        }

        FxDealResponse response = new FxDealResponse();
        response.setId(deal.getId());
        response.setDealUniqueId(deal.getDealUniqueId());
        response.setFromCurrencyIso(deal.getFromCurrencyIso());
        response.setToCurrencyIso(deal.getToCurrencyIso());
        response.setDealTimestamp(deal.getDealTimestamp());
        response.setDealAmount(deal.getDealAmount());
        response.setCreatedAt(deal.getCreatedAt());

        return response;
    }
}
