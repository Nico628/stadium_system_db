//User Interface

import java.util.Scanner;

public class OptionSelector {
    // CTRL + F INDEX:
    // !!!: constructor
    // 111: guest
    // 222: Manager

    //constructor
    //displays initial selection choices.

    //use scanner to scan for user-inputs in terminal
    //input will hold the integer value users type in
    private Scanner scan = new Scanner(System.in);
    private int input;

    public OptionSelector(){

        System.out.println(
                                 "Welcome to StadiumSystem! \n" +
                                 "Are you a guest or a manager at Stadium? \n" +
                                 "Please type the appropriate number and press enter"
        );

        //if they don't input correct values, the option will repeat.

        boolean repeat = true;

        while(repeat) {
            System.out.println(
                            "1. Guest \n" +
                            "2. Manager \n" +
                            "3. Exit application"
            );


            input = scan.nextInt();
            switch (input){
                case 1:
                    repeat = false;
                    guest();
                    break;
                case 2:
                    repeat = false;
                    manager();
                    break;

                 //System.exit will(0) terminate the application.
                case 3:
                    repeat = false;
                    System.out.println("Thank you for using StadiumSystem!");
                    System.exit(0);
                    break;
                default:
                    invalidSelection();
            }

        }

    }


    // 111
    // =============================================GUEST========================================

    private void guest() {

        boolean repeat = true;

        while (repeat) {
            System.out.println(
                    "GUEST selected.\n" +
                            "What would you like to do today?\n" +
                            "1. Purchase event tickets\n" +
                            "2. Purchase food\n" +
                            "3. Purchase merchandise\n" +
                            "4. Select parking spot"
            );

            input = scan.nextInt();

            switch (input) {
                case 1:
                    repeat = false;
                    listEvents();
                    break;
                case 2:
                    repeat = false;
                    listFood();
                    break;
                case 3:
                    repeat = false;
                    listMerchandise();
                    break;
                case 4:
                    repeat = false;
                    selectParkingSpot();
                    break;
                default:
                    invalidSelection();

            }
        }
    }


    // iterate through events in database and list them for customers to see
    private void listEvents(){
        System.out.println(
                "Which event ticket would you like to purchase?"
        );
        //TODO: Add Events query

    }

    // iterate through food in database and list them for customers to see
    private void listFood(){
        System.out.println(
                "Which food item would you like to purchase?"
        );
        //TODO: Add Food query

    }

    // iterate through merchandise in database and list them for customers to see
    private void listMerchandise(){
        System.out.println(
                "Which event ticket would you like to purchase?"
        );
        //TODO: Add Merchandise query

    }

    // iterate through open parking spots in database and list them for customers to see
    private void selectParkingSpot(){
        System.out.println(
                "Which parking spot would you like to reserve?"
        );
        //TODO: Add ParkingSpot Query

    }

    // 222
    // =============================================MANAGER================================================

    private void manager() {

        boolean repeat = true;
        input = scan.nextInt();

        while (repeat) {
            System.out.println(
                    "Manager selected.\n" +
                            "What would you like to do today?\n" +
                            "1. Create new Event/Food/Merchandise\n" +
                            "2. Delete Event/Food/Merchandise\n" +
                            "3. Employee assignment\n" +
                            "4. Manage sponsorship\n" +
                            "5. Look bookkeeping records"
            );

            switch(input){
                case 1:
                    repeat = false;
                    createItem();
                    break;
                case 2:
                    repeat= false;
                    deleteItem();
                    break;
                case 3:
                    repeat = false;
                    employeeAssginment();
                    break;
                case 4:
                    repeat = false;
                    manageSponsorship();
                    break;
                case 5:
                    repeat= false;
                    lookBookKeeping();
                    break;
                default:
                    invalidSelection();
            }

        }
    }

    private void createItem(){
        boolean repeat = true;
        input = scan.nextInt();
        while(repeat){
            System.out.println(
                    "Create item selected.\n"+
                            "What type of item would you like to create?\n"+
                            "1. Event\n"+
                            "2. Food\n"+
                            "3. Merchandise\n"
            );

            switch(input){
                case 1:
                    repeat = false;
                    createEvent();
                    break;
                case 2:
                    repeat = false;
                    createFood();
                    break;
                case 3:
                    repeat = false;
                    createMerchandise();
                    break;
                default:
                    invalidSelection();
            }
        }
    }

    //add new Event to database
    private void createEvent(){
        System.out.println("New event has been created.");
    }

    //add new Food to database
    private void createFood(){
        System.out.println("New food has been created.");
    }

    //add new Merchandise to database
    private void createMerchandise(){
        System.out.println("New merchandise has been created");
    }

    private void deleteItem(){
        boolean repeat = true;
        input = scan.nextInt();
        while(repeat){
            System.out.println(
                    "Delete item selected.\n"+
                            "What type of item would you like to delete?\n"+
                            "1. Event\n"+
                            "2. Food\n"+
                            "3. Merchandise\n"
            );

            switch(input){
                case 1:
                    repeat = false;
                    deleteEvent();
                    break;
                case 2:
                    repeat = false;
                    deleteFood();
                    break;
                case 3:
                    repeat = false;
                    deleteMerchandise();
                    break;
                default:
                    invalidSelection();
            }
        }
    }

    //for deleting items, we should probably use list methods above from Guest
    //after listing, we can choose which item to delete.

    //delete Event from database
    private void deleteEvent(){
        //listEvents();
        System.out.println("Event has been deleted.");
    }

    //delete Food from database
    private void deleteFood(){
        //listFood();
        System.out.println("Food has been deleted");
    }

    //delete Merchandise from database
    private void deleteMerchandise(){
        //listMerchandise();
        System.out.println("New merchandise has been created");
    }

    // assigns which employee works which event
    // probably should use listEvent() from Guest and another helper function listEmployee() below
    private void employeeAssginment(){
        //listEvent();
        //listEmployee();
        System.out.println("THIS_EMPLOYEE will work on THIS_EVENT");
    }

    // list all employees for stadium
    private void listEmployee(){

    }

    // actually not sure what managing sponsorship is for... someone fill me in
    // but we probably should make another helper function listSponsors()
    private void manageSponsorship(){
        //listSponsors();
    }

    // lists sponsors of the Stadium
    private void listSponsors(){

    }

    // given date should return bookkeeping record for that day
    // OR we can just display the last 4 day's worth of bookkeeping like nico suggested lol
    // IF NEEDED make the helper function listBookKeeping() else delete the helper function
    private void lookBookKeeping(){
        //INPUT date -> bookkeeping
        //MAYBE listBookKeeping();
    }

    //return list of bookkeepings
    private void listBookKeeping(){

    }

    // =============================================OTHER=====================================
    //for switch statements.
    //when user inputs a selection that is not part of what's given
    private void invalidSelection(){
        System.out.println("Invalid Input. Please select from below options:");
    }

}
