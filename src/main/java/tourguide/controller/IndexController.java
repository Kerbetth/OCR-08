package tourguide.controller;


import org.springframework.web.bind.annotation.*;

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

}