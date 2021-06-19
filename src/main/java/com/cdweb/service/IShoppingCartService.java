package com.cdweb.service;

import com.cdweb.dto.ShoppingCartDTO;

import java.util.List;

public interface IShoppingCartService {
    public List<ShoppingCartDTO> addProduct(long book_id, String email);

    public List<ShoppingCartDTO> getProduct(String email);
}
