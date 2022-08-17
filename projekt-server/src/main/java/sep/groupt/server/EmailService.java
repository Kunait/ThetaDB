package sep.groupt.server;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.objects.Email;
import sep.groupt.server.dataclass.Einladung;
import sep.groupt.server.dataclass.Nutzer;
import sep.groupt.server.dataclass.Report;
import sep.groupt.server.dataclass.Systemadministrator;

import java.io.IOException;
import java.sql.SQLException;

public class EmailService {
    final private static String hostname = "smtp.sendgrid.net";
    final private static int smtpPort = 587;
    final private static String username = "apikey";
    private static String passwort = "";
    private static String email;
    private static String sendgridEmailAPIKEY = "";




    public static void sendSecurityPass(Nutzer nutzer, String code){
        System.out.println(code);
    }


    /*

    public static void sendSecurityPass(Nutzer nutzer, String code) throws IOException {


        Email from = new Email(email);
        String subject = "ThetaDB Sicherheitscode";
        Email to = new Email(nutzer.getEmailAdresse());
        Content content = new Content("text/plain", "Hier ist ihr Code: "+ code);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridEmailAPIKEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }


     */





    public static void sendRepMail(Report rep, Systemadministrator s) throws IOException {
        Email from = new Email(email);
        String subject = "Neuer Fehlerreport";
        Email to = new Email(s.getEmailAdresse());
        Content content = new Content("text/plain", "Es wurde ein fehlerhafter Eintrag für einen Film gefunden( "+ rep.getDate()+ ")");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridEmailAPIKEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }

    }

    public static void sendEinMail(Einladung ein) throws IOException {
        Email from = new Email(email);
        String subject = ein.getUser1().getVorname() + " will mit dir einen Film gucken";
        Email to = new Email(ein.getUser2().getEmailAdresse());
        Content content;
        if(!ein.getText().equals("") && !ein.getText().equals(null)){
            content = new Content("text/plain", ein.getUser1().getVorname() +" hat dich zu "+ ein.getFilm().getName() + " eingeladen: \n \n " + ein.getText());
        } else{
            content = new Content("text/plain", ein.getUser1().getVorname() +" hat dich zu "+ ein.getFilm().getName() + " eingeladen");
        }

        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendgridEmailAPIKEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }

    }

    public static void sendEinResponseMail(Einladung ein) throws IOException {
        Email from = new Email(email);
        String subject;
        Einladung zweinladung = new Einladung();
        zweinladung.setDate(String.valueOf(ein.getUser1().getUserID()));
        zweinladung.setUser2(ein.getUser1());
        if(ein.isAngenommen()){
            subject = ein.getUser2().getVorname() + " hat deine Einladung angenommen";
            zweinladung.setText(ein.getUser2().getVorname() + " hat deine Einladung angenommen!");
        } else{
            subject = ein.getUser2().getVorname() + " hat deine Einladung abgelehnt";
            zweinladung.setText(ein.getUser2().getVorname() + " hat deine Einladung nicht angenommen.");
        }
        try {
            Datenbank.addEinladungResp(zweinladung);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Email to = new Email(ein.getUser1().getEmailAdresse());
        Content content;
        if(ein.isAngenommen()){
            content = new Content("text/plain", ein.getUser2().getVorname() +" hat deine Einladung angenommen");
        } else{
            content = new Content("text/plain", ein.getUser2().getVorname() +" hat deine Einladung abgelehnt");
        }
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(sendgridEmailAPIKEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }

    }

    public static void setEmail(String newEmail) {
        email = newEmail;
    }

    public static void setSendgridEmailAPIKEY(String apikey){
        sendgridEmailAPIKEY = apikey;
    }

    public static void setPasswort(String newPassword) {
        passwort = newPassword;
    }


}
