//package com.springbootcamp.ecommerceapp.events;
//
//import com.springbootcamp.ecommerceapp.entities.User;
//import com.springbootcamp.ecommerceapp.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
//import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedHashMap;
//
//
//@Component
//public class AuthenticationEventListener {
//
//    @Autowired
//    UserService userService;
//
////    @EventListener
////    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
////        int counter;
////        String userEmail = (String) event.getAuthentication().getPrincipal();
////        Optional<UserLoginFailCounter> userLoginFailCounter = userLoginFailCounterRepo.findByEmail(userEmail);
////
////        if (!userLoginFailCounter.isPresent()) {
////            UserLoginFailCounter userLoginFailCounter1 = new UserLoginFailCounter();
////            userLoginFailCounter1.setAttempts(1);
////            userLoginFailCounter1.setEmail(userEmail);
////            userLoginFailCounterRepo.save(userLoginFailCounter1);
////        }
////        if (userLoginFailCounter.isPresent()) {
////            counter = userLoginFailCounter.get().getAttempts();
////            System.out.println(counter);
////            if (counter>=2) {
////                User user = userRepo.findByEmail(userEmail);
////                user.setLocked(true);
////                sendEmail.sendEmail("ACCOUNT LOCKED","YOUR ACCOUNT HAS BEEN LOCKED",userEmail);
////                userRepo.save(user);
////            }
////            UserLoginFailCounter userLoginFailCounter1 = userLoginFailCounter.get();
////            userLoginFailCounter1.setAttempts(++counter);
////            userLoginFailCounter1.setEmail(userEmail);
////            System.out.println(userLoginFailCounter1+"-----------------");
////            userLoginFailCounterRepo.save(userLoginFailCounter1);
////        }
////
////    }
//
//    @EventListener
//    public void authenticationPass(AuthenticationSuccessEvent event) {
//        LinkedHashMap<String,String> userMap = new LinkedHashMap<>();
//        try {
//            userMap = (LinkedHashMap<String, String>) event.getAuthentication().getDetails();
//        } catch (ClassCastException ex) {
//
//        }
//        String userEmail = new String();
//        try {
//            userEmail = userMap.get("username");
//        } catch (NullPointerException e) {
//        }
//
//        User user = userService.getUserByEmail(userEmail);
//        System.out.println(" ##### user found");
//        try{
//            System.out.println("before setting status in try catch");
//            user.setLoginStatus(true);
//            System.out.println("after setting status in try catch");
//        }
//        catch(Exception ex){
//            System.out.println("##### " + ex);
//        }
//        userService.saveRegisteredUser(user);
//    }
//
//}
//
