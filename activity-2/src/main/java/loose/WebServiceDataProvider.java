package loose;

public class WebServiceDataProvider implements UserDataProvider{
    @Override
    public String getUserDetails() {
        return "From Web Service: Fetching user details";
    }
}
