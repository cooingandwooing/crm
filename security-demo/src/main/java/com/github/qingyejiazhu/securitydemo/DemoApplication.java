/**
 *
 */
package com.github.qingyejiazhu.securitydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gaoxiaofeng
 *
 */
@SpringBootApplication(scanBasePackages =
        {
                "com.github.qingyejiazhu.securitydemo",
                //"com.github.qingyejiazhu.securitybrowser",
                "com.github.qingyejiazhu.securityapp",
                "com.github.qingyejiazhu.securitycore",
                "com.github.qingyejiazhu.securityauthorize"
        })
@RestController
@EnableSwagger2
public class DemoApplication {


    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

}
