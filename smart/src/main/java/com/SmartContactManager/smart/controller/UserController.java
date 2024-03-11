package com.SmartContactManager.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.SmartContactManager.smart.dao.ContactRepository;
import com.SmartContactManager.smart.dao.UserRepository;
import com.SmartContactManager.smart.entities.Contact;
import com.SmartContactManager.smart.entities.User;
import com.SmartContactManager.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;


    //Model for add basic data for item
    @ModelAttribute
    public void addCommonData( Model model , Principal principal){

        String userName=principal.getName();
        System.out.println("Username = "+ userName);

         User user =this.userRepository.getUserByUserName(userName);

         System.out.println("User :"+ user);
         model.addAttribute("user", user);



    }

    //dashbord home
    @RequestMapping("/index")
    public String dashboard(Model model ,Principal principal){

        model.addAttribute("title", "User Dashbord");
       

         
        //get the user using username(Email)
        return "./normal/user_dashboard";
    }



     //"open add handeller
    @GetMapping("/add-contact")
     public String openAddContactForm(Model model){
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return"normal/add_contact_form";
     }
    

     //processing add contanct form 
     @PostMapping("/process-contact")
     public String processContact(@ModelAttribute Contact contact , @RequestParam("profileImage") MultipartFile file, Principal principal,HttpSession session){

        try{
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            //processing and uploading file
            if(file.isEmpty()){
                //if the file is empty message
                System.out.println("File is Empty");
                contact.setImage("contact.png");
            }
            else{
                //file the file to folder and update the name of contact
                contact.setImage(file.getOriginalFilename());
                java.io.File savefile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(savefile.getAbsolutePath()+java.io.File.separator+file.getOriginalFilename());

                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is uploaded");
                
            }


            
            contact.setUser(user); 
            user.getContacts().add(contact);





            this.userRepository.save(user);// save into the DB

            System.out.println("DATA" + contact);
            System.out.println("Added to DB");

            //message sucess..
            // session.setAttribute("message", new Message("Your Contact Is  Added !! Add More","sucess"));

            
        }catch(Exception e){
            System.out.println("ERROR"+ e.getMessage());
            e.printStackTrace();
            // session.setAttribute("message", new Message("Something went wrong try again!!","danger"));

            //error message..
        }

        return "normal/add_contact_form";

     }


     //show Contacts Handeller
     @GetMapping("/show-contacts/{page}")
     public String showContacts( @PathVariable("page") Integer page ,Model m,Principal principal){
        m.addAttribute("title", "Show User Contacts");

        //send contact list
   

        String userName = principal.getName();
        User user=this.userRepository.getUserByUserName(userName);

       Pageable pageable= PageRequest.of(page,5);

        Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(),pageable);

        m.addAttribute("contacts", contacts);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", contacts.getTotalPages());




        return "normal/show_contacts";
     }

     //showing particular contact details

     @RequestMapping("/{cId}/contact")
     public String showContactDetail(@PathVariable("cId") Integer cId ,Model model , Principal principal){

        System.out.println("CID"+ cId);
        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact=contactOptional.get();

       String userName= principal.getName();
        User user =this.userRepository.getUserByUserName(userName);


        if(user.getId() == contact.getUser().getId()){

            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());

        }
        else
        {
            model.addAttribute("title", "Permission Denied");
        }

       

        return "normal/contact_detail";
     }



     //delete Contact 

    @SuppressWarnings("null")
    @RequestMapping(value = "/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cid, Model model, Principal principal, HttpSession session) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		if(user.getId()==contact.getUser().getId()) {
			
			user.getContacts().remove(contact);
			
			this.contactRepository.deleteById(cid);
			session.setAttribute("message", new Message("Contact deleted Successfully...", "alert-success"));
			
		}
		return "redirect:/user/show-contacts/0";
	}
    

    //open update form handeller
    @PostMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid") Integer cid, Model m){
        m.addAttribute("title", "Update-Contact");
        Contact contact = this.contactRepository.findById(cid).get();
        m.addAttribute("contact", contact);
      
        return "normal/update_form";
    }

    //update contact handeller
    @RequestMapping(value = "/process-update", method = RequestMethod.POST)
    public String updateHandeller(@ModelAttribute Contact contact ,@RequestParam("profileImage") MultipartFile file,Model m,HttpSession session,Principal principal){
        try {

            //old  contact detail
           Contact oldcontactDetail  =  this.contactRepository.findById(contact.getcId()).get();

            //image...
            if(!file.isEmpty()){
                //file work..
                //rewrite..
                //delete old photo
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldcontactDetail.getImage());
                file1.delete();


                // update new photo

                File savFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(savFile.getAbsolutePath()+File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path ,StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());


            }
            else{
                contact.setImage(oldcontactDetail.getImage());
            }
            User user = this.userRepository.getUserByUserName(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);
            
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("CONTACT NAME" + contact.getName());
        
        return "redirect:/user/" + contact.getcId()+ "/contact";
    }


    //your profile handeller
    @GetMapping("/profile")
    public String yourProfile(Model model){

        model.addAttribute("title", "Profile page");

        return "normal/profile";

    }

    



}
