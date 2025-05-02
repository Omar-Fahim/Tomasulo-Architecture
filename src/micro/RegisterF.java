package micro;

public class RegisterF {
   String name;
   String q;
   double value;
   


   public RegisterF(String name){
    this.name = name;
    this.q ="0";
    this.value = 0;

   }


   @Override
    public String toString() {
        return String.format("%-15s%-5s%-10s", name, q, value);
    }

}
