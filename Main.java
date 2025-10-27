import java.util.Scanner; //ignore this — unimportant for now

public class Main{ //what's the name of my .java file?
        public static void main(String[] args){ //what's special about the main function?
                Scanner scanner = new Scanner(System.in); // just a way to catch user input - unimportant for now

                int number = 0; //what does this mean?
                boolean state = true; //what does THIS mean?

                while(state){  //while loop — keeps repeating the code below until state is false
                        System.out.println("Enter an integer: "); //what will this print?

                        String response = scanner.nextLine(); //reading output — unimportant for now
                        try{ //try... catch... finally block
                                number = Integer.parseInt(response); //looking for int within a string — unimportant for now-

                                break;
                        } catch(java.lang.NumberFormatException e1){ //error handling if possible
                                System.out.println(response + " is not an integer.");
                        } finally{ //always executes, no matter if an error was thrown or not
                                System.out.println("This is a finally statement");
                        }
                }

                if(number == 0){ //if... else block
                        System.out.println("number is equal to 0");
                } else if(number > 0){
                        System.out.println("number is greater than 0");
                } else{ //default case —  if none of the other are true — how does this differ from a finally{} block?
                        System.out.println("number is smaller than 0");
                }
        }
}
