import java.util.Scanner;

public class InversaoStringRec {

    public static String inversao(String str, int tam){
        if (tam <= 0) {
            return "";
        }
        return str.charAt(tam - 1) + inversao(str, tam - 1);
    }

public static void main(String[] args) {
    
    Scanner sc = new Scanner(System.in);

    String str = sc.nextLine();

    while(!(str.equals("FIM"))){

        System.out.println(inversao(str, str.length()));

        str = sc.nextLine();
    }
}

}
