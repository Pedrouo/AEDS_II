import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class NoArrayList {
    public static final int MAX_GAMES = 500;
    public static final int MAX_INNER_ARRAY = 50;
    public static final int MAX_IDS = 100;
}

class Game {
    int id;
    String name;
    String releaseDate;
    int estimatedOwners;
    float price;
    String[] supportedLanguages;
    int supportedLanguagesCount;
    int metacriticScore;
    float userScore;
    int achievements;
    String[] publishers;
    int publishersCount;
    String[] developers;
    int developersCount;
    String[] categories;
    int categoriesCount;
    String[] genres;
    int genresCount;
    String[] tags;
    int tagsCount;

    Game() {
        this.id = 0;
        this.name = "";
        this.releaseDate = "";
        this.estimatedOwners = 0;
        this.price = 0.0f;
        this.supportedLanguages = new String[NoArrayList.MAX_INNER_ARRAY];
        this.supportedLanguagesCount = 0;
        this.metacriticScore = -1;
        this.userScore = -1.0f;
        this.achievements = 0;
        this.publishers = new String[NoArrayList.MAX_INNER_ARRAY];
        this.publishersCount = 0;
        this.developers = new String[NoArrayList.MAX_INNER_ARRAY];
        this.developersCount = 0;
        this.categories = new String[NoArrayList.MAX_INNER_ARRAY];
        this.categoriesCount = 0;
        this.genres = new String[NoArrayList.MAX_INNER_ARRAY];
        this.genresCount = 0;
        this.tags = new String[NoArrayList.MAX_INNER_ARRAY];
        this.tagsCount = 0;
    }

    Game(int id, String name, String releaseDate, int estimatedOwners, float price,
            String[] supportedLanguages, int supportedLanguagesCount, int metacriticScore, float userScore,
            int achievements,
            String[] publishers, int publishersCount, String[] developers, int developersCount,
            String[] categories, int categoriesCount, String[] genres, int genresCount, String[] tags, int tagsCount) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.estimatedOwners = estimatedOwners;
        this.price = price;

        this.supportedLanguages = supportedLanguages;
        this.supportedLanguagesCount = supportedLanguagesCount;
        this.publishers = publishers;
        this.publishersCount = publishersCount;
        this.developers = developers;
        this.developersCount = developersCount;
        this.categories = categories;
        this.categoriesCount = categoriesCount;
        this.genres = genres;
        this.genresCount = genresCount;
        this.tags = tags;
        this.tagsCount = tagsCount;

        this.metacriticScore = metacriticScore;
        this.userScore = userScore;
        this.achievements = achievements;
    }
}

class HLogMetrics {
    public static long tempoExecucaoMs = 0;
    public static long comparacoes = 0;
    public static long movimentacoes = 0;
    public static final String MATRICULA = "885732";

    public static void criarArquivoLog() {
        try {
            FileWriter arq = new FileWriter(MATRICULA + "_heapsort.txt");
            PrintWriter gravarArq = new PrintWriter(arq);
            gravarArq.println(MATRICULA + "\t" + comparacoes + "\t" + movimentacoes + "\t" + tempoExecucaoMs);
            gravarArq.close();
        } catch (IOException e) {
            System.out.println("Erro ao criar log: " + e.getMessage());
        }
    }
}

public class HeapSort {
    public static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        String entrada = sc.nextLine();

        String[] ids = new String[NoArrayList.MAX_IDS];
        int idsTamanho = 0;

        while (!entrada.equals("FIM") && idsTamanho < ids.length) {
            if (idsTamanho < ids.length) {
                ids[idsTamanho++] = entrada;
            }
            entrada = sc.nextLine();
        }

        Game[] gamesList = JogosDigitados.preparar(ids, idsTamanho);
        int gamesListTamanho = JogosDigitados.getTamanhoLista();

        long tempoInicio = System.currentTimeMillis();
        gamesList = aplicarHeapSort(gamesList, gamesListTamanho);
        long tempoFim = System.currentTimeMillis();
        HLogMetrics.tempoExecucaoMs = tempoFim - tempoInicio;

        exibirJogos(gamesList, gamesListTamanho);
        HLogMetrics.criarArquivoLog();

        sc.close();
    }

    static Game[] aplicarHeapSort(Game[] gameList, int tam) {
        Game[] tmp = new Game[tam + 1];
        for (int i = 0; i < tam; i++) {
            tmp[i + 1] = gameList[i];
        }
        gameList = tmp;

        for (int i = tam / 2; i >= 1; i--) {
            reorganizar(i, tam, gameList);
        }

        int tamHeap = tam;
        while (tamHeap > 1) {
            trocarPosicoes(1, tamHeap, gameList);
            tamHeap--;
            reorganizar(1, tamHeap, gameList);
        }

        Game[] tmp2 = new Game[tam];
        for (int i = 0; i < tam; i++) {
            tmp2[i] = gameList[i + 1];
        }
        gameList = tmp2;

        return gameList;
    }

    static void reorganizar(int i, int tamHeap, Game[] gameList) {
        int filho;
        while (i <= (tamHeap / 2)) {
            filho = obterFilhoMaior(i, tamHeap, gameList);
            HLogMetrics.comparacoes++;
            if (ehMaior(gameList, filho, i)) {
                trocarPosicoes(i, filho, gameList);
                i = filho;
            } else {
                break;
            }
        }
    }

    static int obterFilhoMaior(int i, int tamHeap, Game[] gameList) {
        int filho;
        int filhoEsquerdo = 2 * i;
        int filhoDireito = 2 * i + 1;
        if (filhoDireito > tamHeap) {
            filho = filhoEsquerdo;
        } else {
            HLogMetrics.comparacoes++;
            if (ehMaior(gameList, filhoDireito, filhoEsquerdo)) {
                filho = filhoDireito;
            } else {
                filho = filhoEsquerdo;
            }
        }
        return filho;
    }

    static void trocarPosicoes(int p1, int p2, Game[] gameList) {
        Game aux = gameList[p2];
        gameList[p2] = gameList[p1];
        gameList[p1] = aux;
        HLogMetrics.movimentacoes += 3;
    }

    static boolean ehMaior(Game[] gameList, int p1, int p2) {
        if (gameList[p1].estimatedOwners > gameList[p2].estimatedOwners) {
            return true;
        } else if (gameList[p1].estimatedOwners == gameList[p2].estimatedOwners) {
            return gameList[p1].id > gameList[p2].id;
        }
        return false;
    }

    static void exibirJogos(Game[] jogosOrdenados, int tamanho) {
        for (int i = 0; i < tamanho; i++) {
            String releaseDate = (jogosOrdenados[i].releaseDate.charAt(1) == '/'
                    ? "0" + jogosOrdenados[i].releaseDate
                    : jogosOrdenados[i].releaseDate);
            System.out.println(
                    "=> " + jogosOrdenados[i].id + " ## " + jogosOrdenados[i].name + " ## " + releaseDate
                            + " ## " + jogosOrdenados[i].estimatedOwners + " ## " + jogosOrdenados[i].price
                            + " ## "
                            + formatarArray(jogosOrdenados[i].supportedLanguages,
                                    jogosOrdenados[i].supportedLanguagesCount)
                            + " ## " + jogosOrdenados[i].metacriticScore + " ## " + jogosOrdenados[i].userScore
                            + " ## " + jogosOrdenados[i].achievements
                            + " ## " + formatarArray(jogosOrdenados[i].publishers, jogosOrdenados[i].publishersCount)
                            + " ## " + formatarArray(jogosOrdenados[i].developers, jogosOrdenados[i].developersCount)
                            + " ## " + formatarArray(jogosOrdenados[i].categories, jogosOrdenados[i].categoriesCount)
                            + " ## " + formatarArray(jogosOrdenados[i].genres, jogosOrdenados[i].genresCount)
                            + " ## "
                            + (jogosOrdenados[i].tagsCount == 0 ? ""
                                    : formatarArray(jogosOrdenados[i].tags, jogosOrdenados[i].tagsCount))
                            + (jogosOrdenados[i].tagsCount == 0 ? "" : " ##"));
        }
    }

    static String formatarArray(String[] array, int tamanho) {
        String resultado = "";
        if (tamanho == 0)
            return "[]";
        resultado += "[";
        for (int i = 0; i < tamanho; i++) {
            resultado += array[i];
            if (i < tamanho - 1)
                resultado += ", ";
        }
        resultado += "]";
        return resultado;
    }
}

class JogosDigitados {
    public static Scanner sc;
    static int contador = 0;
    static String[] ids;
    static int idsTamanho;
    static Game[] gamesList;
    static int gamesListTamanho;

    public static int getTamanhoLista() {
        return gamesListTamanho;
    }

    static Game[] preparar(String[] idArray, int tamanho) {
        gamesList = new Game[NoArrayList.MAX_GAMES];
        gamesListTamanho = 0;

        ids = idArray;
        idsTamanho = tamanho;

        InputStream is = null;
        try {
            java.io.File arquivo = new java.io.File("/tmp/games.csv");
            if (!arquivo.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado!");
                return gamesList;
            }
            is = new FileInputStream(arquivo);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return gamesList;
        }

        sc = new Scanner(is);
        if (sc.hasNextLine())
            sc.nextLine();

        while (sc.hasNextLine() && idsTamanho > 0 && gamesListTamanho < gamesList.length) {
            String linha = sc.nextLine();
            contador = 0;
            int id = extrairId(linha);

            int indiceEncontrado = verificarId(id);
            if (indiceEncontrado != -1) {
                String name = extrairNome(linha);
                String releaseDate = extrairData(linha);
                int estimatedOwners = extrairDonos(linha);
                float price = extrairPreco(linha);

                String[] supportedLanguages = new String[NoArrayList.MAX_INNER_ARRAY];
                int supportedLanguagesCount = extrairIdiomas(linha, supportedLanguages);
                int metacriticScore = extrairMetacritic(linha);
                float userScore = extrairUserScore(linha);
                int achievements = extrairConquistas(linha);

                String[] publishers = new String[NoArrayList.MAX_INNER_ARRAY];
                int publishersCount = extrairArraysFinal(linha, publishers);
                String[] developers = new String[NoArrayList.MAX_INNER_ARRAY];
                int developersCount = extrairArraysFinal(linha, developers);
                String[] categories = new String[NoArrayList.MAX_INNER_ARRAY];
                int categoriesCount = extrairArraysFinal(linha, categories);
                String[] genres = new String[NoArrayList.MAX_INNER_ARRAY];
                int genresCount = extrairArraysFinal(linha, genres);
                String[] tags = new String[NoArrayList.MAX_INNER_ARRAY];
                int tagsCount = extrairArraysFinal(linha, tags);

                Game jogo = new Game(id, name, releaseDate, estimatedOwners, price,
                        supportedLanguages, supportedLanguagesCount, metacriticScore, userScore, achievements,
                        publishers, publishersCount, developers, developersCount, categories, categoriesCount, genres,
                        genresCount, tags, tagsCount);
                gamesList[gamesListTamanho++] = jogo;
                removerIdDaLista(indiceEncontrado);
            }
        }
        sc.close();

        Game[] resultadoFinal = new Game[gamesListTamanho];
        for (int i = 0; i < gamesListTamanho; i++) {
            resultadoFinal[i] = gamesList[i];
        }
        gamesList = resultadoFinal;

        return resultadoFinal;
    }

    static int verificarId(int id) {
        for (int i = 0; i < idsTamanho; i++) {
            if (Integer.parseInt(ids[i]) == id) {
                return i;
            }
        }
        return -1;
    }

    static void removerIdDaLista(int indice) {
        if (indice >= 0 && indice < idsTamanho) {
            for (int j = indice; j < idsTamanho - 1; j++) {
                ids[j] = ids[j + 1];
            }
            ids[idsTamanho - 1] = null;
            idsTamanho--;
        }
    }

    static int extrairId(String jogo) {
        int id = 0;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            id = id * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    static String extrairNome(String jogo) {
        String name = "";
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            contador++;
        }
        contador++;
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            name += jogo.charAt(contador);
            contador++;
        }
        return name;
    }

    static String extrairData(String jogo) {
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            contador++;
        }
        contador++;
        String dia = "", mes = "", ano = "";
        for (int i = 0; contador < jogo.length() && i < 3; i++) {
            mes += jogo.charAt(contador);
            contador++;
        }
        mes = mes.trim();
        switch (mes) {
            case "Jan":
                mes = "01";
                break;
            case "Feb":
                mes = "02";
                break;
            case "Mar":
                mes = "03";
                break;
            case "Apr":
                mes = "04";
                break;
            case "May":
                mes = "05";
                break;
            case "Jun":
                mes = "06";
                break;
            case "Jul":
                mes = "07";
                break;
            case "Aug":
                mes = "08";
                break;
            case "Sep":
                mes = "09";
                break;
            case "Oct":
                mes = "10";
                break;
            case "Nov":
                mes = "11";
                break;
            case "Dec":
                mes = "12";
                break;
            default:
                break;
        }
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador)) && jogo.charAt(contador) != ',') {
            contador++;
        }
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            dia += jogo.charAt(contador);
            contador++;
        }
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador))) {
            contador++;
        }
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            ano += jogo.charAt(contador);
            contador++;
        }
        if (dia.isEmpty())
            dia = "01";
        if (mes.isEmpty())
            mes = "01";
        if (ano.isEmpty())
            ano = "0000";
        return dia + "/" + mes + "/" + ano;
    }

    static int extrairDonos(String jogo) {
        int estimatedOwners = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            estimatedOwners = estimatedOwners * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return estimatedOwners;
    }

    static float extrairPreco(String jogo) {
        String price = "";
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador)) && jogo.charAt(contador) != 'F') {
            contador++;
        }
        while (contador < jogo.length() && (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.')) {
            price += jogo.charAt(contador);
            contador++;
        }
        price = price.trim();
        if (price.isEmpty() || price.equalsIgnoreCase("Free to play")) {
            return 0.0f;
        }
        price = price.replaceAll("[^0-9.]", "");
        try {
            return Float.parseFloat(price);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    static int extrairIdiomas(String jogo, String[] supportedLanguages) {
        int count = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != ']' && count < supportedLanguages.length) {
            String lingua = "";
            while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))) {
                contador++;
            }
            while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != ']') {
                if (Character.isAlphabetic(jogo.charAt(contador)) || jogo.charAt(contador) == ' '
                        || jogo.charAt(contador) == '-') {
                    lingua += jogo.charAt(contador);
                }
                contador++;
            }
            supportedLanguages[count++] = lingua;
        }
        return count;
    }

    static int extrairMetacritic(String jogo) {
        String metacriticScore = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            metacriticScore += jogo.charAt(contador);
            contador++;
        }
        if (metacriticScore.isEmpty())
            return -1;
        else
            return Integer.parseInt(metacriticScore);
    }

    static float extrairUserScore(String jogo) {
        String userScore = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.')) {
            userScore += jogo.charAt(contador);
            contador++;
        }
        if (userScore.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(userScore);
    }

    static int extrairConquistas(String jogo) {
        String achievements = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        while (contador < jogo.length() && (Character.isDigit(jogo.charAt(contador)) || jogo.charAt(contador) == '.')) {
            achievements += jogo.charAt(contador);
            contador++;
        }
        if (achievements.isEmpty())
            return -1;
        else
            return Integer.parseInt(achievements);
    }

    static int extrairArraysFinal(String jogo, String[] categoria) {
        int count = 0;
        boolean teste = false;
        while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))
                && !Character.isDigit(jogo.charAt(contador))) {
            if (jogo.charAt(contador) == '"')
                teste = true;
            contador++;
        }
        if (teste) {
            while (contador < jogo.length() && jogo.charAt(contador) != '"' && count < categoria.length) {
                String parte = "";
                while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != '"') {
                    parte += jogo.charAt(contador);
                    contador++;
                }
                while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))
                        && !Character.isDigit(jogo.charAt(contador))
                        && jogo.charAt(contador) != '"') {
                    contador++;
                }
                categoria[count++] = parte;
            }
            contador++;
        } else {
            if (count < categoria.length) {
                String parte = "";
                while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                    parte += jogo.charAt(contador);
                    contador++;
                }
                categoria[count++] = parte;
            }
        }
        if (contador < jogo.length() && jogo.charAt(contador) == ',') {
            contador++;
        }
        return count;
    }
}