package org.demo.client;

import org.demo.global.client.Account;
import web.client.WebClient;

import javax.swing.*;

public  class Client extends WebClient {
    public static Account account;

    public Client(Class<?> target) {
        super(target);
    }
}
