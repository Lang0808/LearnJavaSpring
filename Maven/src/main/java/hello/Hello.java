package main.java.hello;

import org.joda.time.LocalTime;

public class Hello {
    public static void main(String[] args){
        LocalTime currentTime=new LocalTime();
        System.out.println("Current time: "+currentTime);
        Greeter greeter=new Greeter();
        System.out.println(greeter.sayHello());
    }
}
