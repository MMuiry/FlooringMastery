package com.mthree.floorMastery;

import com.mthree.floorMastery.controller.FlooringMasterController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringMasterController controller = ctx.getBean("controller", FlooringMasterController.class);
        controller.run();
    }
}
