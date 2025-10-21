import java.util.Scanner;

public class SomaDigitosRec {

    public static int Soma(int num) {
        if (num == 0) {
            return 0; // caso base
        }
        return (num % 10) + Soma(num / 10);
    }

    public static void main(String[] args) {
       Scanner sc = new Scanner(System.in); 
       
       String entrada = sc.nextLine();  // lê como String

        // enquanto não for "FIM"
        while (!entrada.equals("FIM")) {
            int n = Integer.parseInt(entrada); // converte String -> int
            System.out.println(Soma(n));        // imprime soma dos dígitos
            entrada = sc.nextLine();         // lê próxima entrada
        }
        sc.close();
    }
}