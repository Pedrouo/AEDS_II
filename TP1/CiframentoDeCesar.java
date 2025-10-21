public class CiframentoDeCesar {

    public static String Ciframento(String texto){
        String resp = "";

        // Cifra de César: desloca cada caractere 3 posições
        for(int i = 0;i < texto.length();i++){
            char aux = texto.charAt(i);      // caractere atual
            resp += (char) (aux + 3);        // aplica o deslocamento +3
        }
        return resp;
    }
    
    public static void main(String[] args) {
        String texto = MyIO.readLine();      // lê a primeira linha

        // loop até a linha ser exatamente "FIM"
        while(!(texto.length() == 3 && texto.charAt(0) == 'F' && texto.charAt(1) == 'I' && texto.charAt(2) == 'M')){
           MyIO.println(Ciframento(texto));  // imprime a linha cifrada
           texto = MyIO.readLine();          // lê a próxima linha
        }
    }
}
