package com.company;

public class ParkscheinException extends RuntimeException {

        public ParkscheinException (String message){
            System.err.println(message);
        }
}
