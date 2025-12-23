/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package life.cal;

/**
 *
 * @author User
 */
public class LifeCal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    try { Db.migrate(); } catch (Exception e) { e.printStackTrace(); }
    new HomeLogin().setVisible(true);
    }
    
}
