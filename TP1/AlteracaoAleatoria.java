import java.util.Random;  
import java.util.Scanner;  

public class AlteracaoAleatoria {
    
    private static Random gerador = new Random(); 

    private static String alteraLinha(String s) {  
        int tam  = s.length();                   
        
        char[] palavraNova = new char[s.length()]; 

        // Sorteia duas letras minúsculas ('a'..'z'):
        // 'a' = letra a ser substituída, 'b' = nova letra
        char a = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
        char b = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
                  
        for (int i = 0; i < tam; i++) {           
            char aux = s.charAt(i);               

            if(aux == a){                         
                palavraNova[i] = b;              
            } else {
                palavraNova[i] = aux;             
            }
        }
  
        return new String(palavraNova);           // Converte o array de chars em String
    }

    public static void main(String[] args) {
        
        gerador.setSeed(4);                       

        Scanner sc = new Scanner(System.in);      
        String texto = sc.nextLine();             

        // Continua processando até ler exatamente "FIM"
        while(!(texto.length() == 3 && texto.charAt(0) == 'F' && texto.charAt(1) == 'I' && texto.charAt(2) == 'M')){
            System.out.println(alteraLinha(texto)); 
            texto = sc.nextLine();                  
        }

        sc.close();                           
    }
}
