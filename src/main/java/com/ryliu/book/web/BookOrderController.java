package com.ryliu.book.web;

import java.util.Date;

import com.ryliu.book.domain.Book;
import com.ryliu.book.domain.BookOrder;
import com.ryliu.book.domain.BookTransaction;
import com.ryliu.book.domain.OrderStatus;
import com.ryliu.book.domain.TransactionType;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bookorders")
@Controller
@RooWebScaffold(path = "bookorders", formBackingObject = BookOrder.class,
delete = false, update = false)
public class BookOrderController {
	
	private Logger LOGGER = Logger.getLogger(BookOrderController.class);
	
	@RequestMapping(value = "/{id}", params = "pay", produces = "text/html")
	public String pay(@PathVariable("id") Long id, Model uiModel) {
		BookOrder bookOrder = BookOrder.findBookOrder(id);
		if (bookOrder.getStatus() != OrderStatus.UNPAID) {
			throw new IllegalArgumentException("Must be an unpaid order!");
		}
		BookTransaction transaction = new BookTransaction();
		transaction.setBook(bookOrder.getBook());
		transaction.setAmount(bookOrder.getAmount());
		transaction.setFigure(bookOrder.getFigure());
		transaction.setTransactionDate(new Date());
		transaction.setTransactionType(TransactionType.BUY);
		transaction.persist();

		bookOrder.setStatus(OrderStatus.PAID);
		uiModel.asMap().clear();
		bookOrder.merge();
		return "redirect:/bookorders/" + bookOrder.getId().toString();
	}
	
	@RequestMapping(value = "/{id}", params = "cancel", produces = "text/html")
	public String cancel(@PathVariable("id") Long id, Model uiModel) {
		BookOrder bookOrder = BookOrder.findBookOrder(id);
		if (bookOrder.getStatus() != OrderStatus.UNPAID) {
			throw new IllegalArgumentException("Must be an unpaid order!");
		}
		bookOrder.setStatus(OrderStatus.CANCELED);
		uiModel.asMap().clear();
		bookOrder.merge();
		return "redirect:/bookorders/" + bookOrder.getId().toString();
	}
	
	@RequestMapping(value = "/{id}", params = "shelf", produces = "text/html")
	public String shelf(@PathVariable("id") Long id, Model uiModel) {
		BookOrder bookOrder = BookOrder.findBookOrder(id);
		if (bookOrder.getStatus() != OrderStatus.PAID) {
			throw new IllegalArgumentException("Must be a paid order!");
		}
		Book book = bookOrder.getBook();
		book.setAmount(book.getAmount() + bookOrder.getAmount());
		book.merge();

		bookOrder.setStatus(OrderStatus.SHELVED);
		bookOrder.merge();
		return "redirect:/books/" + book.getId().toString() + "?form";
	}
	
	@ExceptionHandler
	public void handleException(Exception e) {
		LOGGER.error("Error: ", e);
	}
}
