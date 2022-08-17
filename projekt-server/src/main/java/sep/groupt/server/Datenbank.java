package sep.groupt.server;

import sep.groupt.server.dataclass.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Datenbank {

    private static String url = "jdbc:mysql://localhost:3306";
    private static String user = "root";
    private static String pass = "";

    private static Connection connection;

    // Setter

    public static void setUser(String username){
        user = username;
    }

    public static void setPassword(String password){
        pass = password;
    }


    public static Connection getConnection() {
        return connection;
    }

    // Verbindet sich mit der Datenbank & erstellt Schema und connected sich mit dem Schema & erstellt die Tabellen, falls sie nicht vorhanden sind

    public static void setupDatabase() throws SQLException {
        connect();
        checkDatabase();
        createTables();



    }

    // Verbindet sich mit dem MYSQL server und kann über die Connection statements senden

    public static void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
    }

    // Überprüft, ob das Schema sep existiert, wenn nein dann wird eins erstellt

    public static void checkDatabase() throws SQLException {
        String query = "CREATE SCHEMA IF NOT EXISTS sep";

        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Database Schema is ready!");
        url = "jdbc:mysql://localhost:3306/sep";
        connection = DriverManager.getConnection(url, user, pass);
    }

    /*
     Erstellt in der Datenbank sep Tabellen mit:
        Systemadministrator (Admin_id, EmailAdresse, Vorname, Nachname, Salt, Passwort)
        Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner)

     */

    public static void createTables() throws SQLException {
        createAdminTable();
        createFilmTable();
        createNutzerTable();
        createEinladungTable();
        createReportTable();
        createFreundeTable();
        createBewertunglistTable();
        createSeenlistTable();
        createWatchlistTable();
        createFreundAnfrageTable();
        createStatsAdminTable();
        createDiskussionenTable();
        createNutzerDiskussionen();
        try {
           // addDiskussion("Test", getUserByID(3), false);

            Diskussionsgruppe[] test = getDiskussionen(getUserByID(1));
            for(Diskussionsgruppe t : test){
                System.out.println(t.toString());
            }
        }catch (SQLException e){

            e.printStackTrace();
        }
    }

    // Erstellt eine Systemadminstrator Tabelle in der Datenbank, falls es keine existiert

    private static void createAdminTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Systemadministrator (Admin_id INT NOT NULL AUTO_INCREMENT, EmailAdresse VARCHAR(45) NOT NULL, Vorname VARCHAR(45) NOT NULL, Nachname VARCHAR(45) NOT NULL, Salt VARCHAR(25) NOT NULL, Passwort VARCHAR(255) NOT NULL, PRIMARY KEY (`Admin_id`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Systemadministrator is ready!");
    }

    private static void createReportTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Report (RepID INT NOT NULL AUTO_INCREMENT, RepDate VARCHAR(45) NOT NULL, FilmID VARCHAR(45) NOT NULL, Gelesen VARCHAR(45) NOT NULL, RepText VARCHAR(255) NOT NULL, PRIMARY KEY (`RepID`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Report is ready!");
    }

    private static void createEinladungTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Einladung (EinladungID INT NOT NULL AUTO_INCREMENT, User1 VARCHAR(45) NOT NULL, User2 VARCHAR(45) NOT NULL, FilmID VARCHAR(45) NOT NULL, Date VARCHAR(45) NOT NULL, Text VARCHAR(255) NOT NULL, PRIMARY KEY (`EinladungID`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Einladung is ready!");
    }

    // Erstellt eine Film Tabelle in der Datenbank, falls es keine existiert

    private static void createFilmTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Film (Film_id INT NOT NULL AUTO_INCREMENT, Filmname VARCHAR(100) NOT NULL, FilmKategorie VARCHAR(50) NOT NULL, Filmlaenge VARCHAR(25) NOT NULL, Erscheinungsdatum VARCHAR(25) NOT NULL, Regisseur VARCHAR (30) NOT NULL, Drehbuchautor VARCHAR (30) NOT NULL, Cast VARCHAR(255) NOT NULL, Filmbanner VARCHAR(255) NULL, PRIMARY KEY (`Film_id`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Film is ready!");
    }

    // Erstellt eine Nutzer Tabelle in der Datenbank, falls es keine existiert

    private static void createNutzerTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Nutzer (Nutzer_id INT NOT NULL AUTO_INCREMENT, EmailAdresse VARCHAR(255) NOT NULL, Vorname VARCHAR(255) NOT NULL, Nachname VARCHAR(255) NOT NULL, Salt VARCHAR(15) NOT NULL, Einstellungen VARCHAR(10) NOT NULL, Passwort VARCHAR(255) NOT NULL, Profilbild VARCHAR(255), PRIMARY KEY (`Nutzer_id`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Nutzer is ready!");
    }

    private static void createFreundAnfrageTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Freundschaftsanfrage (Nutzer_id INT NOT NULL, anfrager_id INT NOT NULL, " +
                "CONSTRAINT pk PRIMARY KEY (Nutzer_id, anfrager_id), " +
                "CONSTRAINT fk3 FOREIGN KEY (Nutzer_id) references Nutzer(Nutzer_id) " +
                "ON UPDATE cascade " +
                "ON DELETE cascade, " +
                "CONSTRAINT fk4 FOREIGN KEY (anfrager_id) references Nutzer(Nutzer_id) " +
                "ON UPDATE cascade " +
                "ON DELETE cascade);";

        Statement create = connection.createStatement();
        create.execute(query);
        create.close();
        System.out.println("Table Freundschaftsanfrage is ready!");
    }
    private static void createFreundeTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Freundesliste (Nutzer_id INT NOT NULL, freund_id INT NOT NULL, " +
                "CONSTRAINT pk PRIMARY KEY (Nutzer_id, freund_id), " +
                "CONSTRAINT fk1 FOREIGN KEY (Nutzer_id) references Nutzer(Nutzer_id) " +
                "ON UPDATE cascade " +
                "ON DELETE cascade, " +
                "CONSTRAINT fk2 FOREIGN KEY (freund_id) references Nutzer(Nutzer_id) " +
                "ON UPDATE cascade " +
                "ON DELETE cascade);";

        Statement create = connection.createStatement();
        create.execute(query);
        create.close();
        System.out.println("Table Freundesliste is ready!");
    }

    private static void createDiskussionenTable() throws SQLException{

        String query = "CREATE TABLE IF NOT EXISTS Diskussionen (Gruppe_id INT NOT NULL AUTO_INCREMENT, Gruppe_name VARCHAR(255), Nutzer_id INT NOT NULL, Privat VARCHAR(255), PRIMARY KEY(Gruppe_id));";
        Statement create = connection.createStatement();
        create.execute(query);
        create.close();
        System.out.println("Table Diskussionen is ready!");

    }
    private static void createNutzerDiskussionen() throws SQLException{

        String query = "CREATE TABLE IF NOT EXISTS nutzerdiskussionen (Nutzer_id INT NOT NULL, Gruppe_id INT NOT NULL);";
        Statement create = connection.createStatement();
        create.execute(query);
        create.close();
        System.out.println("Table NutzerDiskussionen is ready!");

    }

    /*
     Füge Dummy Admins für alle Gruppenmitglieder in die Datenbank ein und 2 Filme in die Filmdatenbank
     */

    public static void addDummies() throws SQLException {

    }


    // Überprüft, ob es einen Admin mit der EmailAdresse und dem Passwort existiert und gibt den entsprechenden Booleanwert zurück
    public static void addBewertung(Bewertung bewertung) throws SQLException {
        String query = "INSERT INTO Bewertung(film_ID, nutzer_ID, Punkte, bewertung) VALUES " +
                "" + "('" + bewertung.getFilmID() + "','" + bewertung.getUserID() + "','" + bewertung.getPunkte() + "','" + bewertung.getBewertung() + "')";

        Statement insert = connection.createStatement();
        insert.execute(query);


        insert.close();
    }

    public static Bewertung[] getBewertungen(int filmid) throws SQLException {
        ArrayList<Bewertung> tempBewertungList = new ArrayList<>();

        // Datenbankabfrage
        String query = "SELECT * FROM bewertung WHERE Film_id= '" + filmid + "';";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Bewertung tempBewertung = new Bewertung(RS.getInt("Bewertung_id"), RS.getInt("Film_id"), RS.getInt("Nutzer_id"), RS.getInt("Punkte"), RS.getString("Bewertung"));
            tempBewertungList.add(tempBewertung);
        }
        Bewertung[] newBewertungList = new Bewertung[tempBewertungList.size()];
        newBewertungList = tempBewertungList.toArray(newBewertungList);
        return newBewertungList;
    }

    public static boolean checkAdmin(Systemadministrator admin) throws SQLException {
        String query = "SELECT * FROM Systemadministrator WHERE EmailAdresse = '" + admin.getEmailAdresse() + "';";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()) {
            if (admin.getEmailAdresse().equalsIgnoreCase(RS.getString("EmailAdresse"))) {
                getData.close();
                return true;
            }
        }
        getData.close();
        return false;
    }


    // Fügt in der Tabelle Systemadministrator einen neuen Admin hinzu

    public static void addAdmin(Systemadministrator admin) throws SQLException {
        if (!checkAdmin(admin)) {
            String query = "INSERT INTO Systemadministrator (EmailAdresse, Vorname, Nachname, Salt, Passwort) VALUES " +
                    "" + "('" + admin.getEmailAdresse() + "','" + admin.getVorname() + "','" + admin.getNachname() + "','" + admin.getSalt() + "','" + admin.getPasswort() + "')";

            Statement insert = connection.createStatement();
            insert.execute(query);
            insert.close();
            System.out.println("Admin hat sich mit E-Mail: " + admin.getEmailAdresse() + " registriert!");
        }
    }


    // Gibt einen Systemadministrator zurück der mit EmailAdresse und Passwort übereinstimmen

    public static Systemadministrator getAdmin(Systemadministrator admin) throws SQLException {
        if (checkAdmin(admin)) {
            String query = "SELECT * FROM Systemadministrator WHERE EmailAdresse = '" + admin.getEmailAdresse() + "';";
            Statement getData = connection.createStatement();
            ResultSet RS = getData.executeQuery(query);
            while (RS.next()) {
                admin.setAdminID(RS.getInt("Admin_id"));
                admin.setVorname(RS.getString("Vorname"));
                admin.setNachname(RS.getString("Nachname"));
                admin.setSalt(RS.getString("Salt"));
                admin.setPasswort(RS.getString("Passwort"));
            }
            getData.close();
            return admin;
        } else {
            return null;
        }
    }


    // Überprüft, ob es einen Nutzer mit der E-MailAdresse existiert

    public static boolean checkNutzer(Nutzer nutzer) throws SQLException {
        String query = "SELECT EmailAdresse FROM Nutzer WHERE EmailAdresse = '" + nutzer.getEmailAdresse() + "';";
        Statement getData =
                connection.createStatement();
        ResultSet RS = getData.executeQuery(query);


        //System.out.println(RS.first());
        while (RS.next()) {
            if (nutzer.getEmailAdresse().equalsIgnoreCase(RS.getString("EmailAdresse"))) {
                getData.close();
                return true;
            }
        }
        getData.close();
        return false;
    }

    public static boolean checkFriend(Nutzer user, Nutzer potFriend) throws SQLException {

        String query = "SELECT * FROM freundesliste WHERE Nutzer_id="+user.getUserID()+" AND freund_id="+ potFriend.getUserID()+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if(RS.next())
            return true;
        return false;
    }

    public static void addDiskussion(String name, Nutzer user, boolean privat) throws SQLException {



        String query = "INSERT INTO Diskussionen(Gruppe_name, Nutzer_id, Privat) VALUES ('"+ name+"','"+user.getUserID()+"','"+String.valueOf(privat)+"');";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        String queryTest = "SELECT * FROM Diskussionen WHERE Gruppe_id=(SELECT max(Gruppe_id) FROM Diskussionen);";
        Statement data = connection.createStatement();
        ResultSet RS = data.executeQuery(queryTest);
        int id=0;
        if(RS.next()) {
             id = RS.getInt("Gruppe_id");
        }
        if(id != 0) {
            String queryInsert = "INSERT INTO Nutzerdiskussionen(Nutzer_id, Gruppe_id) VALUES ('" + user.getUserID() + "','" + id + "');";
            Statement insert2 = connection.createStatement();
            insert2.execute(queryInsert);
            insert2.close();
        }
    }

    public static void joinDiskussion(Diskussionsgruppe gruppe, Nutzer nutzer) throws SQLException {

        String query = "SELECT * FROM nutzerdiskussionen WHERE Nutzer_id="+nutzer.getUserID()+" AND gruppe_id="+ gruppe.getId()+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if(!RS.next()){
            String queryInsert = "INSERT INTO Nutzerdiskussionen(Nutzer_id, Gruppe_id) VALUES ('"+ nutzer.getUserID()+"','"+gruppe.getId()+"');";
            Statement insert = connection.createStatement();
            insert.execute(queryInsert);
            insert.close();
        }

    }

    public static Diskussionsgruppe[] getDiskussionen(Nutzer user) throws SQLException {

        ArrayList<Diskussionsgruppe> gruppen = new ArrayList<>();

        String query = "SELECT * FROM nutzerdiskussionen WHERE Nutzer_id="+user.getUserID()+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while(RS.next()){

            Diskussionsgruppe gruppe = getGruppeByID(RS.getInt("Gruppe_id"));
            gruppen.add(gruppe);


        }

        Diskussionsgruppe[] array = new Diskussionsgruppe[gruppen.size()];
        array = gruppen.toArray(array);

        return array;
    }

    public static Diskussionsgruppe getGruppeByID(int id) throws SQLException {

        String query = "SELECT * FROM diskussionen WHERE Gruppe_id="+id+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if(RS.next()){

            boolean temp = RS.getString("Privat").toLowerCase().equals("true");
            Diskussionsgruppe gruppe = new Diskussionsgruppe(getUserByID(RS.getInt("Nutzer_id")),RS.getString("Gruppe_name"),temp,RS.getInt("Gruppe_id"));
            return gruppe;
        }
        return null;
    }

    public static Diskussionsgruppe[] getVisibleDiskussionsgruppen(Nutzer user) throws SQLException {

        ArrayList<Diskussionsgruppe> gruppen = new ArrayList<>();

        String query = "SELECT * FROM diskussionen;";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while(RS.next()){
            boolean temp = RS.getString("Privat").toLowerCase().equals("true");
            Diskussionsgruppe gruppe = new Diskussionsgruppe(getUserByID(RS.getInt("Nutzer_id")),RS.getString("Gruppe_name"),temp,RS.getInt("Gruppe_id"));
            if(!checkDiskussion(user,gruppe)) {
                if (!gruppe.isPrivat()) {
                    gruppen.add(gruppe);
                    //System.out.println(gruppe.isPrivat());
                } else {

                    if (checkFriend(user, getUserByID(gruppe.getNutzer().getUserID()))) {
                        gruppen.add(gruppe);
                    }
                }
            }

        }

        Diskussionsgruppe[] array = new Diskussionsgruppe[gruppen.size()];

        array = gruppen.toArray(array);

        return array;

    }

    public static boolean checkDiskussion(Nutzer user, Diskussionsgruppe gruppe) throws SQLException {

        String query = "SELECT * FROM nutzerdiskussionen WHERE Nutzer_id="+user.getUserID()+" AND gruppe_id="+ gruppe.getId()+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if(RS.next()) {
        return true;
        }else
            return false;
        }



    // Fügt in der Tabelle Nutzer einen neuen Datensatz hinzu

    public static void addNutzer(Nutzer nutzer) throws SQLException {
        if (!checkNutzer(nutzer)) {
            String query = "INSERT INTO Nutzer(EmailAdresse, Vorname, Nachname, Salt, Einstellungen, Passwort) VALUES (" +
                    "'" + nutzer.getEmailAdresse() + "', " +
                    "'" + nutzer.getVorname() + "', " +
                    "'" + nutzer.getNachname() + "', " +
                    "'" + nutzer.getSalt() + "', " +
                    "'" + nutzer.getEinstellungen() + "', " +
                    "'" + nutzer.getPasswort() + "');";

            Statement insert = connection.createStatement();
            insert.execute(query);
            insert.close();
        }
    }

    public static Nutzer[] nutzerSearch(String filter) throws SQLException {

        String query = "SELECT * FROM Nutzer WHERE Vorname LIKE '%" + filter + "%';";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        ArrayList<Nutzer> liste = new ArrayList<Nutzer>();

        while (RS.next()) {

            Nutzer nutzer = new Nutzer();
            nutzer.setUserID(RS.getInt("Nutzer_id"));
            nutzer.setEmailAdresse(RS.getString("EmailAdresse"));
            nutzer.setVorname(RS.getString("Vorname"));
            nutzer.setNachname(RS.getString("Nachname"));
            nutzer.setSalt(RS.getString("Salt"));
            nutzer.setEinstellungen(RS.getString("Einstellungen"));
            nutzer.setPasswort(RS.getString("Passwort"));
            nutzer.setProfilbildURL(RS.getString("Profilbild"));
            liste.add(nutzer);
        }

        Nutzer[] objects = new Nutzer[liste.size()];
        int i = 0;
        for(Nutzer n : liste){
            objects[i] = n;

            i++;
        }

        return objects;

    }

    public static void addNutzerWithImage(Nutzer nutzer) throws SQLException {
        if (!checkNutzer(nutzer)) {
            String query = "INSERT INTO Nutzer(EmailAdresse, Vorname, Nachname, Salt, Einstellungen, Passwort, Profilbild) VALUES (" +
                    "'" + nutzer.getEmailAdresse() + "', " +
                    "'" + nutzer.getVorname() + "', " +
                    "'" + nutzer.getNachname() + "', " +
                    "'" + nutzer.getSalt() + "', " +
                    "'" + nutzer.getEinstellungen() + "', " +
                    "'" + nutzer.getPasswort() + "', " +
                    "'" + nutzer.getProfilbildURL() + "');";

            Statement insert = connection.createStatement();
            insert.execute(query);
            insert.close();
        }
    }


    // Speichert Profilbild URL in dem Datensatz mit der Nutzerid ab

    public static void setProfilBildNutzer(Nutzer nutzer) throws SQLException{
        if (checkNutzer(nutzer)){
            String query = "UPDATE Nutzer SET Profilbild = '" + nutzer.getProfilbildURL() + "' WHERE Nutzer_id = '" + nutzer.getUserID() + "';";
            Statement update = connection.createStatement();
            update.execute(query);
            update.close();
        }
    }

    public static void changeNutzer(Nutzer nutzer) throws SQLException{
        if (checkNutzer(nutzer)){
            String temp = nutzer.getEinstellungen();
            nutzer = getNutzer(nutzer);
            String query = "UPDATE Nutzer SET Einstellungen = '" + temp + "' WHERE Nutzer_id = '" + nutzer.getUserID() + "';";
            Statement update = connection.createStatement();
            update.execute(query);
            update.close();
        }
    }


    // Gibt eine Nutzerklasse zurück mit dem die E-MailAdresse übereinstimmt.

    public static Nutzer getNutzer(Nutzer nutzer) throws SQLException {
        if (checkNutzer(nutzer)) {
            String query = "SELECT * FROM Nutzer WHERE EmailAdresse = '" + nutzer.getEmailAdresse() + "';";
            Statement getData = connection.createStatement();
            ResultSet RS = getData.executeQuery(query);

            while (RS.next()) {
                nutzer.setUserID(RS.getInt("Nutzer_id"));
                nutzer.setVorname(RS.getString("Vorname"));
                nutzer.setNachname(RS.getString("Nachname"));
                nutzer.setSalt(RS.getString("Salt"));
                nutzer.setEinstellungen(RS.getString("Einstellungen"));
                nutzer.setPasswort(RS.getString("Passwort"));
                nutzer.setProfilbildURL(RS.getString("Profilbild"));
                nutzer.setEmailAdresse(RS.getString("EmailAdresse"));
            }
            return nutzer;
        } else {
            return null;
        }
    }

    public static Nutzer getUserByID(int id) throws SQLException {

        String query = "SELECT * FROM Nutzer WHERE Nutzer_id = '" + id + "';";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        Nutzer nutzer = new Nutzer();
        while (RS.next()) {
            nutzer.setUserID(RS.getInt("Nutzer_id"));
            nutzer.setEmailAdresse(RS.getString("EmailAdresse"));
            nutzer.setVorname(RS.getString("Vorname"));
            nutzer.setNachname(RS.getString("Nachname"));
            nutzer.setSalt(RS.getString("Salt"));
            nutzer.setEinstellungen(RS.getString("Einstellungen"));
            nutzer.setPasswort(RS.getString("Passwort"));
            nutzer.setProfilbildURL(RS.getString("Profilbild"));
        }


        return nutzer;
    }
    public static Nutzer getUserByIDWithoutPic(int id) throws SQLException {

        String query = "SELECT * FROM Nutzer WHERE Nutzer_id = '" + id + "';";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        Nutzer nutzer = new Nutzer();
        while (RS.next()) {
            nutzer.setUserID(RS.getInt("Nutzer_id"));
            nutzer.setEmailAdresse(RS.getString("EmailAdresse"));
            nutzer.setVorname(RS.getString("Vorname"));
            nutzer.setNachname(RS.getString("Nachname"));
            nutzer.setSalt(RS.getString("Salt"));
            nutzer.setEinstellungen(RS.getString("Einstellungen"));
            nutzer.setPasswort(RS.getString("Passwort"));

        }


        return nutzer;
    }

    public static String getNutzerID(Nutzer nutzer) throws SQLException {
        String result = null;
        if (checkNutzer(nutzer)) {
            String query = "SELECT Nutzer_id FROM Nutzer WHERE EmailAdresse = '" + nutzer.getEmailAdresse() + "';";
            Statement getData = connection.createStatement();
            ResultSet RS = getData.executeQuery(query);

            while (RS.next()) {
                result = RS.getString("Nutzer_id");
            }
            return result;
        }
        return null;
    }

    public static void addFriend(Nutzer user, Nutzer newFriend) throws SQLException{

        String queryVorher = "SELECT * FROM freundesliste WHERE Nutzer_id ='"+user.getUserID()+"' AND freund_id ='"+newFriend.getUserID()+"';";
        Statement statement = connection.createStatement();
        ResultSet RS = statement.executeQuery(queryVorher);

        if(!RS.next()) {

            String query = "INSERT INTO freundesliste(Nutzer_id, freund_id) VALUES " +
                    "" + "('" + user.getUserID() + "','" + newFriend.getUserID() + "')";
            Statement insert = connection.createStatement();
            insert.execute(query);

            insert.close();
            deleteFriendRequest(user,newFriend);
        }

    }

    public static Nutzer[] getAnfragen(Nutzer nutzer) throws SQLException {
        if (checkNutzer(nutzer)) {
            String query = "SELECT * FROM freundschaftsanfrage WHERE Nutzer_id = '" + nutzer.getUserID() + "';";
            Statement getData = connection.createStatement();
            ResultSet RS = getData.executeQuery(query);
            ArrayList<Nutzer> nutzers = new ArrayList<Nutzer>();
            while (RS.next()) {
                Nutzer n = new Nutzer();
                n.setUserID(RS.getInt("anfrager_id"));
                n.setVorname(getUserByID(n.getUserID()).getVorname());
                n.setNachname(getUserByID(n.getUserID()).getNachname());
                n.setEmailAdresse(getUserByID(n.getUserID()).getEmailAdresse());
                nutzers.add(n);
            }

            Nutzer[] nutzerArray = new Nutzer[nutzers.size()];
            nutzerArray = nutzers.toArray(nutzerArray);
            return nutzerArray;
        } else{

            return null;
        }
    }


    public static void addFriendRequest(Nutzer user, Nutzer newFriend) throws SQLException{

        String queryVorher = "SELECT * FROM freundschaftsanfrage WHERE Nutzer_id ='"+newFriend.getUserID()+"' AND anfrager_id ='"+user.getUserID()+"';";
        Statement statement = connection.createStatement();
        ResultSet RS = statement.executeQuery(queryVorher);

        if(!RS.next()) {

            String query = "INSERT INTO freundschaftsanfrage(Nutzer_id, anfrager_id) VALUES " +
                    "" + "('" + newFriend.getUserID() + "','" + user.getUserID() + "')";
            Statement insert = connection.createStatement();
            insert.execute(query);

            insert.close();
        }

    }

    public static void deleteFriend(Nutzer user, Nutzer friend) throws SQLException {
        String queryVorher = "SELECT * FROM freundesliste WHERE Nutzer_id ='"+user.getUserID()+"' AND freund_id ='"+ friend.getUserID()+"';";
        Statement statement = connection.createStatement();
        ResultSet RS = statement.executeQuery(queryVorher);

        if(RS.next()){

            String query = "DELETE FROM freundesliste WHERE Nutzer_id ='" + user.getUserID() + "' AND freund_id ='"+ friend.getUserID()+"';";
            Statement insert = connection.createStatement();
            insert.execute(query);
            insert.close();

            String query2 = "DELETE FROM freundesliste WHERE Nutzer_id ='" + friend.getUserID() + "' AND freund_id ='"+ user.getUserID()+"';";
            Statement insert2 = connection.createStatement();
            insert2.execute(query2);
            insert2.close();

        }

    }

    public static void deleteFriendRequest(Nutzer user, Nutzer friend) throws SQLException {
        String queryVorher = "SELECT * FROM freundschaftsanfrage WHERE Nutzer_id ='"+user.getUserID()+"' AND anfrager_id ='"+ friend.getUserID()+"';";
        Statement statement = connection.createStatement();
        ResultSet RS = statement.executeQuery(queryVorher);

        if(RS.next()){

            String query = "DELETE FROM freundschaftsanfrage WHERE Nutzer_id ='" + user.getUserID() + "' AND anfrager_id ='"+ friend.getUserID()+"';";
            Statement insert = connection.createStatement();
            insert.execute(query);
            insert.close();

        }

    }
    public static Nutzer[] getFreundeslisteForUbersicht(int nutzer_id) throws SQLException{
        ArrayList<Nutzer> freundesliste = new ArrayList<>();
        Nutzer[] ergebnis;

        String query = "SELECT Nutzer.Nutzer_id, Vorname, Nachname " +
                "FROM Nutzer INNER JOIN freundesliste ON (Freundesliste.freund_id = Nutzer.Nutzer_id and " +
                "freundesliste.Nutzer_id = " + nutzer_id + ");";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()){
            Nutzer tempNutzer = new Nutzer();
            tempNutzer.setUserID(RS.getInt("Nutzer_id"));
            tempNutzer.setVorname(RS.getString("Vorname"));
            tempNutzer.setNachname(RS.getString("Nachname"));

            freundesliste.add(tempNutzer);
        }

        ergebnis = new Nutzer[freundesliste.size()];
        ergebnis = freundesliste.toArray(ergebnis);
        return ergebnis;
    }

    public static Film[] getWatchListeForUbersicht(int nutzer_id) throws SQLException{
        ArrayList<Film> watchListe = new ArrayList<>();
        Film[] ergebnis;

        String query = "SELECT Film_id, Filmname FROM watchlist NATURAL JOIN film WHERE nutzer_id =" + nutzer_id + ";";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()){
            Film tempFilm = new Film();
            tempFilm.setFilm_id(RS.getInt("Film_id"));
            tempFilm.setName(RS.getString("Filmname"));

            watchListe.add(tempFilm);
        }
        ergebnis = new Film[watchListe.size()];
        ergebnis = watchListe.toArray(ergebnis);
        return ergebnis;
    }

    public static Film[] getSeenListeForUbersicht(int nutzer_id) throws SQLException{
        ArrayList<Film> seenListe = new ArrayList<>();
        Film[] ergebnis;
        String query = "SELECT Film_id, Filmname FROM seenlist NATURAL JOIN film WHERE nutzer_id =" + nutzer_id + ";";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()){
            Film tempFilm = new Film();
            tempFilm.setFilm_id(RS.getInt("Film_id"));
            tempFilm.setName(RS.getString("Filmname"));

            seenListe.add(tempFilm);
        }
        ergebnis = new Film[seenListe.size()];
        ergebnis = seenListe.toArray(ergebnis);
        return ergebnis;
    }


    public static Film[] getSeenListe(int id) throws SQLException, IOException {
        ArrayList<Film> seenListe = new ArrayList<>();
        Nutzer nutzer = getUserByID(id);
        Film[] ergebnis;
        if (checkNutzer(nutzer)){
            String query = "SELECT * FROM seenlist NATURAL JOIN film WHERE nutzer_id =" + nutzer.getUserID() + ";";

            Statement getData = connection.createStatement();
            ResultSet RS = getData.executeQuery(query);

            while (RS.next()){
                Film tempFilm = new Film();
                tempFilm.setFilm_id(RS.getInt("Film_id"));
                tempFilm.setName(RS.getString("Filmname"));
                tempFilm.setCategory(RS.getString("FilmKategorie"));
                tempFilm.setLength(RS.getInt("Filmlaenge"));
                tempFilm.setDate(RS.getString("Erscheinungsdatum"));
                tempFilm.setRegisseur(RS.getString("Regisseur"));
                tempFilm.setAuthor(RS.getString("Drehbuchautor"));
                tempFilm.setCast(RS.getString("Cast"));

                //String bildname = tempFilm.getName().replaceAll(":", "").replaceAll("\\*", "").replaceAll("/", "").replaceAll("[^a-zA-Z0-9]", " ");
                String bildname = String.valueOf(tempFilm.getFilm_id());
                File banner = new File("banner/" + bildname + ".jpg");

                if (banner != null){
                    FileInputStream fileInputStream = new FileInputStream(banner);
                    tempFilm.setBanner(fileInputStream.readAllBytes());
                    fileInputStream.close();
                }
                else {

                }

                seenListe.add(tempFilm);
            }
            ergebnis = new Film[seenListe.size()];
            ergebnis = seenListe.toArray(ergebnis);
            return ergebnis;

        }
        ergebnis = new Film[seenListe.size()];
        ergebnis = seenListe.toArray(ergebnis);
        return ergebnis;
    }

    // Fügt in der Tabelle Filme einen neuen Film hinzu

    public static void addMovie(Film film) throws SQLException {
        String query = "INSERT INTO Film(Filmname, FilmKategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast) VALUES " +
                "" + "('" + film.getName() + "','" + film.getCategory() + "','" + Integer.toString(film.getLength()) + "','" + film.getDate() + "','" + film.getRegisseur() + "','" + film.getAuthor() + "','" + film.getCast() + "')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();

    }

    public static void addEinladung(Einladung ein) throws SQLException{
        String query = "INSERT INTO Einladung(User1, User2, FilmID, Date, Text) VALUES " +
                ""+"('"+ein.getUser1().getUserID()+"','"+ein.getUser2().getUserID()+"','"+ein.getFilm().getFilm_id()+"','"+ein.getDate()+"','"+ein.getText()+"')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();

    }

    public static void addEinladungResp(Einladung ein) throws SQLException{
        String query = "INSERT INTO Einladung(User1, User2, FilmID, Date, Text) VALUES " +
                ""+"('"+"1"+"','"+ ein.getUser2().getUserID() +"','"+"1"+"','"+ein.getDate()+"','"+ein.getText()+"')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();

    }

    public static void addReport(Report rep) throws SQLException{
        String query = "INSERT INTO Report(RepID, RepDate, FilmID, Gelesen, repText) VALUES " +
                ""+"('"+Integer.toString(rep.getid())+"','"+rep.getDate()+"','"+Integer.toString(rep.getFilm().getFilm_id())+"','"+Boolean.toString(rep.isGelesen())+"','"+rep.getRepText()+"')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();

    }

    public static void readReport(Report rep) throws SQLException{
        String query = "UPDATE Report SET Gelesen = '"+Boolean.toString(rep.isGelesen())+"' WHERE RepID = " + rep.getid();
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }

    public static void addMovie(Film film, int id) throws SQLException {
        String query = "INSERT INTO Film(Film_id, Filmname, FilmKategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner) VALUES " +
                "" + "('" + film.getFilm_id() + "','" + film.getName() + "','" + film.getCategory() + "','" + Integer.toString(film.getLength()) + "','" + film.getDate() + "','" + film.getRegisseur() + "','" + film.getAuthor() + "','" + film.getCast() + "','" + film.getBannerLink() + "')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();

    }

    public static void deleteMovie(Film film) throws SQLException {
        String query = "DELETE FROM Film WHERE Film_id = '" + film.getFilm_id() + "'";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }

    public static void deleteMovie(int film_id) throws SQLException {
        String query = "DELETE FROM Film WHERE Film_id = " + film_id + ";";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }


    public static void editMovie(Film film) throws SQLException {
        String query;
        if(film.getBannerLink()!=null){
            query = "UPDATE Film SET Filmname = '" + film.getName() + "', FilmKategorie = '" + film.getCategory() + "', Filmlaenge = '" + film.getLength() + "', Erscheinungsdatum = '" + film.getDate() + "', Regisseur = '" + film.getRegisseur() + "', Drehbuchautor = '" + film.getAuthor() + "', Cast = '" + film.getCast() + "', Filmbanner = '" + film.getBannerLink() + "' WHERE Film_id = " + film.getFilm_id();

        } else {
            query = "UPDATE Film SET Filmname = '" + film.getName() + "', FilmKategorie = '" + film.getCategory() + "', Filmlaenge = '" + film.getLength() + "', Erscheinungsdatum = '" + film.getDate() + "', Regisseur = '" + film.getRegisseur() + "', Drehbuchautor = '" + film.getAuthor() + "', Cast = '" + film.getCast() + "' WHERE Film_id = " + film.getFilm_id();
        }
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }

    public static boolean checkMovie(Film film) throws SQLException {
        String query2 = "SELECT * FROM Film WHERE " +
                "Filmname= '" + film.getName() +
                "' AND FilmKategorie = '" + film.getCategory() +
                "' AND Filmlaenge = " + film.getLength() +
                " AND Erscheinungsdatum = '" + film.getDate() +
                "' AND Regisseur = '" + film.getRegisseur() +
                "' AND DrehbuchAutor = '" + film.getAuthor() +
                "' AND Cast = '" + film.getCast() + "';";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while (RS2.next()) {
            if (film.getName().equals(RS2.getString("Filmname"))){
                getData2.close();
                return true;
            }
        }
        getData2.close();
        return false;
    }

    public static boolean checkMoviebyID(int film_id) throws SQLException{
        String query = "SELECT Film_id FROM Film WHERE Film_id = " + film_id + ";";
        Statement getdata = connection.createStatement();
        ResultSet RS = getdata.executeQuery(query);

        boolean condition = false;

        while(RS.next()){
            if (film_id == RS.getInt("Film_id")){
                getdata.close();
                return true;
            }
        }

        getdata.close();
        return false;
    }

    public static Film getMovie(int filmid) throws SQLException {
        Film ergebnis = new Film();

        String query = "SELECT * FROM Film WHERE Film_id = " + filmid + ";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        while (RS.next()) {
            ergebnis.setAuthor(RS.getString("Regisseur"));
            ergebnis.setFilm_id(RS.getInt("Film_id"));
            ergebnis.setBannerLink(RS.getString("Filmbanner"));
            ergebnis.setDate(RS.getString("Erscheinungsdatum"));
            ergebnis.setCast(RS.getString("Cast"));
            ergebnis.setCategory(RS.getString("FilmKategorie"));
            ergebnis.setLength(RS.getInt("Filmlaenge"));
            ergebnis.setName(RS.getString("Filmname"));
            ergebnis.setRegisseur(RS.getString("Regisseur"));
        }
        getData.close();

        return ergebnis;
    }

    // Gibt die Film_id zurück die mit dem Film übereinstimmen

    public static int getFilm_id(Film film) throws SQLException{
        int filmid = 0;
        if (checkMovie(film)){
            String query = "SELECT Film_id FROM Film WHERE Filmname= '" + film.getName() + "' AND Erscheinungsdatum = '" + film.getDate() + "' AND Regisseur='" + film.getRegisseur() + "';";

            Statement getData = connection.createStatement();
            ResultSet RS = getData.executeQuery(query);

            while(RS.next()){
                filmid = RS.getInt("Film_id");
                return filmid;
            }
        }
        return filmid;
    }

    // Speichert die BannerUrl des Films in die Datenbank

    public static void setBannerForMovie(String bannerURL, int film_id) throws SQLException{
        String query = "UPDATE Film SET Filmbanner = '" + bannerURL + "' WHERE film_id = " + film_id + ";";
        Statement update = connection.createStatement();
        update.execute(query);
    }

    // Gibt eine Film[] als bestehenden aus allen Tupeln die in der Filmtabelle sind

    public static Film[] getMovies() throws SQLException {
        ArrayList<Film> tempFilmList = new ArrayList<>();

        // Datenbankabfrage
        // Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner)
        String query = "SELECT * FROM Film";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Film tempFilm = new Film(RS.getInt("Film_id"), RS.getString("Filmname"),
                    RS.getString("FilmKategorie"), RS.getInt("Filmlaenge"),
                    RS.getString("Erscheinungsdatum"), RS.getString("Regisseur"),
                    RS.getString("Drehbuchautor"), RS.getString("Cast"), RS.getString("Filmbanner"));
            tempFilmList.add(tempFilm);
        }
        Film[] newfilmList = new Film[tempFilmList.size()];
        newfilmList = tempFilmList.toArray(newfilmList);
        return newfilmList;


        // Überprüfe ob es Einträge in der Filmtabelle gibt, wenn nein dann wird eine leere Tabelle zurückgegeben & übertrage in eine Array anstatt Arraylist

    }

    public static Systemadministrator[] getAdmins() throws SQLException {
        ArrayList<Systemadministrator> tempAdminList = new ArrayList<>();

        // Datenbankabfrage
        String query = "SELECT * FROM systemadministrator";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Admin Objekten
        while (RS.next()) {
            Systemadministrator admin = new Systemadministrator();
            admin.setAdminID(RS.getInt("Admin_id"));
            admin.setVorname(RS.getString("Vorname"));
            admin.setNachname(RS.getString("Nachname"));
            admin.setSalt(RS.getString("Salt"));
            admin.setEmailAdresse(RS.getString("EmailAdresse"));
            admin.setPasswort(RS.getString("Passwort"));
            tempAdminList.add(admin);
        }
        Systemadministrator[] newadminlist = new Systemadministrator[tempAdminList.size()];
        newadminlist = tempAdminList.toArray(newadminlist);
        return newadminlist;

    }

    public static void addMovieToSL(Nutzer nid, Film filmid, Bewertung bert) throws SQLException {
        String query = "INSERT INTO SeenList(Nutzer_id, Film_id) VALUES " +
                "" + "('" + nid.getUserID() + "','" + filmid.getFilm_id() + "', '" + bert.getBewertungID() + "')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();
    }

    public static Film[] getMoviesFilter(String name, String kategorie, String cast, String von, String bis) throws SQLException, IOException {
        ArrayList<Film> tempFilmList = new ArrayList<>();


        String query = "SELECT * FROM Film WHERE Filmname LIKE '%" + name + "%' "  + kategorie +  "AND Cast LIKE '%" + cast + "%'";


        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Film tempFilm = new Film(RS.getInt("Film_id"), RS.getString("Filmname"),
                    RS.getString("FilmKategorie"), RS.getInt("Filmlaenge"),
                    RS.getString("Erscheinungsdatum"), RS.getString("Regisseur"),
                    RS.getString("Drehbuchautor"), RS.getString("Cast"));


            tempFilmList.add(tempFilm);
        }

        if(Objects.equals(von, "")){
            von = "1950-01-01";
        }
        if(Objects.equals(bis, "")){
            LocalDate now = LocalDate.now();
            bis = now.toString();
        }


             String [] dvon = von.split("-");

             int yvon = Integer.parseInt(dvon[0]);
             int mvon = Integer.parseInt(dvon[1]);
             int tvon = Integer.parseInt(dvon[2]);


             String[] dbis = bis.split("-");
             int ybis = Integer.parseInt(dbis[0]);
             int mbis = Integer.parseInt(dbis[1]);
             int tbis = Integer.parseInt(dbis[2]);

             LocalDate datevon = LocalDate.of(yvon, mvon, tvon);
             LocalDate datebis = LocalDate.of(ybis, mbis, tbis);


            ArrayList<Film> temp = new ArrayList<Film>();
        for (int x = 0; x < tempFilmList.size(); x++) {
            Film f = tempFilmList.get(x);


                String[] fvon = f.getDate().split("-");

                int yf = Integer.parseInt(fvon[0]);
                int mf = Integer.parseInt(fvon[1]);
                int df = Integer.parseInt(fvon[2]);
                LocalDate datef = LocalDate.of(yf, mf, df);

                if(!datef.isBefore(datevon) && ! datef.isAfter(datebis)){
                    temp.add(f);

                }
        }

        RS.close();
        Film[] newfilmList = new Film[temp.size()];
        newfilmList = temp.toArray(newfilmList);
        return newfilmList;


    }

    public static Film[] getMoviesSl(String id) throws SQLException {
        ArrayList<Film> tempFilmList = new ArrayList<>();

        // Datenbankabfrage
        // Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner)
        String query = "SELECT * FROM Film NATURAL JOIN SeenList NATURAL JOIN Nutzer NATURAL LEFT JOIN Bewertung WHERE SeenList.Nutzer_id = '" + id + "'";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Film tempFilm = new Film(RS.getInt("Film_id"), RS.getString("Filmname"),
                    RS.getString("FilmKategorie"), RS.getInt("Filmlaenge"),
                    RS.getString("Erscheinungsdatum"), RS.getString("Regisseur"),
                    RS.getString("Drehbuchautor"), RS.getString("Cast"), RS.getString("Filmbanner"));
            tempFilmList.add(tempFilm);
        }
        Film[] newfilmList = new Film[tempFilmList.size()];
        newfilmList = tempFilmList.toArray(newfilmList);
        return newfilmList;
        // Überprüfe ob es Einträge in der Filmtabelle gibt, wenn nein dann wird eine leere Tabelle zurückgegeben & übertrage in eine Array anstatt Arraylist

    }


    public static void addWatchlist(Watchlist list) throws SQLException {
        String query = "INSERT INTO Watchlist(Watchlist_id, Film_id, Nutzer_id) VALUES " +
                "" + "('" + list.getWatchlistID() + "','" + list.getFilmID() + "','" + list.getUserID() + "')";

        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();

    }

    public static boolean checkWatchlist(Watchlist list) throws SQLException {
        String query2 = "SELECT * FROM Watchlist WHERE Film_id= '" + list.getFilmID() + "' AND Nutzer_id = '" + list.getUserID() + "'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while (RS2.next()) {
            if (list.getFilmID() == RS2.getInt("Film_id") && list.getUserID() == RS2.getInt("Nutzer_id")) {
                getData2.close();
                return true;
            }
        }
        getData2.close();
        return false;
    }

    public static boolean inWatchlist(int id, int nid) throws SQLException {
        String query2 = "SELECT * FROM Watchlist WHERE Film_id= '" + id + "' AND Nutzer_id = '" + nid + "'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        if (RS2.next()) {
            System.out.println("ist in DB");
            getData2.close();
            return true;

        }else {
            System.out.println("nicht in DB");
            getData2.close();
            return false;

        }

    }


    public static Bewertung getBewertung(int nutzer,int film) throws SQLException {
        String query = "SELECT * FROM Bewertung NATURAL JOIN SeenList WHERE SeenList.Nutzer_id = '" + nutzer + "' AND SeenList.Film_id='" + film + "'";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        Bewertung bert = new Bewertung();
        while (RS.next()) {
            bert.setBewertungID(RS.getInt("bewertung_id"));
            bert.setPunkte(RS.getInt("punkte"));
            bert.setBewertung(RS.getString("bewertung"));
        }
        getData.close();
        return bert;
    }

    public static void addSeenList(int uid, int fid) throws SQLException {
        String query = "INSERT INTO SeenList(Nutzer_id, film_id, Date) VALUES " +
                "(" +uid+ ", "
                + fid + " ," +
                "CURRENT_TIMESTAMP)";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }

    public static void deleteBewertung(Bewertung bewertung) throws SQLException {
        String query = "Delete FROM Bewertung WHERE bewertung_id ='" + bewertung.getBewertungID() + "'";
        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();
    }

    public static void updateBewertung(Bewertung bewertung) throws SQLException {
        String query = "UPDATE Bewertung SET punkte = '" + bewertung.getPunkte() + "', bewertung = '" + bewertung.getBewertung() + "' WHERE bewertung_id = '" + bewertung.getBewertungID() + "'";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }

    public static boolean checkSeenlist(Watchlist seenList) throws SQLException {
        String query2 = "SELECT * FROM SeenList WHERE Film_id= '" + seenList.getFilmID() + "' AND Nutzer_id = '" + seenList.getUserID() + "'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while (RS2.next()) {
            if (seenList.getFilmID() == RS2.getInt("Film_id") && seenList.getUserID() == RS2.getInt("Nutzer_id")) {
                getData2.close();
                return true;
            }
        }
        getData2.close();
        return false;
    }

    public static void deleteWatchList(Watchlist watchlist) {

        try {
            String query2 = "DELETE FROM WatchList WHERE watchlist_id = '" + watchlist.getWatchlistID() + "'";
            Statement insert2 = connection.createStatement();
            insert2.execute(query2);
            insert2.close();
        }catch ( Exception e){

            e.printStackTrace();
        }



    }

    public static Film[] getMoviesWl(String id) throws SQLException {
        ArrayList<Film> tempFilmList = new ArrayList<>();

        // Datenbankabfrage
        // Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner)
        String query = "SELECT DISTINCT * FROM Film NATURAL JOIN WatchList NATURAL JOIN Nutzer WHERE WatchList.Nutzer_id = '" + id + "'";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Film tempFilm = new Film(RS.getInt("Watchlist_id"),RS.getInt("Film_id"), RS.getString("Filmname"),
                    RS.getString("FilmKategorie"), RS.getInt("Filmlaenge"),
                    RS.getString("Erscheinungsdatum"), RS.getString("Regisseur"),
                    RS.getString("Drehbuchautor"), RS.getString("Cast"), RS.getBytes("Filmbanner"));
            tempFilmList.add(tempFilm);
        }
        Film[] newfilmList = new Film[tempFilmList.size()];
        newfilmList = tempFilmList.toArray(newfilmList);
        return newfilmList;
        // Überprüfe ob es Einträge in der Filmtabelle gibt, wenn nein dann wird eine leere Tabelle zurückgegeben & übertrage in eine Array anstatt Arraylist

    }

    public static Report[] getReports() throws SQLException{
        ArrayList<Report> tempReportList = new ArrayList<>();

        // Datenbankabfrage
        // Report (id, datum, Film, Text)
        String query = "SELECT * FROM Report";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        Film[] f = getMovies();
        Film ff = new Film();

        //Fülle die Liste mit Report Objekten

        while(RS.next()){
            int e = RS.getInt("FilmID");
            for(int i = 0; i<f.length; i++){
                if(f[i].getFilm_id()==e){
                    ff=f[i];
                    i=f.length;
                }
            }
            Report tempReport = new Report(RS.getInt("RepID"), RS.getString("RepDate"),
                    ff, RS.getString("RepText"), RS.getBoolean("Gelesen"));
            tempReportList.add(tempReport);
        }
        Report[] newreportList = new Report[tempReportList.size()];
        newreportList = tempReportList.toArray(newreportList);
        return newreportList;
    }

    public static Einladung[] getEinladungen(int nutzerid) throws SQLException{
        ArrayList<Einladung> tempEinList = new ArrayList<>();

        Nutzer curruser = Datenbank.getUserByID(nutzerid);

        // Datenbankabfrage
        // Einladung (id, user1, user2, Film, date, Text)
        String query = "SELECT * FROM Einladung";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        Film[] f = getMovies();
        Film ff = new Film();
        //Nutzer[] freunde = curruser.getFreundesListe().toArray(new Nutzer[0]);
        Nutzer[] freunde = Datenbank.getFreundeslisteForUbersicht(nutzerid);
        Nutzer nn = new Nutzer();

        //Fülle die Liste mit Einladung Objekten

        while(RS.next()){
            //prüfe ob zieluser current user ist
            if(curruser.getUserID()==RS.getInt("User2")) {
                int e = RS.getInt("FilmID");
                for (int i = 0; i < f.length; i++) {
                    if (f[i].getFilm_id() == e) {
                        ff = f[i];
                        i = f.length;
                    }
                }
                //////////////////
                e = RS.getInt("User1");
                for (int l = 0; l < freunde.length; l++) {
                    System.out.println(freunde[l].getVorname());
                    if (freunde[l].getUserID() == e) {
                        nn = freunde[l];
                        l = freunde.length;
                    }
                }
                Einladung tempEin = new Einladung(RS.getInt("EinladungID"), nn,
                        curruser, ff, RS.getString("Date"), RS.getString("Text"));
                tempEinList.add(tempEin);
            }
        }
        Einladung[] neweinList = new Einladung[tempEinList.size()];
        neweinList = tempEinList.toArray(neweinList);
        return neweinList;
    }

    public static void deleteEinladung(int ein_id) throws SQLException {
        String query = "DELETE FROM Einladung WHERE EinladungID = " + ein_id + ";";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
    }

    public static int getWl(int uid, int fid) throws SQLException {
        String query = "SELECT * FROM Watchlist WHERE Film_id= '" + fid + "' AND Nutzer_id = '" + uid + "'";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);
        int wlid = 0;
        while (RS.next()) {
            wlid = RS.getInt("Watchlist_id");
        }
        getData.close();
        return wlid;
    }

    private static void createSeenlistTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS SeenList (sl_id INT NOT NULL AUTO_INCREMENT, Nutzer_id VARCHAR(255) NOT NULL, Film_id VARCHAR(255), Date DATETIME NOT NULL, PRIMARY KEY (sl_id))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table SeenList is ready!");
    }

    private static void createBewertunglistTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Bewertung (Bewertung_id INT NOT NULL AUTO_INCREMENT, Film_id INT NOT NULL, Nutzer_id INT NOT NULL, Punkte INT NOT NULL, Bewertung VARCHAR(255), PRIMARY KEY (Bewertung_id))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Bewertung is ready!");
    }

    private static void createWatchlistTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS Watchlist (Watchlist_id INT NOT NULL AUTO_INCREMENT, Film_id INT NOT NULL, Nutzer_id INT NOT NULL, PRIMARY KEY (`Watchlist_id`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table Watchlist is ready!");
    }

    public static Bewertung[] getBewertung2(int fid) throws SQLException {
        ArrayList<Bewertung> tempBewertungList = new ArrayList<>();


        String query = "SELECT punkte, Bewertung FROM bewertung WHERE film_id = '" + fid + "' AND punkte > '"+0+ "'";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Bewertung tempFilm = new Bewertung(RS.getInt("Punkte"),
                    RS.getString("Bewertung"));
            tempBewertungList.add(tempFilm);
        }
        Bewertung[] newBewertungList = new Bewertung[tempBewertungList.size()];
        newBewertungList = tempBewertungList.toArray(newBewertungList);
        return newBewertungList;


        // Überprüfe ob es Einträge in der Filmtabelle gibt, wenn nein dann wird eine leere Tabelle zurückgegeben & übertrage in eine Array anstatt Arraylist

    }

    public static boolean checkBewertung(int bewertung) throws SQLException {
        String query2 = "SELECT * FROM Bewertung WHERE Film_id= '" + bewertung+ "' AND Punkte>'0'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while (RS2.next()) {
            if (bewertung==RS2.getInt("Film_id")) {
                getData2.close();
                return true;
            }
        }
        getData2.close();
        return false;
    }

    public static boolean checkSeenlist(int id, int nid) throws SQLException {
        String query2 = "SELECT * FROM seenlist WHERE Film_id= '" + id + "' AND Nutzer_id = '" + nid + "'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        if (RS2.isBeforeFirst()) {
            System.out.println("ist in DB");
            getData2.close();
            return true;

        }else {
            System.out.println("nicht in DB");
            getData2.close();
            return false;

        }

    }

    public static void deleteSeenList(int slid) throws SQLException {
        String query1 = "SET SQL_SAFE_UPDATES ='"+ 0+"'";
        //String query2 = "Delete FROM Seenlist WHERE nutzer_id = '" + nid + "'AND film_id ='" + fid +"'";
        String query2 = "DELETE FROM Seenlist WHERE sl_id = '" + slid + "'";
        String query3 = "SET SQL_SAFE_UPDATES ='"+ 1+"'";

        /*Statement insert1 = connection.createStatement();
        insert1.execute(query1);
        insert1.close();*/

        Statement insert2 = connection.createStatement();
        insert2.execute(query2);
        insert2.close();

        /*Statement insert3 = connection.createStatement();
        insert3.execute(query3);
        insert3.close();*/
    }

    public static int getSeenlistID(int uid,int fid) throws SQLException {
        int result = 0;
        String query = "SELECT * FROM Seenlist WHERE Nutzer_id = '" + uid + "' AND Film_id='" + fid + "'";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()) {
           result=RS.getInt("sl_id");
        }
        getData.close();
        return result;
    }

    public static boolean checkBewertungUser(int uid, int fid) throws SQLException {
        String query2 = "SELECT * FROM Bewertung WHERE Film_id= '" +fid+ "' AND Nutzer_id='" +uid+"'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while (RS2.next()) {
            if (fid==RS2.getInt("Film_id")) {
                getData2.close();
                return true;
            }
        }
        getData2.close();
        return false;
    }

    public static boolean checkBewertungUpdate(int punkte, String bewertung) throws SQLException {
        String query2 = "SELECT * FROM Bewertung WHERE Punkte= '" +punkte+ "' AND Bewertung='" +bewertung+"'";

        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while (RS2.next()) {
            if (punkte==RS2.getInt("Punkte")&& bewertung.equals(RS2.getString("Bewertung"))) {
                getData2.close();
                return true;
            }
        }
        getData2.close();
        return false;
    }

    private static void createStatsAdminTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS StatistikAdmin ( Film_id INT NOT NULL, Anzahl_Bewertung INT,Anzahl_gesehen INT NOT NULL, gesamtPunkte INT, PRIMARY KEY (`Film_id`))";
        Statement insert = connection.createStatement();
        insert.execute(query);
        insert.close();
        System.out.println("Table StatsAdmin is ready!");
    }

    public static void addFullStatsAdmin(StatistikAdmin statistikAdmin) throws SQLException {
        StatistikAdmin oldData = null;
        int fid = statistikAdmin.getFilmid();
        if(getStats(fid)!=null) {
            oldData = getStats(statistikAdmin.getFilmid());
        }

        String query = "INSERT INTO StatistikAdmin(film_ID, anzahl_bewertung, anzahl_gesehen, gesamtPunkte) VALUES " +
                "" + "('" + statistikAdmin.getFilmid()+ "','" + statistikAdmin.getAnzahlBewertung() + "','" + statistikAdmin.getAnzahlGesehen() + "','" + statistikAdmin.getGesamtPunkte()+ "')";
        // String query2= "UPDATE StatistikAdmin SET Anzahl_gesehen = '" + neuerWert + "' WHERE film_id = '" + statistikAdmin.getFilmid() + "'";

        Statement insert = connection.createStatement();
        if(oldData!=null)
        {
            int alterWert = oldData.getAnzahlGesehen();
            int neuerWert = alterWert+statistikAdmin.getAnzahlGesehen();
            int altBert = oldData.getAnzahlBewertung();
            int neuBert = altBert + statistikAdmin.getAnzahlBewertung();
            int altPunkt = oldData.getGesamtPunkte();
            int neuPunkt = altPunkt + statistikAdmin.getGesamtPunkte();

            String query2= "UPDATE StatistikAdmin SET Anzahl_gesehen = '" + neuerWert + "', Anzahl_Bewertung = '"+neuBert + "', gesamtPunkte = '"+neuPunkt + "' WHERE film_id = '" + statistikAdmin.getFilmid() + "'";
            insert.execute(query2);
        }
        else{

            insert.execute(query);
        }
        insert.close();

    }

    public static void addGesehenAdmin(StatistikAdmin statistikAdmin) throws SQLException {
        StatistikAdmin oldData = null;
        int fid = statistikAdmin.getFilmid();
        if(getStats(fid)!=null) {
                oldData = getStats(statistikAdmin.getFilmid());
        }

        String query = "INSERT INTO StatistikAdmin(film_ID, anzahl_bewertung, anzahl_gesehen, gesamtPunkte) VALUES " +
                "" + "('" + statistikAdmin.getFilmid()+ "','" + statistikAdmin.getAnzahlBewertung() + "','" + statistikAdmin.getAnzahlGesehen() + "','" + statistikAdmin.getGesamtPunkte()+ "')";
       // String query2= "UPDATE StatistikAdmin SET Anzahl_gesehen = '" + neuerWert + "' WHERE film_id = '" + statistikAdmin.getFilmid() + "'";

        Statement insert = connection.createStatement();
        if(oldData!=null)
        {
            int alterWert = oldData.getAnzahlGesehen();
            int neuerWert = alterWert+statistikAdmin.getAnzahlGesehen();
            String query2= "UPDATE StatistikAdmin SET Anzahl_gesehen = '" + neuerWert + "' WHERE film_id = '" + statistikAdmin.getFilmid() + "'";
            insert.execute(query2);
        }
        else{

            insert.execute(query);
        }
        insert.close();

    }

    public static StatistikAdmin getStats(int fid) throws SQLException {
        String query = "Select * FROM StatistikAdmin WHERE Film_id ='" + fid + "'";
        StatistikAdmin statistikAdmin = null;
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()) {
           statistikAdmin = new StatistikAdmin(RS.getInt("Anzahl_Bewertung"),RS.getInt("Anzahl_gesehen"),RS.getInt("gesamtPunkte"));
        }
        getData.close();

        return statistikAdmin;
    }


    public static void deleteStatsBewertung(int fid) throws SQLException {
        String query = "Delete FROM StatistikAdmin WHERE Film_id ='" + fid + "'";
        Statement insert = connection.createStatement();
        insert.execute(query);

        insert.close();
    }

    // Gibt TOP 5 der meist geschauten Kategorien aus (in absteigender Reihenfolge) mit Count

    public static FavouriteStats[] getNutzerStatsKategorie(int nutzer_id, Date datumVon, Date datumBis) throws SQLException {
        String query = "SELECT FilmKategorie FROM seenlist NATURAL JOIN Film WHERE Nutzer_id = " + nutzer_id +
                " AND Date BETWEEN CAST('" + datumVon + "' AS Date)" +
                " and CAST('" + datumBis + "' AS Date);";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        ArrayList<String> kategorie = new ArrayList<>();
        ArrayList<FavouriteStats> kategorieUndAnzahl = new ArrayList<>();

        while(RS.next()){
            String[] list = RS.getString("FilmKategorie").replaceAll(", ", ",").split(",");

            for (String item : list) {
                if (kategorie.contains(item)) {
                    FavouriteStats tempKategorie = kategorieUndAnzahl.get(kategorie.indexOf(item));
                    tempKategorie.addValue(1);
                    kategorieUndAnzahl.set(kategorie.indexOf(item), tempKategorie);
                } else {
                    kategorie.add(item);
                    kategorieUndAnzahl.add(new FavouriteStats(item, 1));
                }
            }
        }

        // Sortiert absteigend die Liste

        Collections.sort(kategorieUndAnzahl, new Comparator<FavouriteStats>() {
            @Override
            public int compare(FavouriteStats o1, FavouriteStats o2) {
                return o2.getValue() - o2.getValue();
            }
        });

        FavouriteStats[] ergebnis;

        if (kategorieUndAnzahl.size() < 5){
            ergebnis = new FavouriteStats[kategorieUndAnzahl.size()];
        }
        else {
            ergebnis = new FavouriteStats[5];
        }

        for (int i=0; i < ergebnis.length; i++){
            ergebnis[i] = kategorieUndAnzahl.get(i);
        }

        return ergebnis;
    }

    // Gibt die TOP 5 meist geschauten Schauspieler aus mit Count

    public static FavouriteStats[] getNutzerStatsSchauspieler(int nutzer_id, Date datumVon, Date datumBis) throws SQLException {
        String query = "SELECT Cast FROM seenlist NATURAL JOIN Film WHERE Nutzer_id = " + nutzer_id +
                " AND Date BETWEEN CAST('" + datumVon + "' AS Date)" +
                " and CAST('" + datumBis + "' AS Date);";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        ArrayList<String> schauspieler = new ArrayList<>();
        ArrayList<FavouriteStats> schauspielerUndAnzahl = new ArrayList<>();

        while(RS.next()) {
            String[] list = RS.getString("Cast").replaceAll(", ", ",").split(",");

            for (String item : list) {
                if (schauspieler.contains(item)) {
                    FavouriteStats tempSchauspieler = schauspielerUndAnzahl.get(schauspieler.indexOf(item));
                    tempSchauspieler.addValue(1);
                    schauspielerUndAnzahl.set(schauspieler.indexOf(item), tempSchauspieler);
                } else {
                    schauspieler.add(item);
                    schauspielerUndAnzahl.add(new FavouriteStats(item, 1));
                }
            }
        }

        Collections.sort(schauspielerUndAnzahl, new Comparator<FavouriteStats>() {
            @Override
            public int compare(FavouriteStats o1, FavouriteStats o2) {
                return o2.getValue() - o2.getValue();
            }
        });

        FavouriteStats[] ergebnis;

        if (schauspielerUndAnzahl.size() < 5){
            ergebnis = new FavouriteStats[schauspielerUndAnzahl.size()];
        }
        else {
            ergebnis = new FavouriteStats[5];
        }

        for (int i=0; i < ergebnis.length; i++){
            ergebnis[i] = schauspielerUndAnzahl.get(i);
        }

        return ergebnis;
    }

    // Gibt die FilmbannerURL aus, wo die TOP2 Schauspieler und TOP2 Kategorie mtunit dem Film übereinstimmen
    // Wenn kein solcher Film gefunden wurde dann Top1 Schauspieler und Top1 Kategorie
    // Ausgabe des Lieblingsfilms ist Random, wenn es mehrere gibt

    public static String getNutzerStatsFavouriteMovie(int nutzer_id, Date datumVon, Date datumBis, String[] schauspieler, String[] kategorie) throws SQLException {
        String query = "SELECT Filmbanner FROM seenlist NATURAL JOIN Film NATURAL JOIN Bewertung WHERE Nutzer_id = " + nutzer_id +
                " AND Date BETWEEN CAST('" + datumVon + "' AS Date)" +
                " AND CAST('" + datumBis + "' AS Date)" +
                " AND FilmKategorie like '%" + kategorie[0] + "%' AND Filmkategorie like '%" + kategorie[1] + "%'" +
                " AND Cast like '%" + schauspieler[0] + "%' AND Cast like '%" + schauspieler[1] + "%'" +
                " ORDER BY Punkte DESC LIMIT 1;";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        String result = "";

        while (RS.next()){
            result = RS.getString("Filmbanner");
        }
        getData.close();

        return result;
    }

    // Nachfolgender Algorithmus film_id mit top1 schauspieler und kategorie

    public static String getNutzerStatsFavouriteMovieAlternative(int nutzer_id, Date datumVon, Date datumBis, String schauspieler, String kategorie) throws SQLException{
        String query = "SELECT Filmbanner FROM seenlist NATURAL JOIN Film NATURAL JOIN Bewertung WHERE Nutzer_id = " + nutzer_id +
                " AND Date BETWEEN CAST('" + datumVon + "' AS Date)" +
                " AND CAST('" + datumBis + "' AS Date)" +
                " AND FilmKategorie like '%" + kategorie + "%'" +
                " AND Cast like '%" + schauspieler + "%'" +
                " ORDER BY Punkte DESC LIMIT 1;";


        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        String result = "";

        while (RS.next()){
            result = RS.getString("Filmbanner");
        }

        getData.close();

        return result;
    }

    // Soll einen intarray mit Laenge 2: [0] = Zeitraum Summe der geschauten Filmminuten [1] = Summe aller geschauten Filmminuten

    public static int[] getNutzerStatsForWatchedTime(int nutzer_id, Date datumVon, Date datumBis) throws SQLException {
        int[] ergebnis = new int[2];

        // AND DATE BETWEEN CAST('2022-03-22' AS Date) AND CAST('2025-06-01' AS Date)

        String query = "SELECT SUM(Filmlaenge) as Summe FROM seenlist NATURAL JOIN Film WHERE Nutzer_id = " + nutzer_id +
                " AND Date BETWEEN CAST('" + datumVon + "' AS Date)" +
                " and CAST('" + datumBis + "' AS Date);";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);


        while (RS.next()){
            ergebnis[0] = RS.getInt("Summe");
        }

        RS.close();

        String query2 = "SELECT SUM(Filmlaenge) as Summe FROM seenlist NATURAL JOIN Film WHERE Nutzer_id = " + nutzer_id +
                ";";
        Statement getData2 = connection.createStatement();
        ResultSet RS2 = getData2.executeQuery(query2);

        while(RS2.next()){
            ergebnis[1] = RS2.getInt("Summe");
        }

        RS2.close();

        return ergebnis;
    }

    public static Film[] getFilmvorschläge(String kategorie) throws SQLException, IOException {
        ArrayList<Film> tempFilmList = new ArrayList<>();


        String query = "SELECT * FROM Film WHERE Film_id NOT IN (SELECT Film_id FROM seenlist) AND Filmname LIKE '%' " + kategorie + " Limit 15";
        System.out.println(query);


        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Film tempFilm = new Film(RS.getInt("Film_id"), RS.getString("Filmname"),
                    RS.getString("FilmKategorie"), RS.getInt("Filmlaenge"),
                    RS.getString("Erscheinungsdatum"), RS.getString("Regisseur"),
                    RS.getString("Drehbuchautor"), RS.getString("Cast"));


            tempFilmList.add(tempFilm);
        }

        RS.close();
        Film[] newfilmList = new Film[tempFilmList.size()];
        newfilmList = tempFilmList.toArray(newfilmList);
        return newfilmList;
    }



    public static Film[] getLatestSL(String id) throws SQLException {
        ArrayList<Film> tempFilmList = new ArrayList<>();

        // Datenbankabfrage
        // Film (Film_id, Filmname, Kategorie, Filmlaenge, Erscheinungsdatum, Regisseur, Drehbuchautor, Cast, Filmbanner)
        String query = "SELECT * FROM Film NATURAL JOIN SeenList NATURAL JOIN Nutzer NATURAL LEFT JOIN Bewertung WHERE SeenList.Nutzer_id = '" + id + "' ORDER BY Date DESC LIMIT 10";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        //Fülle die Liste mit Film Objekten

        while (RS.next()) {
            Film tempFilm = new Film(RS.getInt("Film_id"), RS.getString("Filmname"),
                    RS.getString("FilmKategorie"), RS.getInt("Filmlaenge"),
                    RS.getString("Erscheinungsdatum"), RS.getString("Regisseur"),
                    RS.getString("Drehbuchautor"), RS.getString("Cast"), RS.getString("Filmbanner"));
            tempFilmList.add(tempFilm);
        }
        Film[] newfilmList = new Film[tempFilmList.size()];
        newfilmList = tempFilmList.toArray(newfilmList);
        return newfilmList;
        // Überprüfe ob es Einträge in der Filmtabelle gibt, wenn nein dann wird eine leere Tabelle zurückgegeben & übertrage in eine Array anstatt Arraylist

    }

    public static Integer[] getFreundeID(int nutzer_id) throws SQLException{
        ArrayList<Integer> freundesliste = new ArrayList<>();
        Integer [] ergebnis;

        String query = "SELECT freund_id FROM Freundesliste WHERE Nutzer_id = " + nutzer_id;

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        while (RS.next()){
            int tempNutzer = RS.getInt("freund_id");

            System.out.println(tempNutzer);
            freundesliste.add(tempNutzer);
        }

        getData.close();

        ergebnis = new Integer[freundesliste.size()];
        ergebnis = freundesliste.toArray(ergebnis);
        return ergebnis;
    }



    public static boolean inStatsAdmin(int fid) throws SQLException {
        String query = "Select * FROM StatistikAdmin WHERE Film_id ='" + fid + "'";

        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if (RS.next()) {
            System.out.println("ist in DB");
            getData.close();
            return true;

        }else {
            System.out.println("nicht in DB");
            getData.close();
            return false;

        }

    }

    public static void setOpen(Diskussionsgruppe gruppe) throws SQLException {


        String query = "SELECT * FROM diskussionen WHERE Gruppe_id="+gruppe.getId()+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if(RS.next()){

            String query1 = "UPDATE diskussionen SET Privat = 'false' WHERE Gruppe_id = '" + gruppe.getId() + "';";
            Statement update = connection.createStatement();
            update.execute(query1);
            update.close();

        }

    }

    public static void setPrivat(Diskussionsgruppe gruppe) throws SQLException {

        String query = "SELECT * FROM diskussionen WHERE Gruppe_id="+gruppe.getId()+";";
        Statement getData = connection.createStatement();
        ResultSet RS = getData.executeQuery(query);

        if(RS.next()){

            String query1 = "UPDATE diskussionen SET Privat = 'true' WHERE Gruppe_id = '" + gruppe.getId() + "';";
            Statement update = connection.createStatement();
            update.execute(query1);
            update.close();

        }
    }

    public static boolean cleanUpTest() {
        try {

            int id1 = 0,id2 = 0;
            String queryID1 = "SELECT * FROM diskussionen WHERE Gruppe_name = 'Test Diskussion';";
            Statement get = connection.createStatement();
            ResultSet RS = get.executeQuery(queryID1);
            if(RS.next())
            id1= RS.getInt("Gruppe_id");
            get.close();

            String queryID2 = "SELECT * FROM diskussionen WHERE Gruppe_name = 'Zweiter Test';";
            Statement get2 = connection.createStatement();
            ResultSet RS2 = get2.executeQuery(queryID1);
            if(RS2.next())
            id2= RS2.getInt("Gruppe_id");
            get2.close();



            String query = "DELETE FROM diskussionen WHERE Gruppe_name = 'Test Diskussion';";
            Statement delete = connection.createStatement();
            delete.execute(query);
            delete.close();

            String query2 = "DELETE FROM diskussionen WHERE Gruppe_name = 'Zweiter Test';";
            Statement delete2 = connection.createStatement();
            delete2.execute(query2);
            delete2.close();

            String query3 = "DELETE FROM nutzerdiskussionen WHERE Gruppe_id = "+(id2-1)+";";
            Statement delete3 = connection.createStatement();
            delete3.execute(query3);
            delete3.close();

            String query4 = "DELETE FROM nutzerdiskussionen WHERE Gruppe_id = "+id2+";";
            Statement delete4 = connection.createStatement();
            delete4.execute(query4);
            delete4.close();

            String query5 = "DELETE FROM nutzerdiskussionen WHERE Gruppe_id = 0;";
            Statement delete5 = connection.createStatement();
            delete5.execute(query5);
            delete5.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }
}
