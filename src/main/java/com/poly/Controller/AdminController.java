package com.poly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.Model.User;
import com.poly.Service.CategoryService;
import com.poly.Service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/account/index";
    }

    @GetMapping("/account/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/account/_form";
    }

    @PostMapping("/account/save")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(value = "password", required = false) String password) {
        if (user.getUserID() != null) { // Sửa user
            User existing = userService.findById(user.getUserID()).orElse(null);
            if (existing != null) {
                if (password != null && !password.isBlank()) {
                    user.setPasswordHash(passwordEncoder.encode(password));
                } else {
                    user.setPasswordHash(existing.getPasswordHash());
                }
            }
        } else { // Thêm mới user
            if (password != null && !password.isBlank()) {
                user.setPasswordHash(passwordEncoder.encode(password));
            } else {
                user.setPasswordHash(passwordEncoder.encode("123456")); // Mặc định nếu không nhập
            }
        }
        userService.save(user);
        return "redirect:/admin/account";
    }

    @GetMapping("/account/edit/{id}")
    public String editUserForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "admin/account/_form";
    }

    @GetMapping("/account/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return "redirect:/admin/account";
    }

    @GetMapping("/role")
    public String role() {
        return "admin/role/index";
    }

    // @GetMapping("/product")
    // public String product() {
    // 	return "admin/product/index";
    // }
    // Đã xóa method category(Model) để tránh trùng mapping với CategoryController
    // @GetMapping("/order")
    // public String order() {
    //     return "admin/order/index";
    // }
}
