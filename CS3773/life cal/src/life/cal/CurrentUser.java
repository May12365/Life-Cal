/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life.cal;

/**
 *
 * @author kitti_yk
 */
public final class CurrentUser {
    private static User user;

    public static void set(User u) { user = u; }
    public static User get() { return user; }
    public static boolean isLoggedIn() { return user != null; }
    public static void logout() { user = null; }
    private CurrentUser() {}
}
