import java.util.*;

public class VerificacaoAnagrama {

    // Verifica se as duas metades da linha (antes e depois de " - ") são anagramas
    static boolean checarPermutacao(String texto) {
        boolean resultado = true;
        String parteDireita = "";
        int tamanho = texto.length();
        int posHifen = 0;

        // Encontra a posição do '-' e encerra o laço
        for (int k = 0; k < tamanho; k++) {
            if (texto.charAt(k) == '-') {
                posHifen = k;
                k = tamanho; // força saída
            }
        }

        // Copia a parte após o " - " (assume espaço após o hífen)
        for (int k = posHifen + 2; k < tamanho; k++) {
            char ch = texto.charAt(k);
            parteDireita += ch;
        }

        // Copia a parte antes do " - " (assume espaço antes do hífen)
        String parteEsquerda = "";
        for (int k = 0; k < posHifen - 1; k++) {
            parteEsquerda += texto.charAt(k);
        }

        // Normaliza para minúsculas
        parteEsquerda = parteEsquerda.toLowerCase();
        parteDireita = parteDireita.toLowerCase();

        tamanho = parteEsquerda.length();
        int contador = 0;

        // Tamanhos diferentes já invalida anagrama
        if (tamanho != parteDireita.length()) {
            resultado = false;
        }

        // Para cada caractere da esquerda, busca um igual na direita
        if (resultado == true) {
            for (int a = 0; a < tamanho; a++) {
                for (int b = 0; b < tamanho; b++) {
                    if (parteEsquerda.charAt(a) == parteDireita.charAt(b)) {
                        contador++;
                        b = tamanho; // encontrou correspondente; sai do laço interno
                    }
                }
                // Se não achou correspondente, não é anagrama
                if (contador < 1) {
                    resultado = false;
                }
                contador = 0; // reinicia para o próximo caractere
            }
        }

        return resultado;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String texto = sc.nextLine();

        // Lê até a sentinela "FIM"
        while (!(texto.length() == 3 && texto.charAt(0) == 'F' && texto.charAt(1) == 'I' && texto.charAt(2) == 'M')) {
            if (checarPermutacao(texto)) {
                System.out.println("SIM");
            } else {
                System.out.println("N" + (char) ('a' + 'b') + "O");
            }
            texto = sc.nextLine();
        }

        sc.close();
    }
}
