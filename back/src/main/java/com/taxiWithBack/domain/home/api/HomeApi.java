package com.taxiWithBack.domain.home.api;


import com.taxiWithBack.domain.home.dto.HomeDto;
import com.taxiWithBack.domain.home.service.HomeService;
import com.taxiWithBack.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/v1/home")
@Slf4j
public class HomeApi {

    @Autowired
    private TokenProvider tokenProvider;

    private final HomeService homeService;
    @Autowired
    public HomeApi(HomeService homeService){
        this.homeService=homeService;

    }

    @GetMapping("/test")
    public String homeTest(){
        String message="Test in Home";
        log.info(message);
        return message;

    }

    @PostMapping("/") // "/home/home" -> "/home"
    public ResponseEntity<Boolean> home(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);

    }

    @PostMapping("/oreum1")
    public ResponseEntity<Boolean> oreum1(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/oreum2")
    public ResponseEntity<Boolean> oreum2(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/oreum3")
    public ResponseEntity<Boolean> oreum3(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/preum1")
    public ResponseEntity<Boolean> preum1(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/preum2")
    public ResponseEntity<Boolean> preum2(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/preum3")
    public ResponseEntity<Boolean> preum3(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/preum4")
    public ResponseEntity<Boolean> preum4(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/myWriting") //jwt 유효성 검사 필요, try-catch
    public ResponseEntity<Boolean> myWriting(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/newWriting") //jwt 유효성 검사 필요, try-catch
    public ResponseEntity<Boolean> newWriting(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }

    @PostMapping("/alarm") //jwt 유효성 검사 필요, try-catch
    public ResponseEntity<Boolean> alarm(@RequestBody HomeDto dto){
        return ResponseEntity.ok(true);
    }
}
