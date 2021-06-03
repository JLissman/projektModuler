package db;


import db.entity.Pokemon;
import java.util.Scanner;

public class DataBaseAction {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        EntityHandler eh = new EntityHandler(Pokemon.class);
        System.out.println(eh.findById(1));



        /*
        System.out.println("1. Create ");

        System.out.println("2. Read");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        int choice = sc.nextInt();
        sc.nextLine();
        switch(choice){
            case 1:
                //new Pokemon(name, evolution);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                System.exit(0);

        }
 */
    }
}
