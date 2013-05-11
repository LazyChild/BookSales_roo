package com.ryliu.book.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

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

	@RequestMapping(params = { "find=ByAuthorLike", "form" }, method = RequestMethod.GET)
    public String findBooksByAuthorLikeForm(Model uiModel) {
        return "books/findBooksByAuthorLike";
    }

	@RequestMapping(params = "find=ByAuthorLike", method = RequestMethod.GET)
    public String findBooksByAuthorLike(@RequestParam("author") String author, Model uiModel) {
        uiModel.addAttribute("books", Book.findBooksByAuthorLike(author).getResultList());
        return "books/list";
    }

	@RequestMapping(params = { "find=ByIsbnEquals", "form" }, method = RequestMethod.GET)
    public String findBooksByIsbnEqualsForm(Model uiModel) {
        return "books/findBooksByIsbnEquals";
    }

	@RequestMapping(params = "find=ByIsbnEquals", method = RequestMethod.GET)
    public String findBooksByIsbnEquals(@RequestParam("isbn") String isbn, Model uiModel) {
        uiModel.addAttribute("books", Book.findBooksByIsbnEquals(isbn).getResultList());
        return "books/list";
    }

	@RequestMapping(params = { "find=ByPublisherLike", "form" }, method = RequestMethod.GET)
    public String findBooksByPublisherLikeForm(Model uiModel) {
        return "books/findBooksByPublisherLike";
    }

	@RequestMapping(params = "find=ByPublisherLike", method = RequestMethod.GET)
    public String findBooksByPublisherLike(@RequestParam("publisher") String publisher, Model uiModel) {
        uiModel.addAttribute("books", Book.findBooksByPublisherLike(publisher).getResultList());
        return "books/list";
    }

	@RequestMapping(params = { "find=ByTitleLike", "form" }, method = RequestMethod.GET)
    public String findBooksByTitleLikeForm(Model uiModel) {
        return "books/findBooksByTitleLike";
    }

	@RequestMapping(params = "find=ByTitleLike", method = RequestMethod.GET)
    public String findBooksByTitleLike(@RequestParam("title") String title, Model uiModel) {
        uiModel.addAttribute("books", Book.findBooksByTitleLike(title).getResultList());
        return "books/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Book book, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/create";
        }
        uiModel.asMap().clear();
        book.persist();
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Book());
        return "books/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("book", Book.findBook(id));
        uiModel.addAttribute("itemId", id);
        return "books/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", Book.findBookEntries(firstResult, sizeNo));
            float nrOfPages = (float) Book.countBooks() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("books", Book.findAllBooks());
        }
        return "books/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Book book, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/update";
        }
        uiModel.asMap().clear();
        book.merge();
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Book.findBook(id));
        return "books/update";
    }

	void populateEditForm(Model uiModel, Book book) {
        uiModel.addAttribute("book", book);
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
