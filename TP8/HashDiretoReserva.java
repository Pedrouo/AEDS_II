import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

class NoArrayListHDR {
    public static final int MAX_GAMES = 500;
    public static final int MAX_INNER_ARRAY = 50;
    public static final int MAX_IDS = 100;
}

// Classe de Dados 
class GameBinarioHDR {
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

    GameBinarioHDR() {
        this.id = 0;
        this.name = "";
        this.releaseDate = "";
        this.estimatedOwners = 0;
        this.price = 0.0f;
        this.supportedLanguages = new String[NoArrayListHDR.MAX_INNER_ARRAY];
        this.supportedLanguagesCount = 0;
        this.metacriticScore = -1;
        this.userScore = -1.0f;
        this.achievements = 0;
        this.publishers = new String[NoArrayListHDR.MAX_INNER_ARRAY];
        this.publishersCount = 0;
        this.developers = new String[NoArrayListHDR.MAX_INNER_ARRAY];
        this.developersCount = 0;
        this.categories = new String[NoArrayListHDR.MAX_INNER_ARRAY];
        this.categoriesCount = 0;
        this.genres = new String[NoArrayListHDR.MAX_INNER_ARRAY];
        this.genresCount = 0;
        this.tags = new String[NoArrayListHDR.MAX_INNER_ARRAY];
        this.tagsCount = 0;
    }

    GameBinarioHDR(int id, String name, String releaseDate, int estimatedOwners, float price,
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

// Classe Hash
class HashReserva {
    GameBinarioHDR tabela[];
    int m1, m2, m, reserva;

    public HashReserva() {
        this(21, 9);
    }

    public HashReserva(int m1, int m2) {
        this.m1 = m1;
        this.m2 = m2;
        this.m = m1 + m2;
        this.tabela = new GameBinarioHDR[this.m];
        for (int i = 0; i < m1; i++) {
            tabela[i] = null;
        }
        reserva = 0;
    }

    public int h(int indice) {
        return indice % m1;
    }

    public boolean inserir(GameBinarioHDR game) {
        boolean resp = false;
        if (game != null) {
            int indice = 0;
            for (int i = 0; i < game.name.length(); i++) {
                indice += (int) game.name.charAt(i);
            }
            int pos = h(indice);
            if (tabela[pos] == null) {
                tabela[pos] = game;
                resp = true;
            } else if (reserva < m2) {
                tabela[m1 + reserva] = game;
                reserva++;
                resp = true;
            }
        }
        return resp;
    }

    public boolean pesquisar(String name) {
        boolean resp = false;
        int indice = 0;
        for (int i = 0; i < name.length(); i++) {
            indice += (int) name.charAt(i);
        }
        int pos = h(indice);
        if (tabela[pos] != null && tabela[pos].name.equals(name)) {
            resp = true;
        } else if (tabela[pos] != null) {
            for (int i = 0; i < reserva; i++) {
                if (tabela[m1 + i].name.equals(name)) {
                    pos = m1 + i;
                    resp = true;
                    i = reserva;
                }
            }
        }
        // Imprime o resultado da busca
        if (resp) {
            System.out.println(name + ":  (Posicao: " + pos + ") SIM");
            HashDiretoReserva.logWriter.println(name + ":  (Posicao: " + pos + ") SIM");
        }
        else {
            System.out.println(name + ":  (Posicao: " + pos + ") NAO");
            HashDiretoReserva.logWriter.println(name + ":  (Posicao: " + pos + ") NAO");
        }
        return resp;
    }
}

// Classe Principal
public class HashDiretoReserva {
    public static Scanner sc;
    public static PrintWriter logWriter;

    // Main
    public static void main(String[] args) {
        sc = new Scanner(System.in);

        // Arquivo log
        try {
            logWriter = new PrintWriter(new FileWriter("876881_hashReserva.txt"));
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo de log: " + e.getMessage());
            return; // Interrompe se não puder criar o log
        }

        // Capturando os ids que serão adicionados
        String entrada = sc.nextLine();
        String ids[] = new String[2000];
        int tam = 0;
        for (; !entrada.equals("FIM"); tam++) {
            ids[tam] = entrada;
            entrada = sc.nextLine();
        }
        // Criando a árvore
        HashReserva hash = JogosDigitadosHashReserva.inicializacao(ids, tam);

        // Vendo a posição na árvore e mostrando
        entrada = sc.nextLine();
        while (!entrada.equals("FIM")) {
            hash.pesquisar(entrada);

            entrada = sc.nextLine();
        }
        sc.close();
    }
}

// Leitura de Arquivo
class JogosDigitadosHashReserva {
    public static Scanner sc;
    static int contador = 0;
    static String[] ids;
    static int idsTamanho;

    static HashReserva inicializacao(String[] idArray, int tamanho) {
        HashReserva hash = new HashReserva();

        ids = idArray;
        idsTamanho = tamanho;

        for (int j = 0; j < tamanho; j++) {
            int indiceEncontrado = -1;

            try {
                java.io.File arquivo = new java.io.File("/tmp/games.csv");
                if (!arquivo.exists()) {
                    System.out.println("Arquivo 'games.csv' não encontrado!");
                    return hash;
                }

                InputStream is = new FileInputStream(arquivo);
                Scanner sc = new Scanner(is);

                if (sc.hasNextLine())
                    sc.nextLine();

                while (sc.hasNextLine() && indiceEncontrado == -1) {
                    String linha = sc.nextLine();
                    contador = 0;

                    int id = capturaId(linha);
                    indiceEncontrado = igualId(id);

                    if (indiceEncontrado != -1) {
                        String name = capturaName(linha);
                        String releaseDate = capturaReleaseDate(linha);
                        int estimatedOwners = capturaEstimatedOwners(linha);
                        float price = capturaPrice(linha);

                        String[] supportedLanguages = new String[NoArrayListHDR.MAX_INNER_ARRAY];
                        int supportedLanguagesCount = capturaSupportedLanguages(linha, supportedLanguages);
                        int metacriticScore = capturaMetacriticScore(linha);
                        float userScore = capturaUserScore(linha);
                        int achievements = capturaAchievements(linha);

                        String[] publishers = new String[NoArrayListHDR.MAX_INNER_ARRAY];
                        int publishersCount = capturaUltimosArryays(linha, publishers);
                        String[] developers = new String[NoArrayListHDR.MAX_INNER_ARRAY];
                        int developersCount = capturaUltimosArryays(linha, developers);
                        String[] categories = new String[NoArrayListHDR.MAX_INNER_ARRAY];
                        int categoriesCount = capturaUltimosArryays(linha, categories);
                        String[] genres = new String[NoArrayListHDR.MAX_INNER_ARRAY];
                        int genresCount = capturaUltimosArryays(linha, genres);
                        String[] tags = new String[NoArrayListHDR.MAX_INNER_ARRAY];
                        int tagsCount = capturaUltimosArryays(linha, tags);

                        GameBinarioHDR jogo = new GameBinarioHDR(id, name, releaseDate, estimatedOwners, price,
                                supportedLanguages, supportedLanguagesCount, metacriticScore, userScore, achievements,
                                publishers, publishersCount, developers, developersCount, categories, categoriesCount,
                                genres, genresCount, tags, tagsCount);

                        removerId(indiceEncontrado);
                        hash.inserir(jogo);
                    }
                }

                sc.close();
                is.close();

            } catch (Exception e) {
                System.out.println("Erro ao abrir ou ler o arquivo: " + e.getMessage());
            }
        }

        return hash;
    }

    // Id igual
    static int igualId(int id) {
        for (int i = 0; i < idsTamanho; i++) {
            if (idsTamanho > 0 && Integer.parseInt(ids[0]) == id)
                return 0; // Se encontrar, retorna sempre 0, o índice do primeiro elemento
        }
        return -1;
    }

    // Função para remover o ID da lista de pesquisa (sempre remove no índice 0)
    static void removerId(int indice) {
        if (indice == 0 && idsTamanho > 0) {
            for (int j = indice; j < idsTamanho - 1; j++) {
                ids[j] = ids[j + 1];
            }
            ids[idsTamanho - 1] = null;
            idsTamanho--;
        }
    }

    static int capturaId(String jogo) {
        int id = 0;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            id = id * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    static String capturaName(String jogo) {
        String name = "";
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            contador++;
        }
        contador++;
        // Trata o caso de nome entre aspas
        if (contador < jogo.length() && jogo.charAt(contador) == '"') {
            contador++;
            while (contador < jogo.length() && jogo.charAt(contador) != '"') {
                name += jogo.charAt(contador);
                contador++;
            }
            if (contador < jogo.length())
                contador++; // Pula a aspa de fechamento
        } else {
            while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                name += jogo.charAt(contador);
                contador++;
            }
        }
        return name;
    }

    static String capturaReleaseDate(String jogo) {
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            contador++;
        }
        if (contador < jogo.length())
            contador++;

        String dia = "", mes = "", ano = "";
        // Pegando mês
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
        // Pulando espaço
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador)) && jogo.charAt(contador) != ',') {
            contador++;
        }
        // Pegando dia
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            dia += jogo.charAt(contador);
            contador++;
        }
        // Pulando espaço
        while (contador < jogo.length() && !Character.isDigit(jogo.charAt(contador))) {
            contador++;
        }
        // Pegando ano
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

    static int capturaEstimatedOwners(String jogo) {
        int estimatedOwners = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        contador++;
        StringBuilder numStr = new StringBuilder();
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            if (Character.isDigit(jogo.charAt(contador))) {
                numStr.append(jogo.charAt(contador));
            }
            contador++;
        }
        try {
            estimatedOwners = Integer.parseInt(numStr.toString());
        } catch (NumberFormatException e) {
            estimatedOwners = 0;
        }
        return estimatedOwners;
    }

    static float capturaPrice(String jogo) {
        String price = "";
        while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != 'F'
                && !Character.isDigit(jogo.charAt(contador))) {
            contador++;
        }
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            price += jogo.charAt(contador);
            contador++;
        }
        price = price.trim();
        if (price.isEmpty() || price.toLowerCase().contains("free to play")) {
            return 0.0f;
        }
        price = price.replaceAll("[^0-9.]", "");
        try {
            return Float.parseFloat(price);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    static int capturaSupportedLanguages(String jogo, String[] supportedLanguages) {
        int count = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != ']' && count < supportedLanguages.length) {
            String lingua = "";
            while (contador < jogo.length() && !Character.isAlphabetic(jogo.charAt(contador))) {
                contador++;
            }
            while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != ']') {
                if (jogo.charAt(contador) != '"') {
                    lingua += jogo.charAt(contador);
                }
                contador++;
            }
            supportedLanguages[count++] = lingua.trim();
        }
        while (contador < jogo.length() && jogo.charAt(contador) != ',') {
            contador++;
        }
        if (contador < jogo.length())
            contador++;
        return count;
    }

    static int capturaMetacriticScore(String jogo) {
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

    static float capturaUserScore(String jogo) {
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

    static int capturaAchievements(String jogo) {
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
            return 0;
        else
            return Integer.parseInt(achievements);
    }

    static int capturaUltimosArryays(String jogo, String[] categoria) {
        int count = 0;
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            contador++;
        }

        if (contador < jogo.length() && jogo.charAt(contador) == '"') {
            contador++;
            while (contador < jogo.length() && jogo.charAt(contador) != '"' && count < categoria.length) {
                String parte = "";
                while (contador < jogo.length() && jogo.charAt(contador) != ',' && jogo.charAt(contador) != '"') {
                    parte += jogo.charAt(contador);
                    contador++;
                }
                categoria[count++] = parte.trim();
                if (contador < jogo.length() && jogo.charAt(contador) == ',') {
                    contador++;
                }
            }
            if (contador < jogo.length() && jogo.charAt(contador) == '"') {
                contador++;
            }
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