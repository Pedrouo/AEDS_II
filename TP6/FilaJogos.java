import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class ParametrosGlobais {
    public static final int CAPACIDADE_MAXIMA_JOGOS = 500;
    public static final int TAMANHO_ARRAYS_INTERNOS = 50;
    public static final int LIMITE_IDS = 100;
}

class RegistroJogo {
    int identificador;
    String titulo;
    String dataLancamento;
    int proprietariosEstimados;
    float valor;
    String[] idiomasSuportados;
    int quantidadeIdiomas;
    int notaMetacritic;
    float avaliacaoUsuarios;
    int conquistas;
    String[] editoras;
    int quantidadeEditoras;
    String[] desenvolvedores;
    int quantidadeDesenvolvedores;
    String[] categorias;
    int quantidadeCategorias;
    String[] generos;
    int quantidadeGeneros;
    String[] etiquetas;
    int quantidadeEtiquetas;

    RegistroJogo() {
        this.identificador = 0;
        this.titulo = "";
        this.dataLancamento = "";
        this.proprietariosEstimados = 0;
        this.valor = 0.0f;
        this.idiomasSuportados = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
        this.quantidadeIdiomas = 0;
        this.notaMetacritic = -1;
        this.avaliacaoUsuarios = -1.0f;
        this.conquistas = 0;
        this.editoras = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
        this.quantidadeEditoras = 0;
        this.desenvolvedores = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
        this.quantidadeDesenvolvedores = 0;
        this.categorias = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
        this.quantidadeCategorias = 0;
        this.generos = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
        this.quantidadeGeneros = 0;
        this.etiquetas = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
        this.quantidadeEtiquetas = 0;
    }

    RegistroJogo(int identificador, String titulo, String dataLancamento, int proprietariosEstimados, float valor,
            String[] idiomasSuportados, int quantidadeIdiomas, int notaMetacritic, float avaliacaoUsuarios,
            int conquistas,
            String[] editoras, int quantidadeEditoras, String[] desenvolvedores, int quantidadeDesenvolvedores,
            String[] categorias, int quantidadeCategorias, String[] generos, int quantidadeGeneros, 
            String[] etiquetas, int quantidadeEtiquetas) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.dataLancamento = dataLancamento;
        this.proprietariosEstimados = proprietariosEstimados;
        this.valor = valor;

        this.idiomasSuportados = idiomasSuportados;
        this.quantidadeIdiomas = quantidadeIdiomas;
        this.editoras = editoras;
        this.quantidadeEditoras = quantidadeEditoras;
        this.desenvolvedores = desenvolvedores;
        this.quantidadeDesenvolvedores = quantidadeDesenvolvedores;
        this.categorias = categorias;
        this.quantidadeCategorias = quantidadeCategorias;
        this.generos = generos;
        this.quantidadeGeneros = quantidadeGeneros;
        this.etiquetas = etiquetas;
        this.quantidadeEtiquetas = quantidadeEtiquetas;

        this.notaMetacritic = notaMetacritic;
        this.avaliacaoUsuarios = avaliacaoUsuarios;
        this.conquistas = conquistas;
    }
}

// Nó da lista encadeada
class NodoLista {
    public NodoLista proximo;
    public NodoLista anterior;
    public RegistroJogo jogo;

    public NodoLista() {
        this(null);
    }

    public NodoLista(RegistroJogo registro) {
        proximo = null;
        anterior = null;
        jogo = registro;
    }
}

// Lista duplamente encadeada
class ListaEncadeadaDupla {
    private NodoLista cabeca, cauda;

    public ListaEncadeadaDupla() {
        cabeca = new NodoLista();
        cauda = cabeca;
    }

    // Adicionar no início
    public void adicionarNoInicio(RegistroJogo jogo) {
        if (cabeca == cauda) {
            cabeca.proximo = new NodoLista(jogo);
            cauda = cabeca.proximo;
            cauda.anterior = cabeca;
        } else {
            NodoLista temporario = new NodoLista(jogo);
            temporario.proximo = cabeca.proximo;
            cabeca.proximo = temporario;
            temporario.proximo.anterior = temporario;
            temporario.anterior = cabeca;
            temporario = null;
        }
    }

    // Adicionar no final
    public void adicionarNoFinal(RegistroJogo jogo) {
        if (cabeca == cauda) {
            cabeca.proximo = new NodoLista(jogo);
            cauda = cabeca.proximo;
            cauda.anterior = cabeca;
        } else {
            NodoLista temporario = new NodoLista(jogo);
            cauda.proximo = temporario;
            temporario.anterior = cauda;
            cauda = temporario;
        }
    }

    // Adicionar em posição
    public void adicionarNaPosicao(RegistroJogo jogo, int posicao) {
        if (posicao < 0 || posicao > obterTamanho() + 1)
            return;
        if (posicao == 0)
            adicionarNoInicio(jogo);
        else if (posicao == obterTamanho())
            adicionarNoFinal(jogo);
        else {
            NodoLista iterador = cabeca;
            for (int idx = 0; idx < posicao; idx++, iterador = iterador.proximo) {
            }
            NodoLista temporario = new NodoLista(jogo);
            temporario.proximo = iterador.proximo;
            iterador.proximo = temporario;
            temporario.anterior = iterador;
            temporario.proximo.anterior = temporario;
            iterador = temporario = null;
        }
    }

    // Obter tamanho
    public int obterTamanho() {
        int tamanho = 0;
        for (NodoLista iterador = cabeca.proximo; iterador != null; iterador = iterador.proximo) {
            tamanho++;
        }
        return tamanho;
    }

    // Extrair do início
    public RegistroJogo extrairDoInicio() throws Exception {
        if (cabeca == cauda)
            throw new Exception("Erro ao remover (vazia)!");
        RegistroJogo elemento = cabeca.proximo.jogo;
        if (cabeca.proximo == cauda) {
            NodoLista temporario = cabeca.proximo;
            cabeca.proximo = null;
            cauda.anterior = null;      
            cauda = cabeca;
            temporario = null;
        } else {
            NodoLista temporario = cabeca.proximo;
            cabeca.proximo = cabeca.proximo.proximo;
            cabeca.proximo.anterior = cabeca;
            temporario.proximo = temporario.anterior = temporario = null;
        }
        return elemento;
    }

    // Extrair do final
    public RegistroJogo extrairDoFinal() throws Exception {
        if (cabeca == cauda)
            throw new Exception("Erro ao remover (vazia)!");
        RegistroJogo elemento = cauda.jogo;
        cauda.anterior.proximo = null;
        cauda = cauda.anterior;
        cauda.proximo = null;
        return elemento;
    }

    // Extrair de posição
    public RegistroJogo extrairDaPosicao(int posicao) throws Exception {
        posicao++;
        if (cauda == cabeca)
            throw new Exception("Erro ao remover (vazia)!");
        RegistroJogo elemento = null;
        int tamanho = obterTamanho();
        if (posicao >= tamanho || posicao < 0)
            throw new Exception("Erro ao remover (posição inválida)!");
        if (posicao == 0)
            elemento = extrairDoInicio();
        else if (posicao == tamanho - 1)
            elemento = extrairDoFinal();
        else {
            NodoLista iterador = cabeca;
            for (int idx = 0; idx < posicao; idx++, iterador = iterador.proximo) {
            }
            iterador.anterior.proximo = iterador.proximo;
            iterador.proximo.anterior = iterador.anterior;
            elemento = iterador.jogo;
            iterador = iterador.proximo = iterador.anterior = iterador = null;
        }
        return elemento;
    }

    // Exibir conteúdo
    public void exibirConteudo() {
        int idx = 0;
        for (NodoLista iterador = cabeca.proximo; iterador != null; iterador = iterador.proximo) {
            String dataFormatada = (iterador.jogo.dataLancamento.charAt(1) == '/'
                    ? "0" + iterador.jogo.dataLancamento
                    : iterador.jogo.dataLancamento);
            System.out.println("[" + idx + "]" +
                    " => " + iterador.jogo.identificador + " ## " + iterador.jogo.titulo + " ## " + dataFormatada
                    + " ## " + iterador.jogo.proprietariosEstimados + " ## " + iterador.jogo.valor
                    + " ## "
                    + formatarArray(iterador.jogo.idiomasSuportados,
                            iterador.jogo.quantidadeIdiomas)
                    + " ## " + iterador.jogo.notaMetacritic + " ## " + iterador.jogo.avaliacaoUsuarios
                    + " ## " + iterador.jogo.conquistas
                    + " ## " + formatarArray(iterador.jogo.editoras, iterador.jogo.quantidadeEditoras)
                    + " ## " + formatarArray(iterador.jogo.desenvolvedores, iterador.jogo.quantidadeDesenvolvedores)
                    + " ## " + formatarArray(iterador.jogo.categorias, iterador.jogo.quantidadeCategorias)
                    + " ## " + formatarArray(iterador.jogo.generos, iterador.jogo.quantidadeGeneros)
                    + " ## "
                    + (iterador.jogo.quantidadeEtiquetas == 0 ? ""
                            : formatarArray(iterador.jogo.etiquetas, iterador.jogo.quantidadeEtiquetas))
                    + (iterador.jogo.quantidadeEtiquetas == 0 ? "" : " ##"));
            idx++;
        }
    }

    // Formatar array
    static String formatarArray(String[] array, int tamanho) {
        String resultado = "";
        if (tamanho == 0) {
            return "[]";
        }
        resultado += "[";
        for (int i = 0; i < tamanho; i++) {
            resultado += array[i];
            if (i < tamanho - 1) {
                resultado += ", ";
            }
        }
        resultado += "]";
        return resultado;
    }
}

// Estrutura de fila baseada em lista dupla
class FilaEncadeada {
    private ListaEncadeadaDupla estruturaInterna;

    public FilaEncadeada() {
        estruturaInterna = new ListaEncadeadaDupla();
    }

    // Inserir na fila (enfileirar)
    public void inserir(RegistroJogo jogo) {
        estruturaInterna.adicionarNoFinal(jogo);
    }

    // Remover da fila (desenfileirar)
    public RegistroJogo remover() throws Exception {
        if (estruturaInterna.obterTamanho() == 0) {
            throw new Exception("Erro ao desenfileirar (fila vazia)!");
        }
        return estruturaInterna.extrairDoInicio();
    }

    // Exibir elementos
    public void exibir() {
        estruturaInterna.exibirConteudo();
    }

    // Obter tamanho
    public int obterTamanho() {
        return estruturaInterna.obterTamanho();
    }

    // Verificar se está vazia
    public boolean verificarVazia() {
        return estruturaInterna.obterTamanho() == 0;
    }
}

public class FilaJogos {
    public static Scanner leitor;
    static int posicaoLeitura = 0;

    public static void main(String[] args) {
        leitor = new Scanner(System.in);
        String entrada = leitor.nextLine();
        String[] identificadores = new String[ParametrosGlobais.LIMITE_IDS];
        int totalIdentificadores = 0;

        while (!entrada.equals("FIM") && totalIdentificadores < identificadores.length) {
            identificadores[totalIdentificadores++] = entrada;
            entrada = leitor.nextLine();
        }

        FilaEncadeada filaJogos = new FilaEncadeada();
        preencherFilaComJogos(filaJogos, identificadores, totalIdentificadores);

        int quantidadeOperacoes = leitor.nextInt();
        leitor.nextLine();
        
        for (int i = 0; i < quantidadeOperacoes; i++) {
            posicaoLeitura = 0;
            String comandoCompleto = leitor.nextLine();
            String codigoOperacao = extrairCodigoOperacao(comandoCompleto);

            String identificador = "";
            RegistroJogo jogoEncontrado = null;
            
            if (codigoOperacao.equals("I")) {
                identificador = extrairIdentificador(comandoCompleto);
                if (!identificador.isEmpty())
                    jogoEncontrado = ManipuladorJogos.buscarJogo(identificador);
            }

            switch (codigoOperacao) {
                case "I":
                    filaJogos.inserir(jogoEncontrado);
                    break;
                case "R":
                    try {
                        RegistroJogo removido = filaJogos.remover();
                        System.out.println("(R) " + removido.titulo);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
        
        filaJogos.exibir();
    }

    // Preencher fila com jogos iniciais
    static void preencherFilaComJogos(FilaEncadeada fila, String[] identificadores, int tamanho) {
        for (int idx = 0; idx < tamanho; idx++) {
            RegistroJogo jogo = ManipuladorJogos.buscarJogo(identificadores[idx]);
            if (jogo != null) {
                fila.inserir(jogo);
            }
        }
    }

    // Extrair código da operação
    static String extrairCodigoOperacao(String entrada) {
        String operacao = "";
        while (posicaoLeitura < 1 && posicaoLeitura < entrada.length()) {
            operacao += entrada.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        posicaoLeitura++;
        return operacao;
    }

    // Extrair identificador
    static String extrairIdentificador(String entrada) {
        String id = "";
        while (posicaoLeitura < entrada.length()) {
            if (entrada.charAt(posicaoLeitura) != ' ') {
                id += entrada.charAt(posicaoLeitura);
                posicaoLeitura++;
            } else {
                posicaoLeitura++;
            }
        }
        return id;
    }
}

// Classe para manipular jogos do arquivo CSV
class ManipuladorJogos {
    public static Scanner scanner;
    static int indiceLeitura = 0;

    static RegistroJogo buscarJogo(String idProcurado) {
        InputStream fluxoEntrada = null;
        try {
            java.io.File arquivoCsv = new java.io.File("/tmp/games.csv");
            if (!arquivoCsv.exists()) {
                System.out.println("Arquivo 'games.csv' não encontrado!");
                return null;
            }
            fluxoEntrada = new FileInputStream(arquivoCsv);
        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
            return null;
        }
        scanner = new Scanner(fluxoEntrada);
        RegistroJogo jogoResultado = new RegistroJogo();
        boolean localizado = false;

        if (scanner.hasNextLine())
            scanner.nextLine();

        while (scanner.hasNextLine() && localizado == false) {
            String linhaAtual = scanner.nextLine();
            indiceLeitura = 0;

            int idLinha = interpretarIdentificador(linhaAtual);

            if (idLinha == Integer.parseInt(idProcurado)) {
                localizado = true;
                String nome = interpretarNome(linhaAtual);
                String dataLancamento = interpretarData(linhaAtual);
                int proprietarios = interpretarProprietarios(linhaAtual);
                float preco = interpretarPreco(linhaAtual);

                String[] idiomas = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
                int qtdIdiomas = interpretarIdiomas(linhaAtual, idiomas);
                int notaMeta = interpretarNotaMetacritic(linhaAtual);
                float notaUsuario = interpretarNotaUsuario(linhaAtual);
                int totalConquistas = interpretarConquistas(linhaAtual);

                String[] editoras = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
                int qtdEditoras = interpretarArraysFinais(linhaAtual, editoras);
                String[] desenvolvedores = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
                int qtdDesenvolvedores = interpretarArraysFinais(linhaAtual, desenvolvedores);
                String[] categorias = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
                int qtdCategorias = interpretarArraysFinais(linhaAtual, categorias);
                String[] generos = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
                int qtdGeneros = interpretarArraysFinais(linhaAtual, generos);
                String[] tags = new String[ParametrosGlobais.TAMANHO_ARRAYS_INTERNOS];
                int qtdTags = interpretarArraysFinais(linhaAtual, tags);

                jogoResultado = new RegistroJogo(idLinha, nome, dataLancamento, proprietarios, preco,
                        idiomas, qtdIdiomas, notaMeta, notaUsuario, totalConquistas,
                        editoras, qtdEditoras, desenvolvedores, qtdDesenvolvedores, categorias, qtdCategorias, generos,
                        qtdGeneros, tags, qtdTags);
            }
        }
        scanner.close();
        try {
            fluxoEntrada.close();
        } catch (Exception e) {
            System.out.println("Erro ao fechar o arquivo: " + e.getMessage());
        }
        return jogoResultado;
    }

    static int interpretarIdentificador(String linha) {
        int id = 0;
        while (indiceLeitura < linha.length() && Character.isDigit(linha.charAt(indiceLeitura))) {
            id = id * 10 + (linha.charAt(indiceLeitura) - '0');
            indiceLeitura++;
        }
        return id;
    }

    static String interpretarNome(String linha) {
        String nome = "";
        while (linha.charAt(indiceLeitura) != ',' && indiceLeitura < linha.length()) {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (linha.charAt(indiceLeitura) != ',' && indiceLeitura < linha.length()) {
            nome += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        return nome;
    }

    static String interpretarData(String linha) {
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != '"') {
            indiceLeitura++;
        }
        indiceLeitura++;
        String dia = "", mes = "", ano = "";

        for (int i = 0; indiceLeitura < linha.length() && i < 3; i++) {
            mes += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        mes = mes.trim();
        switch (mes) {
            case "Jan": mes = "01"; break;
            case "Feb": mes = "02"; break;
            case "Mar": mes = "03"; break;
            case "Apr": mes = "04"; break;
            case "May": mes = "05"; break;
            case "Jun": mes = "06"; break;
            case "Jul": mes = "07"; break;
            case "Aug": mes = "08"; break;
            case "Sep": mes = "09"; break;
            case "Oct": mes = "10"; break;
            case "Nov": mes = "11"; break;
            case "Dec": mes = "12"; break;
            default: break;
        }

        while (indiceLeitura < linha.length() && !Character.isDigit(linha.charAt(indiceLeitura)) && linha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }

        while (indiceLeitura < linha.length() && Character.isDigit(linha.charAt(indiceLeitura))) {
            dia += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }

        while (indiceLeitura < linha.length() && !Character.isDigit(linha.charAt(indiceLeitura))) {
            indiceLeitura++;
        }

        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != '"') {
            ano += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (dia.isEmpty()) dia = "01";
        if (mes.isEmpty()) mes = "01";
        if (ano.isEmpty()) ano = "0000";
        return dia + "/" + mes + "/" + ano;
    }

    static int interpretarProprietarios(String linha) {
        int proprietarios = 0;
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',') {
            proprietarios = proprietarios * 10 + (linha.charAt(indiceLeitura) - '0');
            indiceLeitura++;
        }
        return proprietarios;
    }

    static float interpretarPreco(String linha) {
        String preco = "";
        while (indiceLeitura < linha.length() && !Character.isDigit(linha.charAt(indiceLeitura)) && linha.charAt(indiceLeitura) != 'F') {
            indiceLeitura++;
        }
        while (indiceLeitura < linha.length() && (Character.isDigit(linha.charAt(indiceLeitura)) || linha.charAt(indiceLeitura) == '.')) {
            preco += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        preco = preco.trim();
        if (preco.isEmpty() || preco.equalsIgnoreCase("Free to play")) {
            return 0.0f;
        }
        preco = preco.replaceAll("[^0-9.]", "");
        try {
            return Float.parseFloat(preco);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    static int interpretarIdiomas(String linha, String[] idiomas) {
        int contador = 0;
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ']' && contador < idiomas.length) {
            String idioma = "";
            while (indiceLeitura < linha.length() && !Character.isAlphabetic(linha.charAt(indiceLeitura))) {
                indiceLeitura++;
            }
            while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',' && linha.charAt(indiceLeitura) != ']') {
                if (Character.isAlphabetic(linha.charAt(indiceLeitura)) || linha.charAt(indiceLeitura) == ' '
                        || linha.charAt(indiceLeitura) == '-') {
                    idioma += linha.charAt(indiceLeitura);
                }
                indiceLeitura++;
            }
            idiomas[contador++] = idioma;
        }
        return contador;
    }

    static int interpretarNotaMetacritic(String linha) {
        String nota = "";
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < linha.length() && Character.isDigit(linha.charAt(indiceLeitura))) {
            nota += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (nota.isEmpty())
            return -1;
        else
            return Integer.parseInt(nota);
    }

    static float interpretarNotaUsuario(String linha) {
        String nota = "";
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < linha.length() && (Character.isDigit(linha.charAt(indiceLeitura)) || linha.charAt(indiceLeitura) == '.')) {
            nota += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (nota.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(nota);
    }

    static int interpretarConquistas(String linha) {
        String conquistas = "";
        while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < linha.length() && (Character.isDigit(linha.charAt(indiceLeitura)) || linha.charAt(indiceLeitura) == '.')) {
            conquistas += linha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (conquistas.isEmpty())
            return -1;
        else
            return Integer.parseInt(conquistas);
    }

    static int interpretarArraysFinais(String linha, String[] array) {
        int contador = 0;
        boolean entreAspas = false;
        while (indiceLeitura < linha.length() && !Character.isAlphabetic(linha.charAt(indiceLeitura))
                && !Character.isDigit(linha.charAt(indiceLeitura))) {
            if (linha.charAt(indiceLeitura) == '"')
                entreAspas = true;
            indiceLeitura++;
        }
        if (entreAspas) {
            while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != '"' && contador < array.length) {
                String fragmento = "";
                while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',' && linha.charAt(indiceLeitura) != '"') {
                    fragmento += linha.charAt(indiceLeitura);
                    indiceLeitura++;
                }
                while (indiceLeitura < linha.length() && !Character.isAlphabetic(linha.charAt(indiceLeitura))
                        && !Character.isDigit(linha.charAt(indiceLeitura))
                        && linha.charAt(indiceLeitura) != '"') {
                    indiceLeitura++;
                }
                array[contador++] = fragmento;
            }
            indiceLeitura++;
        } else {
            if (contador < array.length) {
                String fragmento = "";
                while (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) != ',') {
                    fragmento += linha.charAt(indiceLeitura);
                    indiceLeitura++;
                }
                array[contador++] = fragmento;
            }
        }
        if (indiceLeitura < linha.length() && linha.charAt(indiceLeitura) == ',') {
            indiceLeitura++;
        }
        return contador;
    }
}