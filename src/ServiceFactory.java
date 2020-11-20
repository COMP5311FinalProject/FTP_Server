public class ServiceFactory {
    public static Service createService(String serviceType){
        switch (serviceType.toUpperCase()){
            case "USER":
                return new ValiService();
            case "PASS":
                return new LoginService();
            case "LIST":
                return new DirService();
            case "MYPORT":
                return new PortService();
            case "RETR":
                return new RetrService();
            case "STOR":
                return new StoreService();
            case "QUIT":
                return new QuitService();
            case "PASV":
                return new PasvService();
            default:
                return null;
        }

    }
}
