import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class NoArrayListAN {
    public static final int MAX_GAMES = 500;
    public static final int MAX_INNER_ARRAY = 50;
    public static final int MAX_IDS = 100;
}

class GameAN {
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

    GameAN() {
        this.id = 0;
        this.name = "";
        this.releaseDate = "";
        this.estimatedOwners = 0;
        this.price = 0.0f;
        this.supportedLanguages = new String[NoArrayListAN.MAX_INNER_ARRAY];
        this.supportedLanguagesCount = 0;
        this.metacriticScore = -1;
        this.userScore = -1.0f;
        this.achievements = 0;
        this.publishers = new String[NoArrayListAN.MAX_INNER_ARRAY];
        this.publishersCount = 0;
        this.developers = new String[NoArrayListAN.MAX_INNER_ARRAY];
        this.developersCount = 0;
        this.categories = new String[NoArrayListAN.MAX_INNER_ARRAY];
        this.categoriesCount = 0;
        this.genres = new String[NoArrayListAN.MAX_INNER_ARRAY];
        this.genresCount = 0;
        this.tags = new String[NoArrayListAN.MAX_INNER_ARRAY];
        this.tagsCount = 0;
    }

    GameAN(int id, String name, String releaseDate, int estimatedOwners, float price,
            String[] supportedLanguages, int supportedLanguagesCount, int metacriticScore, float userScore,
            int achievements,
            String[] publishers, int publishersCount, String[] developers, int developersCount,
            String[] categories, int categoriesCount, String[] genres, int genresCount, String[] tags, int tagsCount) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.estimatedOwners = estimatedOwners;
        this.price = price;

        // Copiando arrays
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

// No
class NoAN {
    public boolean cor;
    public GameAN game;
    public NoAN esq, dir;

    public NoAN() {
        this(null, false);
    }

    public NoAN(GameAN game, boolean cor) {
        this.cor = cor;
        this.game = game;
        esq = dir = null;
    }
}

// Ávore Alvinegra
class AlvinegraArvore {
    private NoAN raiz;

    public AlvinegraArvore() {
        raiz = null;
    }

    // Pesquisar e mostrar caminho
    public boolean pesquisar(String nome) {
        Alvinegra.logWriter.print(nome + ": =>raiz  ");
        System.out.print(nome + ": =>raiz  ");
        return pesquisar(nome, raiz);
    }

    private boolean pesquisar(String nome, NoAN i) {
        boolean resp;
        if (i == null) {
            resp = false;
            Alvinegra.logWriter.println("NAO");
            System.out.println("NAO");
        } else if (nome.equals(i.game.name)) {
            resp = true;
            Alvinegra.logWriter.println("SIM");
            System.out.println("SIM");
        } else if (nome.compareTo(i.game.name) < 0) {
            Alvinegra.logWriter.print("esq ");
            System.out.print("esq ");
            resp = pesquisar(nome, i.esq);
        } else {
            Alvinegra.logWriter.print("dir ");
            System.out.print("dir ");
            resp = pesquisar(nome, i.dir);
        }
        return resp;
    }

    // Inserir manualmente (3 primeiros elementos)
    public void inserir(GameAN game) throws Exception {
        // Se a arvore estiver vazia
        if (raiz == null) {
            raiz = new NoAN(game, false);  
        // Senao, se a arvore tiver um names/games
        } else if (raiz.esq == null && raiz.dir == null) {
            if (game.name.compareTo(raiz.game.name) < 0) {
                raiz.esq = new NoAN(game, false);
            } else {
                raiz.dir = new NoAN(game, false);
            }
        // Senao, se a arvore tiver dois names/games (raiz e dir)
        } else if (raiz.esq == null) {
            if (game.name.compareTo(raiz.game.name) < 0) {
                raiz.esq = new NoAN(raiz.game, false);
                raiz.game = game;
            } else if (game.name.compareTo(raiz.dir.game.name) < 0) {
                raiz.esq = new NoAN(game, false);
            } else {
                raiz.esq = new NoAN(raiz.game, false);
                raiz.game = raiz.dir.game;
                raiz.dir.game = game;
            }
            raiz.esq.cor = raiz.dir.cor = false;

        // Senao, se a arvore tiver dois names/games (raiz e esq)
        } else if (raiz.dir == null) {
            if (game.name.compareTo(raiz.game.name) > 0) {
                raiz.dir = new NoAN(raiz.game, false);
                raiz.game = game;
            } else if (game.name.compareTo(raiz.esq.game.name) > 0) {
                raiz.dir = new NoAN(game, false);
            } else {
                raiz.dir = new NoAN(raiz.game, false);
                raiz.game = raiz.esq.game;
                raiz.esq.game = game;
            }
            raiz.esq.cor = raiz.dir.cor = false;

            // Senao, a arvore tem tres ou mais names/games
        } else {
            inserir(game, null, null, null, raiz);
        }
        raiz.cor = false;
    }

    //Balancear a árvore
    private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) {
        // Se o pai tambem e vermelho, reequilibrar a arvore, rotacionando o avo
        if (pai.cor == true) {
            if (pai.game.name.compareTo(avo.game.name) > 0) { // rotacao a esquerda ou direita-esquerda
                if (i.game.name.compareTo(pai.game.name) > 0) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }
            } else { // rotacao a direita ou esquerda-direita
                if (i.game.name.compareTo(pai.game.name) < 0) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }
            if (bisavo == null) {
                raiz = avo;
            } else if (avo.game.name.compareTo(bisavo.game.name) < 0) {
                bisavo.esq = avo;
            } else {
                bisavo.dir = avo;
            }
            // reestabelecer as cores apos a rotacao
            avo.cor = false;
            if (avo.esq != null) avo.esq.cor = true;
            if (avo.dir != null) avo.dir.cor = true;
        } 
    }

    // Inserir recursivo geral
    private void inserir(GameAN game, NoAN bisavo, NoAN avo, NoAN pai, NoAN i) throws Exception {
        if (i == null) {
            if (game.name.compareTo(pai.game.name) < 0) {
                i = pai.esq = new NoAN(game, true);
            } else {
                i = pai.dir = new NoAN(game, true);
            }
            if (pai.cor == true) {
                balancear(bisavo, avo, pai, i);
            }
        } else {
            if (i.esq != null && i.dir != null && i.esq.cor == true && i.dir.cor == true) {
                i.cor = true;
                i.esq.cor = i.dir.cor = false;
                if (i == raiz) {
                    i.cor = false;
                } else if (pai.cor == true) {
                    balancear(bisavo, avo, pai, i);
                }
            }
            if (game.name.compareTo(i.game.name) < 0) {
                inserir(game, avo, pai, i, i.esq);
            } else if (game.name.compareTo(i.game.name) > 0) {
                inserir(game, avo, pai, i, i.dir);
            }
            // Se o jogo já existe (nome igual), simplesmente ignora
        }
    }

    // Rotação simples e dupla
    private NoAN rotacaoDir(NoAN no) {
        NoAN noEsq = no.esq;
        NoAN noEsqDir = noEsq.dir;

        noEsq.dir = no;
        no.esq = noEsqDir;

        return noEsq;
    }

    private NoAN rotacaoEsq(NoAN no) {
        NoAN noDir = no.dir;
        NoAN noDirEsq = noDir.esq;

        noDir.esq = no;
        no.dir = noDirEsq;
        return noDir;
    }

    private NoAN rotacaoDirEsq(NoAN no) {
        no.dir = rotacaoDir(no.dir);
        return rotacaoEsq(no);
    }

    private NoAN rotacaoEsqDir(NoAN no) {
        no.esq = rotacaoEsq(no.esq);
        return rotacaoDir(no);
    }
}

// Classe principal
public class Alvinegra {
    public static Scanner sc;
    public static PrintWriter logWriter;

    // Main
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        // .log
        try {
            logWriter = new PrintWriter(new FileWriter("876881_arvoreAlvinegra.txt"));
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo de log: " + e.getMessage());
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
        AlvinegraArvore arvore = JogosDIgitadosAN.inicializacao(ids, tam);
        // Vendo a posição na árvore e mostrando
        entrada = sc.nextLine();
        while (!entrada.equals("FIM")) {
            try {
                arvore.pesquisar(entrada.trim());
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
            entrada = sc.nextLine();
        }
        sc.close();
        logWriter.close();
    }
}

// Capturando os jogos digitados através do id pelo usuário
class JogosDIgitadosAN {
    // Scanner
    public static Scanner sc;
    // Variável que pula caracteres das linhas
    static int contador = 0;
    // Ids de pesquisa e seu tamanho
    static String[] ids;
    static int idsTamanho;

    // Capturando os jogos
    static AlvinegraArvore inicializacao(String[] idArray, int tamanho) {
        AlvinegraArvore arvore = new AlvinegraArvore();

        // Copiando IDs para a variável de classe 'ids'
        ids = idArray;
        idsTamanho = tamanho;

        // Para cada ID fornecido, busca no arquivo
        for (int j = 0; j < tamanho; j++) {
            try {
                java.io.File arquivo = new java.io.File("/tmp/games.csv");
                if (!arquivo.exists()) {
                    System.out.println("Arquivo 'games.csv' não encontrado!");
                    return arvore;
                }

                InputStream is = new FileInputStream(arquivo);
                Scanner sc = new Scanner(is);

                // Pula cabeçalho
                if (sc.hasNextLine())
                    sc.nextLine();

                int idProcurado = Integer.parseInt(ids[j]);
                boolean encontrado = false;

                while (sc.hasNextLine() && !encontrado) {
                    String linha = sc.nextLine();
                    contador = 0;

                    int id = capturaId(linha);

                    if (id == idProcurado) {
                        String name = capturaName(linha);
                        String releaseDate = capturaReleaseDate(linha);
                        int estimatedOwners = capturaEstimatedOwners(linha);
                        float price = capturaPrice(linha);

                        String[] supportedLanguages = new String[NoArrayListAN.MAX_INNER_ARRAY];
                        int supportedLanguagesCount = capturaSupportedLanguages(linha, supportedLanguages);
                        int metacriticScore = capturaMetacriticScore(linha);
                        float userScore = capturaUserScore(linha);
                        int achievements = capturaAchievements(linha);

                        String[] publishers = new String[NoArrayListAN.MAX_INNER_ARRAY];
                        int publishersCount = capturaUltimosArryays(linha, publishers);
                        String[] developers = new String[NoArrayListAN.MAX_INNER_ARRAY];
                        int developersCount = capturaUltimosArryays(linha, developers);
                        String[] categories = new String[NoArrayListAN.MAX_INNER_ARRAY];
                        int categoriesCount = capturaUltimosArryays(linha, categories);
                        String[] genres = new String[NoArrayListAN.MAX_INNER_ARRAY];
                        int genresCount = capturaUltimosArryays(linha, genres);
                        String[] tags = new String[NoArrayListAN.MAX_INNER_ARRAY];
                        int tagsCount = capturaUltimosArryays(linha, tags);

                        GameAN jogo = new GameAN(id, name, releaseDate, estimatedOwners, price,
                                supportedLanguages, supportedLanguagesCount, metacriticScore, userScore, achievements,
                                publishers, publishersCount, developers, developersCount, categories, categoriesCount,
                                genres, genresCount, tags, tagsCount);
                        
                        arvore.inserir(jogo);
                        encontrado = true;
                    }
                }

                sc.close();
                is.close();

            } catch (Exception e) {
                System.out.println("Erro ao abrir ou ler o arquivo: " + e.getMessage());
            }
        }

        return arvore;
    }

    // Função para verificar se o ID já foi pesquisado (não usada mais)
    static int igualId(int id) {
        for (int i = 0; i < idsTamanho; i++) {
            if (Integer.parseInt(ids[i]) == id)
                return i;
        }
        return -1;
    }

    // Função para remover o ID da lista de pesquisa (não usada mais)
    static void removerId(int indice) {
        if (indice >= 0 && indice < idsTamanho) {
            for (int j = indice; j < idsTamanho - 1; j++) {
                ids[j] = ids[j + 1];
            }
            ids[idsTamanho - 1] = null; // Limpa a última posição
            idsTamanho--; // Decrementa o tamanho
        }
    }

    // Capturando Id
    static int capturaId(String jogo) {
        int id = 0;
        while (contador < jogo.length() && Character.isDigit(jogo.charAt(contador))) {
            id = id * 10 + (jogo.charAt(contador) - '0');
            contador++;
        }
        return id;
    }

    // Capturando nome
    static String capturaName(String jogo) {
        String name = "";
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            contador++;
        }
        contador++;
        while (jogo.charAt(contador) != ',' && contador < jogo.length()) {
            name += jogo.charAt(contador);
            contador++;
        }
        return name.trim();
    }

    // Capturando Release Date
    static String capturaReleaseDate(String jogo) {
        while (contador < jogo.length() && jogo.charAt(contador) != '"') {
            contador++;
        }
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

    // Capturando Estimated Owners
    static int capturaEstimatedOwners(String jogo) {
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

    // Capturando Price
    static float capturaPrice(String jogo) {
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

    // Capturando idiomas
    static int capturaSupportedLanguages(String jogo, String[] supportedLanguages) {
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
        return count; // Retorna o tamanho real
    }

    // Capturando Metacritic Score
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

    // Capturando User Score
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

    // Capturando Achievements
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
            return -1;
        else
            return Integer.parseInt(achievements);
    }

    // Capturando Últimos Arrays
    static int capturaUltimosArryays(String jogo, String[] categoria) {
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
                categoria[count++] = parte; // Adiciona ao array e incrementa o contador
            }
            contador++;
        } else {
            if (count < categoria.length) {
                String parte = "";
                while (contador < jogo.length() && jogo.charAt(contador) != ',') {
                    parte += jogo.charAt(contador);
                    contador++;
                }
                categoria[count++] = parte; // Adiciona ao array e incrementa o contador
            }
        }
        // Pula a vírgula depois do array (se houver)
        if (contador < jogo.length() && jogo.charAt(contador) == ',') {
            contador++;
        }
        return count; // Retorna o tamanho real
    }
}