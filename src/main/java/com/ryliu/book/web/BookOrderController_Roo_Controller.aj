// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ryliu.book.web;

import com.ryliu.book.domain.Book;
import com.ryliu.book.domain.BookOrder;
import com.ryliu.book.domain.OrderStatus;
import com.ryliu.book.web.BookOrderController;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect BookOrderController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BookOrderController.create(@Valid BookOrder bookOrder, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bookOrder);
            return "bookorders/create";
        }
        uiModel.asMap().clear();
        bookOrder.persist();
        return "redirect:/bookorders/" + encodeUrlPathSegment(bookOrder.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BookOrderController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BookOrder());
        return "bookorders/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BookOrderController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bookorder", BookOrder.findBookOrder(id));
        uiModel.addAttribute("itemId", id);
        return "bookorders/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BookOrderController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
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
    
    void BookOrderController.populateEditForm(Model uiModel, BookOrder bookOrder) {
        uiModel.addAttribute("bookOrder", bookOrder);
        uiModel.addAttribute("books", Book.findAllBooks());
        uiModel.addAttribute("orderstatuses", Arrays.asList(OrderStatus.values()));
    }
    
    String BookOrderController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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