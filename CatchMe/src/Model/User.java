package Model;

public class User {
    private static int ID = 0;
    private String name;

    public User(final String name) {
        ID++;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean equalsIgnoreCase(final User user) {
        if (ID == user.getID()){
            return name.equalsIgnoreCase(user.getName());
        }
        return true;
    }
}
