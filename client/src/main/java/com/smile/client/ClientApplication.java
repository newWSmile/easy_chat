package com.smile.client;

import com.alibaba.fastjson.JSON;
import com.smile.client.client.WebSocketClient;
import com.smile.client.event.LoginEvent;
import com.smile.client.event.OnlineUserEvent;
import com.smile.common.action.ActionIdEnum;
import com.smile.common.action.LoginReqAction;
import com.smile.common.event.EventPool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.util.Scanner;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    private WebSocketClient webSocketClient;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        this.registerEvent();
        this.connect();
        this.handleCommand();

    }

    private void registerEvent(){
        EventPool.getInstance().registe(ActionIdEnum.ACTION_LOGIN_RESP.getAction(), new LoginEvent());
        EventPool.getInstance().registe(ActionIdEnum.ACTION_FETCH_ONLINE_USERS_RESP.getAction(), new OnlineUserEvent());
    }

    private void connect(){
        URI uri = URI.create("ws://localhost:8081/chat");
        this.webSocketClient = new WebSocketClient(uri);
        this.webSocketClient.connect();
    }

    private String readCommand(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void login(String mobile , String password){
        LoginReqAction loginReqAction = new LoginReqAction();
        loginReqAction.setMobile(mobile);
        loginReqAction.setPassword(password);
        this.webSocketClient.send(loginReqAction, JSON.toJSONString(loginReqAction));
    }

    private void handleCommand() {
        System.out.println("wait input command");
        while (true ) {
            String command = readCommand();
            if ( null == command || command.isEmpty() ) {
                System.out.println("empty command!");
                continue;
            }
            if ( command.equals("exit") ) {
                System.exit(-1);
                return ;
            }
            // login 处理登陆逻辑。其实发送登陆包
            // login mobile password
            if ( command.indexOf("login") == 0 ) {
                String[] params = command.split(" ");
                if ( params.length != 3 ) {
                    System.out.println("need input mobile and password!");
                    continue;
                }
                login(params[1], params[2]);
                continue;
            }


        }
    }



}
