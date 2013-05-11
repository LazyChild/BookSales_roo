package com.ryliu.book.web;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.ryliu.book.domain.Book;
import com.ryliu.book.domain.BookOrder;
import com.ryliu.book.domain.BookTransaction;
import com.ryliu.book.domain.OrderStatus;
import com.ryliu.book.domain.TransactionType;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

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

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BookOrder bookOrder, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bookOrder);
            return "bookorders/create";
        }
        uiModel.asMap().clear();
        bookOrder.persist();
        return "redirect:/bookorders/" + encodeUrlPathSegment(bookOrder.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new BookOrder());
        return "bookorders/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bookorder", BookOrder.findBookOrder(id));
        uiModel.addAttribute("itemId", id);
        return "bookorders/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bookorders", BookOrder.findBookOrderEntries(firstResult, sizeNo));
            float nrOfPages = (float) BookOrder.countBookOrders() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bookorders", BookOrder.findAllBookOrders());
        }
        return "bookorders/list";
    }

	void populateEditForm(Model uiModel, BookOrder bookOrder) {
        uiModel.addAttribute("bookOrder", bookOrder);
        uiModel.addAttribute("books", Book.findAllBooks());
        uiModel.addAttribute("orderstatuses", Arrays.asList(OrderStatus.values()));
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
