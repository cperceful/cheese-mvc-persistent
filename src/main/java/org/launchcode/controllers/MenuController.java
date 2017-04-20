package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Class to handle /menu requests
 */

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model){

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model){

        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu newMenu, Errors errors){

        if (errors.hasErrors()){
            return "menu/add";
        }

        menuDao.save(newMenu);

        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") int id, Model model){

        model.addAttribute("menu", menuDao.findOne(id));
        model.addAttribute("title", menuDao.findOne(id).getName());

        return "menu/view";

    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.GET)
    public String addItem(@PathVariable("id") int id, Model model){


        model.addAttribute("title", "Add item to menu: " + menuDao.findOne(id).getName());
        model.addAttribute("form", new AddMenuItemForm(menuDao.findOne(id), cheeseDao.findAll()));

        return "menu/add-item";

    }

    @RequestMapping(value = "add-item/{id}", method = RequestMethod.POST)
    public String addItem(@PathVariable("id") int id, Model model, @ModelAttribute @Valid AddMenuItemForm form, Errors errors){

        if (errors.hasErrors()){
            return "menu/add-item";
        }

        Cheese menuCheese = cheeseDao.findOne(form.getCheeseId());
        Menu newMenu = menuDao.findOne(form.getMenuId());

        newMenu.addItem(menuCheese);

        menuDao.save(newMenu);

        return "redirect:/menu/view/" + newMenu.getId();
    }
}
