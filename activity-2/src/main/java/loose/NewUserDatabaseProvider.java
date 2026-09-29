package loose;

public class NewUserDatabaseProvider implements UserDataProvider{

    @Override
    public String getUserDetails() {
        return "From new user database: Fetching user details";
    }
}
