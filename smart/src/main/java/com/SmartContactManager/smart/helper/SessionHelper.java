package com.SmartContactManager.smart.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import jakarta.servlet.http.HttpSession;
@Component
public class SessionHelper {

    public void removeMessageFromSession(){
        try {
            

          HttpSession session =  ((ServletRequestAttributes) extracted()).getRequest().getSession(); 
          session.removeAttribute("message");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestAttributes extracted() {
        return RequestContextHolder.getRequestAttributes();
    }
    
}
