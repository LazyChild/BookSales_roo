package com.ryliu.book.web;

import com.ryliu.book.domain.BookTransaction;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/booktransactions")
@Controller
@RooWebScaffold(path = "booktransactions", formBackingObject = BookTransaction.class, create = false, delete = false, update = false)
@RooWebFinder
public class BookTransactionController {

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("booktransaction", BookTransaction.findBookTransaction(id));
        uiModel.addAttribute("itemId", id);
        return "booktransactions/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("booktransactions", BookTransaction.findBookTransactionEntries(firstResult, sizeNo));
            float nrOfPages = (float) BookTransaction.countBookTransactions() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("booktransactions", BookTransaction.findAllBookTransactions());
        }
        addDateTimeFormatPatterns(uiModel);
        return "booktransactions/list";
    }
}
