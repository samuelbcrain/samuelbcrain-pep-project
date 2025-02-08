package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        ObjectMapper om = new ObjectMapper();
        //app.get("example-endpoint", this::exampleHandler);

        // add an account
        app.post("/register", ctx -> {
            // get account info from user request
            String newUserJsonString = ctx.body();
            Account account = om.readValue(newUserJsonString, Account.class);

            //add account to db
            AccountService accountService = new AccountService();
            Account addedAccount = accountService.addAccount(account);

            // report if account added successfully
            if (addedAccount != null) {
                // positive response
                ctx.status(200);
                ctx.json(addedAccount);
            } else {
                // negative response
                ctx.status(400);
            }
        });

        // log into app
        app.post("/login", ctx -> {
            // get user login info
            String userJsonString = ctx.body();
            Account account = om.readValue(userJsonString, Account.class);

            // try login
            AccountService accountService = new AccountService();
            Account matchingAccount = accountService.getAccountByUsernameAndPassword(account.getUsername(),
                                    account.getPassword());
            
            // report login success/fail
            if (matchingAccount != null) {
                // positive response
                ctx.status(200);
                ctx.json(matchingAccount);
            } else {
                // negative response
                ctx.status(401);
            }
        });

        // add message to app
        app.post("/messages", ctx -> {
            // get message info
            String messageJsonString = ctx.body();
            Message message = om.readValue(messageJsonString, Message.class);

            // add message to db
            MessageService messageService = new MessageService();
            Message addedMessage = messageService.addMessage(message);

            // report if message added successfully
            if (addedMessage != null) {
                // positive response
                ctx.status(200);
                ctx.json(addedMessage);
            } else {
                // negative response
                ctx.status(400);
            }
        });

        // get all messages from app
        app.get("/messages", ctx -> {
            // get all messages
            MessageService messageService = new MessageService();
            List<Message> allMessages = messageService.getAllMessages();
            // respond with messages
            ctx.status(200);
            ctx.json(allMessages);
        });

        // get message by id
        app.get("/messages/{message_id}", ctx -> {
            // get requested message id
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));

            // get message with requested id
            MessageService messageService = new MessageService();
            Message message = messageService.getMessageByID(message_id);

            // respond positive
            ctx.status(200);
            // if message exists, respond with message
            if (message != null) {
                ctx.json(message);
            }
        });

        // delete message by id
        app.delete("/messages/{message_id}", ctx -> {
            // get id for message requested to delete
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));

            // attempt to delete message with given id
            MessageService messageService = new MessageService();
            Message message = messageService.deleteMessageByID(message_id);

            // respond positive
            ctx.status(200);
            // if message existed, respond with message
            if (message != null) {
                ctx.json(message);
            }
        });

        // update message text by id
        app.patch("/messages/{message_id}", ctx -> {
            // get requested message id and new text
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            String messageJsonString = ctx.body();

            // create message object with new text and id of message to change
            Message messageWithUpdatedText = om.readValue(messageJsonString, Message.class);
            messageWithUpdatedText.setMessage_id(message_id);

            // update text of message in db
            MessageService messageService = new MessageService();
            Message messageAfterUpdate = messageService.updateMessageText(messageWithUpdatedText);

            // respond if message updated
            if (messageAfterUpdate != null) {
                // positive response
                ctx.status(200);
                ctx.json(messageAfterUpdate);
            } else {
                // negative response
                ctx.status(400);
            }
        });

        // get all messages for an account
        app.get("/accounts/{account_id}/messages", ctx -> {
            // get requested account id
            int account_id = Integer.parseInt(ctx.pathParam("account_id"));

            // get all messages for requested account id
            MessageService messageService = new MessageService();
            List<Message> allMessagesForAccount = messageService.getAllMessagesByAccountID(account_id);

            // respond positive
            ctx.status(200);
            // respond with list of all messages for given account id
            ctx.json(allMessagesForAccount);
        });

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}