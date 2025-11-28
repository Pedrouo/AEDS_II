import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

class GamePesquisado {
    String name;

    GamePesquisado() {
        this.name = "";
    }

    GamePesquisado(String name) {
        this.name = name;
    }
}

public class PesquisaBinaria {
    public static Scanner sc = new Scanner(System.in);
    private static final String MATRICULA = "876881";
    private static int numComparacoes = 0;
    private static long tempoExecucao = 0;

    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();

        String linha;
        int tamanho = 0;
        String ids[] = new String[0];
        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            tamanho++;
            String aux[] = ids;
            ids = new String[tamanho];
            for (int i = 0; i < tamanho - 1; i++) {
                ids[i] = aux[i];
            }
            ids[tamanho - 1] = linha;
            aux = null;
            linha = sc.nextLine();
        }
        GamePesquisado gamesList[] = CriandoObjetos.objetos(tamanho, ids);

        if (gamesList != null) {
            ordenarLista(gamesList, 0, gamesList.length - 1);
        }

        linha = sc.nextLine();
        while (!linha.equals("FIM")) {
            boolean achou = false;
            int dir = gamesList.length - 1, esq = 0, meio = 0;
            while (esq <= dir) {
                meio = (esq + dir) / 2;
                numComparacoes++; 
                if (linha.equals(gamesList[meio].name)) {
                    achou = true;
                    esq = dir + 1;
                } else {
                    numComparacoes++; 
                    if (linha.compareTo(gamesList[meio].name) > 0) {
                        esq = meio + 1;
                    } else {
                        dir = meio - 1;
                    }
                }
            }
            if (achou)
                System.out.println(" SIM");
            else
                System.out.println(" NAO");
            linha = sc.nextLine();
        }

        sc.close();

        long fim = System.currentTimeMillis();
        tempoExecucao = fim - inicio;
        gerarArquivoLog();
    }


    private static void gerarArquivoLog() {
        try (FileWriter logWriter = new FileWriter(MATRICULA + "_binaria.txt")) {
            String logLine = MATRICULA + "\t" + tempoExecucao + "\t" + numComparacoes + "\n";
            logWriter.write(logLine);
        } catch (IOException e) {
            System.err.println("Erro ao escrever o arquivo de log: " + e.getMessage());
        }
    }

    static void ordenarLista(GamePesquisado gameList[], int esq, int dir) {
        int i = esq, j = dir;
        int meio = (dir + esq) / 2;
        String pivo = gameList[meio].name;
        while (i <= j) {

            while (gameList[i].name.compareTo(pivo) < 0) {
                i++;
            }
            while (gameList[j].name.compareTo(pivo) > 0) {
                j--;
            }
            if (i <= j) {
                trocar(gameList, i, j);
                i++;
                j--;
            }
        }
        if (esq < j)
            ordenarLista(gameList, esq, j);
        if (i < dir)
            ordenarLista(gameList, i, dir);
    }

    static void trocar(GamePesquisado gameList[], int i, int j) {
        GamePesquisado aux = gameList[i];
        gameList[i] = gameList[j];
        gameList[j] = aux;
    }
}

class CriandoObjetos {
    static int contador = 0;
    public static Scanner sc = new Scanner(System.in);

    static GamePesquisado[] objetos(int tamanho, String ids[]) {
        GamePesquisado gameList[] = new GamePesquisado[tamanho];
        InputStream is = null;
        try {
            java.io.File arquivo = new java.io.File("/tmp/games.csv");
            if (!arquivo.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado em /tmp!");
                return new GamePesquisado[0];
            }
            is = new FileInputStream(arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return new GamePesquisado[0];
        }

        Scanner scArquivo = new Scanner(is);
        if (scArquivo.hasNextLine())
            scArquivo.nextLine();
        int cont = 0;
        int idProcurado;
        String linha;
        while (scArquivo.hasNextLine() && cont < tamanho) {
            linha = scArquivo.nextLine();
            int id = obterIdDaLinha(linha);
            String name = obterNomeDaLinha(linha);
            if ((idProcurado = encontrarId(ids, id)) != -1) {
                GamePesquisado jogo = new GamePesquisado(name);
                gameList[cont] = jogo;
                cont++;

                ids[idProcurado] = null;
            }
            contador = 0;
        }
        scArquivo.close();
        return gameList;
    }

    static int encontrarId(String ids[], int id) {
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] != null) {
                try {
                    if (Integer.parseInt(ids[i]) == id) {
                        return i;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return -1;
    }

    static int obterIdDaLinha(String jogo) {
        int id = 0;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            id = id * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    static String obterNomeDaLinha(String jogo) {
        String name = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        if (contador < jogo.length() && jogo.charAt(contador) == '"') {
            contador++;
            while (contador < jogo.length() && jogo.charAt(contador) != '"') {
                name += jogo.charAt(contador);
                contador++;
            }
            contador++;
        } else {
            while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                name += jogo.charAt(contador);
                contador++;
            }
        }
        return name;
    }
}