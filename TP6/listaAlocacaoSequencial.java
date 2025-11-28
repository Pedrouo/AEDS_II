import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class ConstantesConfig {
    public static final int CAPACIDADE_JOGOS = 500;
    public static final int TAMANHO_ARRAY_INTERNO = 50;
    public static final int LIMITE_IDENTIFICADORES = 100;
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
        this.idiomasSuportados = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
        this.quantidadeIdiomas = 0;
        this.notaMetacritic = -1;
        this.avaliacaoUsuarios = -1.0f;
        this.conquistas = 0;
        this.editoras = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
        this.quantidadeEditoras = 0;
        this.desenvolvedores = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
        this.quantidadeDesenvolvedores = 0;
        this.categorias = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
        this.quantidadeCategorias = 0;
        this.generos = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
        this.quantidadeGeneros = 0;
        this.etiquetas = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
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

// Elemento da lista encadeada
class ElementoLista {
    public ElementoLista proximo;
    public ElementoLista anterior;
    public RegistroJogo jogo;

    public ElementoLista() {
        this(null);
    }

    public ElementoLista(RegistroJogo registro) {
        proximo = null;
        anterior = null;
        jogo = registro;
    }
}

// Lista duplamente encadeada
class ListaDuplamenteEncadeada {
    private ElementoLista cabeca, cauda;

    public ListaDuplamenteEncadeada() {
        cabeca = new ElementoLista();
        cauda = cabeca;
    }

    // Adicionar no início
    public void adicionarNoInicio(RegistroJogo jogo) {
        if (cabeca == cauda) {
            cabeca.proximo = new ElementoLista(jogo);
            cauda = cabeca.proximo;
            cauda.anterior = cabeca;
        } else {
            ElementoLista temporario = new ElementoLista(jogo);
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
            cabeca.proximo = new ElementoLista(jogo);
            cauda = cabeca.proximo;
            cauda.anterior = cabeca;
        } else {
            ElementoLista temporario = new ElementoLista(jogo);
            cauda.proximo = temporario;
            temporario.anterior = cauda;
            cauda = temporario;
        }
    }

    // Adicionar em posição específica
    public void adicionarNaPosicao(RegistroJogo jogo, int posicao) {
        if (posicao < 0 || posicao > obterTamanho() + 1)
            return;
        if (posicao == 0)
            adicionarNoInicio(jogo);
        else if (posicao == obterTamanho())
            adicionarNoFinal(jogo);
        else {
            ElementoLista iterador = cabeca;
            for (int idx = 0; idx < posicao; idx++, iterador = iterador.proximo) {
            }
            ElementoLista temporario = new ElementoLista(jogo);
            temporario.proximo = iterador.proximo;
            iterador.proximo = temporario;
            temporario.anterior = iterador;
            temporario.proximo.anterior = temporario;
            iterador = temporario = null;
        }
    }

    // Obter tamanho da lista
    public int obterTamanho() {
        int tamanho = 0;
        for (ElementoLista iterador = cabeca.proximo; iterador != null; iterador = iterador.proximo) {
            tamanho++;
        }
        return tamanho;
    }

    // Extrair do início
    public RegistroJogo extrairDoInicio() throws Exception {
        if (cabeca == cauda)
            throw new Exception("Erro ao remover (vazia)!");
        exibirElementoRemovido(0);
        RegistroJogo elemento = cabeca.proximo.jogo;
        if (cabeca.proximo == cauda) {
            ElementoLista temporario = cabeca.proximo;
            cabeca.proximo = null;
            cauda.anterior = null;      
            cauda = cabeca;
            temporario = null;
        } else {
            ElementoLista temporario = cabeca.proximo;
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
        int tamanho = obterTamanho();
        exibirElementoRemovido(tamanho);
        RegistroJogo elemento = cauda.jogo;
        cauda.anterior.proximo = null;
        cauda = cauda.anterior;
        cauda.proximo = null;
        return elemento;
    }

    // Extrair de posição específica
    public RegistroJogo extrairDaPosicao(int posicao) throws Exception {
        posicao++;
        if (cauda == cabeca)
            throw new Exception("Erro ao remover (vazia)!");
        RegistroJogo elemento = null;
        int tamanho = obterTamanho();
        if (posicao >= tamanho || posicao < 0)
            throw new Exception("Erro ao remover (posição inválida)!");
        exibirElementoRemovido(posicao);
        if (posicao == 0)
            elemento = extrairDoInicio();
        else if (posicao == tamanho - 1)
            elemento = extrairDoFinal();
        else {
            ElementoLista iterador = cabeca;
            for (int idx = 0; idx < posicao; idx++, iterador = iterador.proximo) {
            }
            iterador.anterior.proximo = iterador.proximo;
            iterador.proximo.anterior = iterador.anterior;
            elemento = iterador.jogo;
            iterador = iterador.proximo = iterador.anterior = iterador = null;
        }
        return elemento;
    }

    // Exibir elemento removido
    public void exibirElementoRemovido(int posicao) {
        ElementoLista iterador = cabeca.proximo;
        int idx = 1;
        for (; iterador != null && idx < posicao; iterador = iterador.proximo, idx++){}
        System.out.println("(R) " + iterador.jogo.titulo);
    }

    // Exibir toda a lista
    public void mostrarConteudo() {
        int idx = 0;
        for (ElementoLista iterador = cabeca.proximo; iterador != null; iterador = iterador.proximo) {
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

    // Formatar array para exibição
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

public class listaAlocacaoSequencial {
    public static Scanner leitor;
    static int indiceAtual = 0;

    public static void main(String[] args) {
        leitor = new Scanner(System.in);
        String entrada = leitor.nextLine();
        String[] identificadores = new String[ConstantesConfig.LIMITE_IDENTIFICADORES];
        int totalIdentificadores = 0;

        while (!entrada.equals("FIM") && totalIdentificadores < identificadores.length) {
            identificadores[totalIdentificadores++] = entrada;
            entrada = leitor.nextLine();
        }

        ListaDuplamenteEncadeada listaJogos = ProcessadorJogos.construirLista(identificadores, totalIdentificadores);

        int quantidadeOperacoes = leitor.nextInt();
        leitor.nextLine();

        for (int i = 0; i < quantidadeOperacoes; i++) {
            indiceAtual = 0;
            String comandoCompleto = leitor.nextLine();
            
            String codigoOperacao = extrairCodigoOperacao(comandoCompleto);

            String posicaoString = "";
            if (codigoOperacao.charAt(1) == '*')
                posicaoString = extrairPosicao(comandoCompleto);

            String identificador = "";
            RegistroJogo jogoEncontrado = null;
            if (codigoOperacao.charAt(0) == 'I') {
                identificador = extrairIdentificadorFinal(comandoCompleto);
                if (!identificador.isEmpty())
                    jogoEncontrado = ProcessadorJogos.buscarJogoPorId(identificador);
            }

            switch (codigoOperacao) {
                case "II":
                    listaJogos.adicionarNoInicio(jogoEncontrado);
                    break;
                case "I*":
                    listaJogos.adicionarNaPosicao(jogoEncontrado, Integer.parseInt(posicaoString));
                    break;
                case "IF":
                    listaJogos.adicionarNoFinal(jogoEncontrado);
                    break;
                case "RI":
                    try {
                        listaJogos.extrairDoInicio();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "R*":
                    try {
                        listaJogos.extrairDaPosicao(Integer.parseInt(posicaoString));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "RF":
                    try {
                        listaJogos.extrairDoFinal();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
        listaJogos.mostrarConteudo();
    }

    // Extrair código da operação
    static String extrairCodigoOperacao(String entrada) {
        String operacao = "";
        while (indiceAtual < 2 && indiceAtual < entrada.length()) {
            operacao += entrada.charAt(indiceAtual);
            indiceAtual++;
        }
        indiceAtual++;
        return operacao;
    }

    // Extrair posição
    static String extrairPosicao(String entrada) {
        String posicao = "";
        while (indiceAtual < entrada.length() && entrada.charAt(indiceAtual) != ' ') {
            posicao += entrada.charAt(indiceAtual);
            indiceAtual++;
        }
        indiceAtual++;
        return posicao;
    }

    // Extrair identificador final
    static String extrairIdentificadorFinal(String entrada) {
        String id = "";
        while (indiceAtual < entrada.length()) {
            if (entrada.charAt(indiceAtual) != ' ') {
                id += entrada.charAt(indiceAtual);
                indiceAtual++;
            }
        }
        return id;
    }
}

// Processador de jogos do arquivo CSV
class ProcessadorJogos {
    public static Scanner leitor;
    static int ponteiro = 0;
    static String[] identificadoresBusca;
    static int tamanhoIdentificadores;
    static int tamanhoListaJogos;

    public static int obterTamanhoLista() {
        return tamanhoListaJogos;
    }

    static ListaDuplamenteEncadeada construirLista(String[] arrayIds, int tamanho) {
        ListaDuplamenteEncadeada lista = new ListaDuplamenteEncadeada();
        tamanhoListaJogos = 0;

        identificadoresBusca = arrayIds;
        tamanhoIdentificadores = tamanho;

        for (int idx = 0; idx < tamanho; idx++) {
            int indiceLocalizado = -1;

            try {
                java.io.File arquivoCsv = new java.io.File("/tmp/games.csv");
                if (!arquivoCsv.exists()) {
                    System.out.println("Arquivo 'games.csv' não encontrado!");
                    return lista;
                }

                InputStream fluxoEntrada = new FileInputStream(arquivoCsv);
                Scanner scannerArquivo = new Scanner(fluxoEntrada);

                if (scannerArquivo.hasNextLine())
                    scannerArquivo.nextLine();

                while (scannerArquivo.hasNextLine() && indiceLocalizado == -1) {
                    String linhaAtual = scannerArquivo.nextLine();
                    ponteiro = 0;

                    int idLinha = interpretarIdentificador(linhaAtual);
                    indiceLocalizado = verificarCorrespondenciaId(idLinha);

                    if (indiceLocalizado != -1) {
                        String nome = interpretarNome(linhaAtual);
                        String dataLancamento = interpretarDataLancamento(linhaAtual);
                        int proprietarios = interpretarProprietariosEstimados(linhaAtual);
                        float preco = interpretarPreco(linhaAtual);

                        String[] idiomas = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                        int qtdIdiomas = interpretarIdiomasSuportados(linhaAtual, idiomas);
                        int notaMeta = interpretarNotaMetacritic(linhaAtual);
                        float notaUsuario = interpretarAvaliacaoUsuario(linhaAtual);
                        int totalConquistas = interpretarConquistas(linhaAtual);

                        String[] editoras = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                        int qtdEditoras = interpretarArraysFinais(linhaAtual, editoras);
                        String[] desenvolvedores = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                        int qtdDesenvolvedores = interpretarArraysFinais(linhaAtual, desenvolvedores);
                        String[] categorias = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                        int qtdCategorias = interpretarArraysFinais(linhaAtual, categorias);
                        String[] generos = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                        int qtdGeneros = interpretarArraysFinais(linhaAtual, generos);
                        String[] tags = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                        int qtdTags = interpretarArraysFinais(linhaAtual, tags);

                        RegistroJogo jogoNovo = new RegistroJogo(idLinha, nome, dataLancamento, proprietarios, preco,
                                idiomas, qtdIdiomas, notaMeta, notaUsuario, totalConquistas,
                                editoras, qtdEditoras, desenvolvedores, qtdDesenvolvedores, categorias, qtdCategorias,
                                generos, qtdGeneros, tags, qtdTags);

                        lista.adicionarNoFinal(jogoNovo);
                        removerIdentificador(indiceLocalizado);
                    }
                }

                scannerArquivo.close();
                fluxoEntrada.close();

            } catch (Exception e) {
                System.out.println("Erro ao abrir ou ler o arquivo: " + e.getMessage());
            }
        }

        return lista;
    }

    static RegistroJogo buscarJogoPorId(String idBuscado) {
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
        leitor = new Scanner(fluxoEntrada);
        RegistroJogo jogoResultado = new RegistroJogo();
        boolean encontrado = false;

        if (leitor.hasNextLine())
            leitor.nextLine();

        while (leitor.hasNextLine() && encontrado == false) {
            String linhaAtual = leitor.nextLine();
            ponteiro = 0;

            int idLinha = interpretarIdentificador(linhaAtual);

            if (idLinha == Integer.parseInt(idBuscado)) {
                encontrado = true;
                String nome = interpretarNome(linhaAtual);
                String dataLancamento = interpretarDataLancamento(linhaAtual);
                int proprietarios = interpretarProprietariosEstimados(linhaAtual);
                float preco = interpretarPreco(linhaAtual);

                String[] idiomas = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                int qtdIdiomas = interpretarIdiomasSuportados(linhaAtual, idiomas);
                int notaMeta = interpretarNotaMetacritic(linhaAtual);
                float notaUsuario = interpretarAvaliacaoUsuario(linhaAtual);
                int totalConquistas = interpretarConquistas(linhaAtual);

                String[] editoras = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                int qtdEditoras = interpretarArraysFinais(linhaAtual, editoras);
                String[] desenvolvedores = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                int qtdDesenvolvedores = interpretarArraysFinais(linhaAtual, desenvolvedores);
                String[] categorias = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                int qtdCategorias = interpretarArraysFinais(linhaAtual, categorias);
                String[] generos = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                int qtdGeneros = interpretarArraysFinais(linhaAtual, generos);
                String[] tags = new String[ConstantesConfig.TAMANHO_ARRAY_INTERNO];
                int qtdTags = interpretarArraysFinais(linhaAtual, tags);

                jogoResultado = new RegistroJogo(idLinha, nome, dataLancamento, proprietarios, preco,
                        idiomas, qtdIdiomas, notaMeta, notaUsuario, totalConquistas,
                        editoras, qtdEditoras, desenvolvedores, qtdDesenvolvedores, categorias, qtdCategorias, generos,
                        qtdGeneros, tags, qtdTags);
            }
        }
        leitor.close();
        return jogoResultado;
    }

    static int verificarCorrespondenciaId(int id) {
        if (Integer.parseInt(identificadoresBusca[0]) == id)
            return 0;
        else
            return -1;
    }

    static void removerIdentificador(int indice) {
        if (indice >= 0 && indice < tamanhoIdentificadores) {
            for (int j = indice; j < tamanhoIdentificadores - 1; j++) {
                identificadoresBusca[j] = identificadoresBusca[j + 1];
            }
            identificadoresBusca[tamanhoIdentificadores - 1] = null;
            tamanhoIdentificadores--;
        }
    }

    static int interpretarIdentificador(String linha) {
        int id = 0;
        while (ponteiro < linha.length() && Character.isDigit(linha.charAt(ponteiro))) {
            id = id * 10 + (linha.charAt(ponteiro) - '0');
            ponteiro++;
        }
        return id;
    }

    static String interpretarNome(String linha) {
        String nome = "";
        while (linha.charAt(ponteiro) != ',' && ponteiro < linha.length()) {
            ponteiro++;
        }
        ponteiro++;
        while (linha.charAt(ponteiro) != ',' && ponteiro < linha.length()) {
            nome += linha.charAt(ponteiro);
            ponteiro++;
        }
        return nome;
    }

    static String interpretarDataLancamento(String linha) {
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != '"') {
            ponteiro++;
        }
        ponteiro++;
        String dia = "", mes = "", ano = "";

        for (int i = 0; ponteiro < linha.length() && i < 3; i++) {
            mes += linha.charAt(ponteiro);
            ponteiro++;
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

        while (ponteiro < linha.length() && !Character.isDigit(linha.charAt(ponteiro)) && linha.charAt(ponteiro) != ',') {
            ponteiro++;
        }

        while (ponteiro < linha.length() && Character.isDigit(linha.charAt(ponteiro))) {
            dia += linha.charAt(ponteiro);
            ponteiro++;
        }

        while (ponteiro < linha.length() && !Character.isDigit(linha.charAt(ponteiro))) {
            ponteiro++;
        }

        while (ponteiro < linha.length() && linha.charAt(ponteiro) != '"') {
            ano += linha.charAt(ponteiro);
            ponteiro++;
        }
        if (dia.isEmpty()) dia = "01";
        if (mes.isEmpty()) mes = "01";
        if (ano.isEmpty()) ano = "0000";
        return dia + "/" + mes + "/" + ano;
    }

    static int interpretarProprietariosEstimados(String linha) {
        int proprietarios = 0;
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',') {
            ponteiro++;
        }
        ponteiro++;
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',') {
            proprietarios = proprietarios * 10 + (linha.charAt(ponteiro) - '0');
            ponteiro++;
        }
        return proprietarios;
    }

    static float interpretarPreco(String linha) {
        String preco = "";
        while (ponteiro < linha.length() && !Character.isDigit(linha.charAt(ponteiro)) && linha.charAt(ponteiro) != 'F') {
            ponteiro++;
        }
        while (ponteiro < linha.length() && (Character.isDigit(linha.charAt(ponteiro)) || linha.charAt(ponteiro) == '.')) {
            preco += linha.charAt(ponteiro);
            ponteiro++;
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

    static int interpretarIdiomasSuportados(String linha, String[] idiomas) {
        int contador = 0;
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != ']' && contador < idiomas.length) {
            String idioma = "";
            while (ponteiro < linha.length() && !Character.isAlphabetic(linha.charAt(ponteiro))) {
                ponteiro++;
            }
            while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',' && linha.charAt(ponteiro) != ']') {
                if (Character.isAlphabetic(linha.charAt(ponteiro)) || linha.charAt(ponteiro) == ' '
                        || linha.charAt(ponteiro) == '-') {
                    idioma += linha.charAt(ponteiro);
                }
                ponteiro++;
            }
            idiomas[contador++] = idioma;
        }
        return contador;
    }

    static int interpretarNotaMetacritic(String linha) {
        String nota = "";
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',') {
            ponteiro++;
        }
        ponteiro++;
        while (ponteiro < linha.length() && Character.isDigit(linha.charAt(ponteiro))) {
            nota += linha.charAt(ponteiro);
            ponteiro++;
        }
        if (nota.isEmpty())
            return -1;
        else
            return Integer.parseInt(nota);
    }

    static float interpretarAvaliacaoUsuario(String linha) {
        String avaliacao = "";
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',') {
            ponteiro++;
        }
        ponteiro++;
        while (ponteiro < linha.length() && (Character.isDigit(linha.charAt(ponteiro)) || linha.charAt(ponteiro) == '.')) {
            avaliacao += linha.charAt(ponteiro);
            ponteiro++;
        }
        if (avaliacao.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(avaliacao);
    }

    static int interpretarConquistas(String linha) {
        String conquistas = "";
        while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',') {
            ponteiro++;
        }
        ponteiro++;
        while (ponteiro < linha.length() && (Character.isDigit(linha.charAt(ponteiro)) || linha.charAt(ponteiro) == '.')) {
            conquistas += linha.charAt(ponteiro);
            ponteiro++;
        }
        if (conquistas.isEmpty())
            return -1;
        else
            return Integer.parseInt(conquistas);
    }

    static int interpretarArraysFinais(String linha, String[] array) {
        int contador = 0;
        boolean entreAspas = false;
        while (ponteiro < linha.length() && !Character.isAlphabetic(linha.charAt(ponteiro))
                && !Character.isDigit(linha.charAt(ponteiro))) {
            if (linha.charAt(ponteiro) == '"')
                entreAspas = true;
            ponteiro++;
        }
        if (entreAspas) {
            while (ponteiro < linha.length() && linha.charAt(ponteiro) != '"' && contador < array.length) {
                String fragmento = "";
                while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',' && linha.charAt(ponteiro) != '"') {
                    fragmento += linha.charAt(ponteiro);
                    ponteiro++;
                }
                while (ponteiro < linha.length() && !Character.isAlphabetic(linha.charAt(ponteiro))
                        && !Character.isDigit(linha.charAt(ponteiro))
                        && linha.charAt(ponteiro) != '"') {
                    ponteiro++;
                }
                array[contador++] = fragmento;
            }
            ponteiro++;
        } else {
            if (contador < array.length) {
                String fragmento = "";
                while (ponteiro < linha.length() && linha.charAt(ponteiro) != ',') {
                    fragmento += linha.charAt(ponteiro);
                    ponteiro++;
                }
                array[contador++] = fragmento;
            }
        }
        if (ponteiro < linha.length() && linha.charAt(ponteiro) == ',') {
            ponteiro++;
        }
        return contador;
    }
}