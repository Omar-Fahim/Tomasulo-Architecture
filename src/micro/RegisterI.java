package micro;

public class RegisterI {
   String name;
   String q;
   int value;
   


   public RegisterI(String name){
    this.name = name;
    this.q ="0";
    this.value = 0;

   }


   @Override
    public String toString() {
        return String.format("%-15s%-5s%-10s", name, q, value);
    }

}
