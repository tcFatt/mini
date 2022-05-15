package nusiss.mini.model;

import org.springframework.util.MultiValueMap;

public class ConvertUtil {
    
    public static User convertUser(MultiValueMap<String, String> form) {
        User u = new User();
        u.setUsername(form.getFirst("username"));
        u.setName(form.getFirst("name"));
        u.setEmail(form.getFirst("email"));
        u.setPassword(form.getFirst("password"));
        
        return u;
    }

}
