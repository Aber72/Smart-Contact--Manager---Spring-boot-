package com.SmartContactManager.smart.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SmartContactManager.smart.dao.UserRepository;
import com.SmartContactManager.smart.entities.User;
import com.SmartContactManager.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    //home handeller
    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title","Home-Smart contact Manager");
        return "home";
    }

    //About handeller
    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title","About Smart contact manager");

        return "about";
    }


    //signup Handeller
    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title","Register Smart contact manager");
        model.addAttribute("user",new User());
        return "signup";
    }

    // this is handler for signup user

    @RequestMapping(value = "/do_register", method = RequestMethod.POST) // from this attribute we will get the signup data on submiting the for as a object
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,@RequestParam(value = "agreement",defaultValue = "false")boolean agreement, 

    Model model,HttpSession session,RedirectAttributes redirectAttributes){/*in thus line we fech the user object by using model attibute and creating one
        more user object the field values which matches this user object is automatically comes to created user object and for checkbox we created request param */


       try {
            if(!agreement){
                System.out.println("you have not agreed the agreement");
                throw new Exception("you have not agreed the agreement");
            }

            if(result.hasErrors()){
                System.out.println("ERROR "+ result.toString());
                model.addAttribute("user", user);
                return"signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageurl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));



          
             this.userRepository.save(user);

            

            model.addAttribute("user", new User());
        
            extracted(session).setAttribute("message", new Message("Sucessfully Registered", "alert-sucess"));
            // redirectAttributes.addFlashAttribute("message", new Message("success", "Registration successful!"));

           
            return "redirect:/signup";
        
       }
        catch (Exception e) {
        //  handle exception
          e.printStackTrace();
          model.addAttribute("user", user);
          extracted(session).setAttribute("message", new Message("something went wrong..!!"+ e.getMessage(), "alert-danger"));
        //   redirectAttributes.addFlashAttribute("message", new Message("error", "Something went wrong: " + e.getMessage()));

          
          return "redirect:/signup";
       }

        
    }

    private HttpSession extracted(HttpSession session) {
        return session;
    }

    // handeler for custom login
    @GetMapping("/login")
    public String customLogin(Model model){
        return "login";
    }
 }
