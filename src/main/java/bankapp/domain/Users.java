package bankapp.domain;

import java.util.List;
import java.util.Scanner;

public class Users {
    Scanner sc = new Scanner(System.in);

    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected int attemptsFailed;
    protected boolean accountBlocked;

    // constructores
    public Users(int id, String name, String email, String password, int attemptsFailed, boolean accountBlocked) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.attemptsFailed = attemptsFailed;
        this.accountBlocked = accountBlocked;
    }

    public Users() {
    }

    public Users(String email) {
        this.email = email;
    }

    // get y set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAttemptsFailed() {
        return attemptsFailed;
    }

    public void setAttemptsFailed(int attemptsFailed) {
        this.attemptsFailed = attemptsFailed;
    }

    public boolean isAccountBlocked() {
        return accountBlocked;
    }

    public void setAccountBlocked(boolean accountBlocked) {
        this.accountBlocked = accountBlocked;
    }

    // metodos
    public <T> void createUser(T user) {
    }

    public Users createUser(Users user) {
        return user;
    }

    public Users updateUser(Users user) {
        return user;
    }

    public List<Users> getAllUsers() {
        return null;
    }

    public Users getUserById(int id) {
        return null;
    }

    public void deleteUser(int id) {
    }

    public boolean login(String password) {
        return false;
    }

    public void register(String name, String password) {
    }

    public void logout() {
    }

    public void changePassword(String newPassword) {
    }
}