package com.coderslab.admincontroller;

import com.coderslab.dao.UsersDao;
import com.coderslab.entity.Users;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class UsersController {

    @Autowired
    private UsersDao usersDao;

    @RequestMapping(value = "/add_user", method = RequestMethod.GET)
    public String loadAddUserPage(HttpServletRequest request, Model model) {
        model.addAttribute("users", new Users());
        model.addAttribute("sm", request.getParameter("sm"));
        model.addAttribute("em", request.getParameter("em"));
        return "/admin/add_user";
    }

    @RequestMapping(value = "/list_user", method = RequestMethod.GET)
    public String loadListUserPage(Model model, Users users, HttpServletRequest request) {
        model.addAttribute("users", usersDao.getAllData(users));
        model.addAttribute("sm", request.getParameter("sm"));
        model.addAttribute("em", request.getParameter("em"));
        return "/admin/list_user";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    public String saveUsers(@Valid Users users, Model model, BindingResult result) {

        if(result.hasErrors()){
            System.out.println("==========" + users.toString());
            return "redirect:/admin/add_user";
        }
        
        try {
            boolean status = usersDao.saveData(users);
            if (status) {
                model.addAttribute("sm", "User info saved successfully");
            } else {
                model.addAttribute("em", "User info not saved");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/admin/add_user";
    }

    @RequestMapping(value = "/update_user_status/{id}/{status}", method = RequestMethod.GET)
    public String updateUserStatus(Model model, @PathVariable("id") int id, @PathVariable("status") boolean status) {

        boolean u_status = !status;

        Users users = new Users();
        users.setUserId(id);
        users.setStatus(u_status);

        try {
            boolean stat = usersDao.updateUserStatus(users);
            if (stat) {
                model.addAttribute("sm", "Status Update successfull");
            } else {
                model.addAttribute("em", "Status Not Update");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "redirect:/admin/list_user";
    }

    @RequestMapping(value = "/edit_user/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable("id") int id, Model model, HttpServletRequest request) {

        model.addAttribute("sm", request.getParameter("sm"));
        model.addAttribute("em", request.getParameter("em"));

        try {
            model.addAttribute("user", usersDao.findData(id));
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "/admin/edit_user";
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String updateUser(Model model, Users users) {

        try {
            boolean status = usersDao.updateData(users);
            if (status) {
                model.addAttribute("sm", "User Info update successfull");
            } else {
                model.addAttribute("em", "User Info not update");
            }
        } catch (Exception ex) {
            model.addAttribute("em", "User Info not update");
            ex.printStackTrace();
        }

        return "redirect:/admin/edit_user/" + users.getUserId();
    }

    @RequestMapping(value = "/delete_user/{id}", method = RequestMethod.GET)
    public String deleteUser(Model model, @PathVariable("id") int id) {

        try {
            boolean status = usersDao.deleteUserById(id);
            if (status) {
                model.addAttribute("sm", "User Info deleted successfully");
            }
        } catch (Exception ex) {
            model.addAttribute("em", "User Info not deleted");
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/admin/list_user";
    }

}
