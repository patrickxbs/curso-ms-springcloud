package com.patrick.book_service.controller;

import com.patrick.book_service.client.ExchangeClient;
import com.patrick.book_service.dto.ExchangeDto;
import com.patrick.book_service.environment.InstanceInformationService;
import com.patrick.book_service.model.Book;
import com.patrick.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookRepository bookRepository;
    private final InstanceInformationService instanceInformationService;
    private final ExchangeClient exchangeClient;

    // http://localhost:8100/book/1/BRL
    @GetMapping(value = "/{id}/{currency}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable Long id,
                         @PathVariable String currency
    ){
        String port = instanceInformationService.retrieveServerPort();

        Book book = bookRepository.findById(id).orElseThrow();

        ExchangeDto exchange = exchangeClient.getExchange(book.getPrice(), "USD", currency);

        book.setEnvironment(port + " FEIGN");
        book.setPrice(exchange.convertedValue());
        book.setCurrency(currency);
        return book;
    }

}
