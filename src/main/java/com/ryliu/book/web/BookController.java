package com.ryliu.book.web;

import java.util.Date;

import com.ryliu.book.domain.Book;
import com.ryliu.book.domain.BookTransaction;
import com.ryliu.book.domain.TransactionType;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class, delete = false)
@RooWebFinder
public class BookController {
	
    private Logger LOGGER = Logger.getLogger(BookController.class);

	@RequestMapping(value = "/{id}", params = "buy", produces = "text/html")
    public String buy(@PathVariable("id") Long id, Model uiModel) {
    	Book book = Book.findBook(id);
    	if (book.getAmount() < 0) {
    		throw new IllegalArgumentException("This book is already sold out!");
    	}
    	LOGGER.debug("Entering Buying");
    	BookTransaction transaction = new BookTransaction();
    	transaction.setTransactionDate(new Date());
    	transaction.setTransactionType(TransactionType.SELL);
    	transaction.setAmount(1L);
    	transaction.setFigure(book.getPrice());
    	book.setAmount(book.getAmount() - 1);
    	uiModel.asMap().clear();
    	book.merge();
    	
    	transaction.setBook(book);
    	transaction.persist();
        return "redirect:/books/" + book.getId().toString();
    }
	
	@ExceptionHandler
	public void handleException(Exception e) {
		LOGGER.error("Error: ", e);
	}
}
