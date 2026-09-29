package loose;


public class LooseCouplingSample {
    public static void main(String[] args) {

        UserDataProvider provider =
                new UserDatabaseProvider();
        UserManager userManager = new UserManager(provider);
        System.out.println(userManager.getUserInfo());


        UserDataProvider webProvider =
                new WebServiceDataProvider();
        UserManager userManagerWeb =
                new UserManager(webProvider);
        System.out.println(userManagerWeb.getUserInfo());


        UserDataProvider newProvider =
                new NewUserDatabaseProvider();
        UserManager userManagerNew =
                new UserManager(newProvider);
        System.out.println(userManagerNew.getUserInfo());
    }
}
