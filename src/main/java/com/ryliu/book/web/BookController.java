package com.ryliu.book.web;

import java.util.Date;
import java.util.List;

import com.ryliu.book.domain.Book;
import com.ryliu.book.domain.BookTransaction;
import com.ryliu.book.domain.TransactionType;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


    @RequestMapping(params = "find=ByTitleOrAuthorLike", method = RequestMethod.GET,
    		headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> findBooksByTitleOrAuthorLike(@RequestParam("keyword") String keyword, Model uiModel) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	List<Book> books = Book.findBooksByTitleOrAuthorLike(keyword).getResultList();
    	List<Book> result = books.subList(0, Math.min(books.size(), 10));
        return new ResponseEntity<String>(Book.toJsonArray(result), headers, HttpStatus.OK);
    }
}
