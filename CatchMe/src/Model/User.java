package Model;

import java.util.Objects;

public class User {
    private static int ID = 0;
    private String name;

    public User(final String name) {
        ID=ID+1;
        this.name = name;
    }

////    @Override
////    public boolean equals(Object o) {
////        if (this.hashCode() == o.hashCode()) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////        User user = (User) o;
////        return Objects.equals(name, user.name);
////    }
////
//    @Override
//    public int hashCode() {
//        return 13*ID;
//    }

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
