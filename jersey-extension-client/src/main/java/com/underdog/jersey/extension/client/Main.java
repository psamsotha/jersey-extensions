/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.underdog.jersey.extension.client;

import java.util.UUID;

/**
 *
 * @author PaulSamsotha
 */
public class Main {
    
    public static void main(String[] args) {
        for (int i = 0; i < 3; i ++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }
}
