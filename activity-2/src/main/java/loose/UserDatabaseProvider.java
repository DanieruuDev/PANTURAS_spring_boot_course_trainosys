package loose;

public class UserDatabaseProvider implements UserDataProvider{

    @Override
    public String getUserDetails() {
        return "From User database: fetching details";
    }
}
