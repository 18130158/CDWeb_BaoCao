package com.cdweb.service.impl;

import com.cdweb.converter.BookConverter;
import com.cdweb.converter.ShoppingCartConverter;
import com.cdweb.converter.UserConverter;
import com.cdweb.dto.BookDTO;
import com.cdweb.dto.ShoppingCartDTO;
import com.cdweb.dto.UserDTO;
import com.cdweb.entity.ShoppingCartEntity;
import com.cdweb.repository.BookRepository;
import com.cdweb.repository.ShoppingCartRepository;
import com.cdweb.service.IBookService;
import com.cdweb.service.IShoppingCartService;
import com.cdweb.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {
    @Autowired
    private BookConverter bookConverter;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ShoppingCartConverter shoppingCartConverter;

    @Override
    public List<ShoppingCartDTO> addProduct(long book_id, String email) {
        UserDTO user = this.userService.findByEmail(email);
        if (book_id != 0) {
            BookDTO book = this.bookService.findById(book_id);
            ShoppingCartEntity entity = this.shoppingCartRepository.findCart(book.getId(), user.getId());
            ShoppingCartDTO cart = new ShoppingCartDTO();
            if (entity == null) {
                cart.setBook(book);
                cart.setUser(user);
                cart.setQuantity(1);
            } else {
                cart.setId(entity.getId());
                cart.setBook(book);
                cart.setUser(user);
                cart.setQuantity(entity.getQuantity() + 1);
            }
            ShoppingCartEntity test = this.shoppingCartConverter.toEntity(cart);
            this.shoppingCartRepository.save(test);
        }
        List<ShoppingCartDTO> cartDTO = new ArrayList<>();
        for (ShoppingCartEntity itemEntity : this.shoppingCartRepository.findAllByUser(user.getId())) {
            cartDTO.add(this.shoppingCartConverter.toDTO(itemEntity));
        }
        return cartDTO;
    }

    @Override
    public List<ShoppingCartDTO> getProduct(String email) {
        UserDTO user = this.userService.findByEmail(email);
        List<ShoppingCartEntity> cartEntities = this.shoppingCartRepository.findAllByUser(user.getId());
        List<ShoppingCartDTO> cartList = new ArrayList<>();
        for (ShoppingCartEntity s : cartEntities) {
            cartList.add(this.shoppingCartConverter.toDTO(s));
        }
        return cartList;
    }

    @Override
    public List<ShoppingCartDTO> updateQuantity(long id, int quantity, String name) {
        UserDTO user = this.userService.findByEmail(name);
        ShoppingCartEntity cartEntity = this.shoppingCartRepository.findCart(id, user.getId());
        ShoppingCartDTO cartDTO = new ShoppingCartDTO();
        if (quantity == 0) {
            this.shoppingCartRepository.delete(cartEntity);
            return getProduct(user.getEmail());
        } else {
            cartDTO.setId(cartEntity.getId());
            cartDTO.setBook(this.bookService.findById(id));
            cartDTO.setUser(user);
            cartDTO.setQuantity(quantity);
            this.shoppingCartRepository.save(this.shoppingCartConverter.toEntity(cartDTO));
            return getProduct(user.getEmail());
        }
    }
}
