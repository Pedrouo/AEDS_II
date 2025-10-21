import java.util.Scanner;

public class Palindromo { 
    
    // Função: verifica se a string é palíndromo comparando extremos
    public static boolean ehPalindromo(String s)
{
  
    int tam = s.length(); 

    for(int i = 0; i < tam / 2;i++) // percorre até a metade
    {
        if (s.charAt(i) != s.charAt(tam - 1 - i)) // se extremos diferem, não é palíndromo
        return false;

    }
return true; // se não houve diferença, é palíndromo

}
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        String texto = sc.nextLine(); 

        // repete enquanto não for exatamente "FIM"
        while(!(texto.length() == 3 && texto.charAt(0) == 'F' && texto.charAt(1) == 'I' && texto.charAt(2) == 'M'))
        {
            // imprime resultado conforme verificação
            if (ehPalindromo(texto)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
            texto = sc.nextLine(); 
        } 
          sc.close(); 
        }
}
