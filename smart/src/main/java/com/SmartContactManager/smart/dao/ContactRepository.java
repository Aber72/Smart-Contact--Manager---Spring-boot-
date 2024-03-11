package com.SmartContactManager.smart.dao;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SmartContactManager.smart.entities.Contact;
import com.SmartContactManager.smart.entities.User;

import java.util.List;


public interface ContactRepository extends JpaRepository <Contact ,Integer> {

    //pagination..
    @Query("from Contact as c where c.user.id =:userId")
    //in page object 1st obj is currentPage-page
    //2nd is Contact per page
    public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pePageable);
    
    // for search 
    public List<Contact>  findByNameContainingAndUser(String name, User user);
    
}
