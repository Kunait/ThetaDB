package sep.groupt.server;


public class ServerStarter {

    public static void main(String[] args)  {


        ServerClass serverController = new ServerClass();
        try {
            serverController.start();
            serverController.downRight();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
