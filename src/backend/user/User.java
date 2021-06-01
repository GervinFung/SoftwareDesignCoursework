package backend.user;

public final class User {

    private enum USER_TYPE {ADMIN, CLIENT}

    private final int userID;
    private final String name, password, phoneNum;
    private final USER_TYPE user_type;

    public User(final int userID, final String name, final String phoneNum, final char c, final String password) {
        this.userID = userID;
        this.name = name;
        this.phoneNum = phoneNum;
        this.user_type = c == 'A' ? USER_TYPE.ADMIN : USER_TYPE.CLIENT;
        this.password = password;
    }

    public String getPhoneNum() { return this.phoneNum; }
    public int getUserID() { return this.userID; }
    public String getPassword() { return this.password; }
    public boolean isAdmin() { return this.user_type == USER_TYPE.ADMIN; }

    @Override
    public String toString() { return this.userID + "/" + this.name + "/" + this.phoneNum + "/" + this.user_type.toString().charAt(0) + "/" + this.password; }

    @Override
    public int hashCode() { return this.userID; }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }

        if (!(obj instanceof User)) { return false; }

        return ((User)obj).userID == this.userID;
    }
}