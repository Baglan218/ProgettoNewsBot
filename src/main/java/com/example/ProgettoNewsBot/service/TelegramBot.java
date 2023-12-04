package com.example.ProgettoNewsBot.service;


import com.example.ProgettoNewsBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class  TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    static final String  HELP_TEXT = "This bot is created to demontration";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listofCommand = new ArrayList<>();
        listofCommand.add(new BotCommand("/start","Начать взаимодействие с ботом"));
        listofCommand.add(new BotCommand("/mydata","Получить информацию о вашем профиле"));
        listofCommand.add(new BotCommand("/deletedata","Удалить ваши личные данные"));
        listofCommand.add(new BotCommand("/help","Показать это сообщение справки"));
        listofCommand.add(new BotCommand("/developer","Получить информацию о разработчик"));
        listofCommand.add(new BotCommand("/contact","Связаться с нами"));
        try{
            this .execute(new SetMyCommands(listofCommand, new BotCommandScopeDefault(),null));
        }
        catch(TelegramApiException e){
            log.error("Error setting bot's command list: "+ e.getMessage());

        }


    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken(){
        return config.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()&& update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId=update.getMessage().getChatId();




            switch(messageText){
                case "/start":
                    startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
                break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;

                    default:sendMessage(chatId,"Извините, эта команда не распознана. Если у вас есть вопросы или вам нужна помощь, воспользуйтесь командой /help или свяжитесь с нашей поддержкой через /contact.\n");
            }
        }


    }
    private void startCommandReceived(long chatId, String name){

        String answer = "Привет,"+name +"!Добро пожаловать в Progetto - твое креативное пространство в мире дизайна! \uD83C\uDFA8✨ Я здесь, чтобы помочь тебе быть в курсе последних новостей, делиться шрифтами, текстурами и 3D-моделями, а также вдохновляться статьями от талантливых дизайнеров. Готов к творчеству? \uD83D\uDCA1✏\uFE0F\"";
        log.info("Replied to user "+name);


        sendMessage(chatId,answer);

    }
    private void sendMessage(long chatId,String  textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try{
            execute(message);
        }catch (TelegramApiException e) {
           log.error("Error occured: "+e.getMessage());
        }
    }


}
