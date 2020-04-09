//package com.springbootcamp.ecommerceapp.events;
//
//import com.springbootcamp.ecommerceapp.entities.User;
//import org.springframework.context.ApplicationEvent;
//
//import java.util.Locale;
//
//public class ActivationEmailFireEvent extends ApplicationEvent {
//
//    private String appUrl;
//    private Locale locale;
//    private User user;
//
//    public ActivationEmailFireEvent(String appUrl, Locale locale, User user) {
//        super(user);
//
//        this.appUrl = appUrl;
//        this.locale = locale;
//        this.user = user;
//    }
//
//    public String getAppUrl() {
//        return appUrl;
//    }
//
//    public void setAppUrl(String appUrl) {
//        this.appUrl = appUrl;
//    }
//
//    public Locale getLocale() {
//        return locale;
//    }
//
//    public void setLocale(Locale locale) {
//        this.locale = locale;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public String toString() {
//        return "OnRegistrationCompleteEvent{" +
//                "appUrl='" + appUrl + '\'' +
//                ", locale=" + locale +
//                ", user=" + user +
//                '}';
//    }
//}
