import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

class LimitesConfigurados {
    public static final int CAPACIDADE_JOGOS = 500;
    public static final int DIMENSAO_ARRAY = 50;
    public static final int TOTAL_IDENTIFICADORES = 100;
}

class DadosJogo {
    int identificador;
    String titulo;
    String dataPublicacao;
    int proprietariosEstimados;
    float valorPreco;
    String[] linguagensDisponiveis;
    int totalLinguagens;
    int notacaoMetacritic;
    float pontuacaoUsuarios;
    int totalConquistas;
    String[] publicadoras;
    int quantidadePublicadoras;
    String[] criadoras;
    int quantidadeCriadoras;
    String[] classificacoes;
    int quantidadeClassificacoes;
    String[] tiposGenero;
    int quantidadeGeneros;
    String[] marcadores;
    int quantidadeMarcadores;

    DadosJogo() {
        this.identificador = 0;
        this.titulo = "";
        this.dataPublicacao = "";
        this.proprietariosEstimados = 0;
        this.valorPreco = 0.0f;
        this.linguagensDisponiveis = new String[LimitesConfigurados.DIMENSAO_ARRAY];
        this.totalLinguagens = 0;
        this.notacaoMetacritic = -1;
        this.pontuacaoUsuarios = -1.0f;
        this.totalConquistas = 0;
        this.publicadoras = new String[LimitesConfigurados.DIMENSAO_ARRAY];
        this.quantidadePublicadoras = 0;
        this.criadoras = new String[LimitesConfigurados.DIMENSAO_ARRAY];
        this.quantidadeCriadoras = 0;
        this.classificacoes = new String[LimitesConfigurados.DIMENSAO_ARRAY];
        this.quantidadeClassificacoes = 0;
        this.tiposGenero = new String[LimitesConfigurados.DIMENSAO_ARRAY];
        this.quantidadeGeneros = 0;
        this.marcadores = new String[LimitesConfigurados.DIMENSAO_ARRAY];
        this.quantidadeMarcadores = 0;
    }

    DadosJogo(int identificador, String titulo, String dataPublicacao, int proprietariosEstimados, float valorPreco,
            String[] linguagensDisponiveis, int totalLinguagens, int notacaoMetacritic, float pontuacaoUsuarios,
            int totalConquistas,
            String[] publicadoras, int quantidadePublicadoras, String[] criadoras, int quantidadeCriadoras,
            String[] classificacoes, int quantidadeClassificacoes, String[] tiposGenero, int quantidadeGeneros, String[] marcadores, int quantidadeMarcadores) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.dataPublicacao = dataPublicacao;
        this.proprietariosEstimados = proprietariosEstimados;
        this.valorPreco = valorPreco;

        this.linguagensDisponiveis = linguagensDisponiveis;
        this.totalLinguagens = totalLinguagens;
        this.publicadoras = publicadoras;
        this.quantidadePublicadoras = quantidadePublicadoras;
        this.criadoras = criadoras;
        this.quantidadeCriadoras = quantidadeCriadoras;
        this.classificacoes = classificacoes;
        this.quantidadeClassificacoes = quantidadeClassificacoes;
        this.tiposGenero = tiposGenero;
        this.quantidadeGeneros = quantidadeGeneros;
        this.marcadores = marcadores;
        this.quantidadeMarcadores = quantidadeMarcadores;

        this.notacaoMetacritic = notacaoMetacritic;
        this.pontuacaoUsuarios = pontuacaoUsuarios;
        this.totalConquistas = totalConquistas;
    }
}

class NodoTexto {
    DadosJogo registro;
    NodoTexto ramoEsquerdo, ramoDireito;

    NodoTexto(DadosJogo registro) {
        this.registro = registro;
        ramoEsquerdo = ramoDireito = null;
    }
}

class EstruturaTexto {

    public NodoTexto adicionarNo(DadosJogo registro, NodoTexto nodoAtual) throws Exception {
        if (nodoAtual == null) {
            nodoAtual = new NodoTexto(registro);
        } else if (registro.titulo.compareTo(nodoAtual.registro.titulo) < 0) {
            nodoAtual.ramoEsquerdo = adicionarNo(registro, nodoAtual.ramoEsquerdo);
        } else if (registro.titulo.compareTo(nodoAtual.registro.titulo) > 0) {
            nodoAtual.ramoDireito = adicionarNo(registro, nodoAtual.ramoDireito);
        } else {
            throw new Exception("Elemento já existe na segunda árvore.");
        }
        return nodoAtual;
    }

    public boolean localizarElemento(String termoBusca, NodoTexto nodoAtual) throws Exception {
        if (nodoAtual == null) {
            return false;
        }

        if (termoBusca.equals(nodoAtual.registro.titulo)) {
            System.out.println(" SIM");
            return true;
        } else if (termoBusca.compareTo(nodoAtual.registro.titulo) < 0) {
            ;
            System.out.print("esq ");
            return localizarElemento(termoBusca, nodoAtual.ramoEsquerdo);
        } else {
            System.out.print("dir ");
            return localizarElemento(termoBusca, nodoAtual.ramoDireito);
        }
    }
}

class NodoNumerico {
    int valorChave;
    NodoNumerico ramoEsquerdo, ramoDireito;
    NodoTexto raizSubarvore;

    NodoNumerico(int valorChave) {
        this.valorChave = valorChave;
        ramoEsquerdo = ramoDireito = null;
        raizSubarvore = null;
    }
}

class ArvoreHierarquica {
    private NodoNumerico elementoRaiz;
    private EstruturaTexto estruturaSecundaria;

    public ArvoreHierarquica() {
        estruturaSecundaria = new EstruturaTexto();
        configurarArvoreInicial();
    }

    private void configurarArvoreInicial() {
        int[] valoresChave = { 7, 3, 11, 1, 5, 9, 13, 0, 2, 4, 6, 8, 10, 12, 14 };
        for (int chave : valoresChave) {
            elementoRaiz = inserirNodoNumerico(chave, elementoRaiz);
        }
    }

    private NodoNumerico inserirNodoNumerico(int valorChave, NodoNumerico nodoAtual) {
        if (nodoAtual == null) {
            nodoAtual = new NodoNumerico(valorChave);
        } else if (valorChave < nodoAtual.valorChave) {
            nodoAtual.ramoEsquerdo = inserirNodoNumerico(valorChave, nodoAtual.ramoEsquerdo);
        } else if (valorChave > nodoAtual.valorChave) {
            nodoAtual.ramoDireito = inserirNodoNumerico(valorChave, nodoAtual.ramoDireito);
        }
        return nodoAtual;
    }

    public void adicionarRegistro(DadosJogo registro) throws Exception {
        int chaveCalculada = registro.proprietariosEstimados % 15;
        adicionarRegistro(registro, chaveCalculada, elementoRaiz);
    }

    private void adicionarRegistro(DadosJogo registro, int chaveCalculada, NodoNumerico nodoAtual) throws Exception {
        if (nodoAtual == null) {
            throw new Exception("Chave Estimated Owners Mod 15 não encontrada na primeira árvore.");
        } else if (chaveCalculada == nodoAtual.valorChave) {
            nodoAtual.raizSubarvore = estruturaSecundaria.adicionarNo(registro, nodoAtual.raizSubarvore);
        } else if (chaveCalculada < nodoAtual.valorChave) {
            adicionarRegistro(registro, chaveCalculada, nodoAtual.ramoEsquerdo);
        } else {
            adicionarRegistro(registro, chaveCalculada, nodoAtual.ramoDireito);
        }
    }

    public void buscarRegistro(String tituloJogo) throws Exception {
        if (elementoRaiz != null) {
            System.out.print("=> " + tituloJogo + " => raiz ");
        }
        boolean resultadoBusca = percorrerArvoreInicial(tituloJogo, elementoRaiz);
        if (!resultadoBusca) {
            System.out.println(" NAO");
        }
    }

    private boolean percorrerArvoreInicial(String tituloJogo, NodoNumerico nodoAtual) throws Exception {
        if (nodoAtual == null) {

            return false;
        }
        System.out.print(" ESQ ");
        if (percorrerArvoreInicial(tituloJogo, nodoAtual.ramoEsquerdo)) {
            return true;
        }
        if (nodoAtual.raizSubarvore != null) {
            if (estruturaSecundaria.localizarElemento(tituloJogo, nodoAtual.raizSubarvore)) {
                return true;
            }
        }
        System.out.print(" DIR ");
        if (percorrerArvoreInicial(tituloJogo, nodoAtual.ramoDireito)) {
            return true;
        }

        return false;
    }
}

public class ADeArvore {
    public static Scanner scannerEntrada;

    public static void main(String[] args) {
        scannerEntrada = new Scanner(System.in);

        String linhaLida = scannerEntrada.nextLine();
        String identificadoresLista[] = new String[2000];
        int dimensaoLista = 0;
        
        for (; !linhaLida.equals("FIM"); dimensaoLista++) {
            identificadoresLista[dimensaoLista] = linhaLida;
            linhaLida = scannerEntrada.nextLine();
        }
        
        ArvoreHierarquica estruturaHierarquica = ManipuladorRegistros.iniciarEstrutura(identificadoresLista, dimensaoLista);

        linhaLida = scannerEntrada.nextLine();
        while (!linhaLida.equals("FIM")) {
            try {
                estruturaHierarquica.buscarRegistro(linhaLida);
            } catch (Exception excecao) {
                System.err.println("Erro: " + excecao.getMessage());
            }
            linhaLida = scannerEntrada.nextLine();
        }
        scannerEntrada.close();
    }
}

class ManipuladorRegistros {
    public static Scanner scannerArquivo;
    static int posicaoLeitura = 0;
    static String[] arrayIds;
    static int tamanhoArrayIds;

    static ArvoreHierarquica iniciarEstrutura(String[] vetorIdentificadores, int dimensaoVetor) {
        ArvoreHierarquica estruturaHierarquica = new ArvoreHierarquica();

        arrayIds = vetorIdentificadores;
        tamanhoArrayIds = dimensaoVetor;

        for (int indiceIteracao = 0; indiceIteracao < dimensaoVetor; indiceIteracao++) {
            int indiceBuscado = -1;

            try {
                java.io.File documentoCsv = new java.io.File("/tmp/games.csv");
                if (!documentoCsv.exists()) {
                    System.out.println("Arquivo 'games.csv' não encontrado!");
                    return estruturaHierarquica;
                }

                InputStream fluxoLeitura = new FileInputStream(documentoCsv);
                Scanner leitorCsv = new Scanner(fluxoLeitura);

                if (leitorCsv.hasNextLine())
                    leitorCsv.nextLine();

                while (leitorCsv.hasNextLine() && indiceBuscado == -1) {
                    String linhaProcessada = leitorCsv.nextLine();
                    posicaoLeitura = 0;

                    int idLinha = obterIdentificador(linhaProcessada);
                    indiceBuscado = compararIdentificador(idLinha);

                    if (indiceBuscado != -1) {
                        String nomeTitulo = obterNome(linhaProcessada);
                        String dataLancamento = obterDataLancamento(linhaProcessada);
                        int quantidadeProprietarios = obterProprietariosEstimados(linhaProcessada);
                        float custoJogo = obterPreco(linhaProcessada);

                        String[] idiomasArray = new String[LimitesConfigurados.DIMENSAO_ARRAY];
                        int contagemIdiomas = obterIdiomasSuportados(linhaProcessada, idiomasArray);
                        int pontuacaoMeta = obterPontuacaoMetacritic(linhaProcessada);
                        float avaliacaoMedia = obterAvaliacaoUsuarios(linhaProcessada);
                        int quantidadeAchievements = obterTotalConquistas(linhaProcessada);

                        String[] editoresArray = new String[LimitesConfigurados.DIMENSAO_ARRAY];
                        int contagemEditores = obterArraysFinais(linhaProcessada, editoresArray);
                        String[] desenvolvedoresArray = new String[LimitesConfigurados.DIMENSAO_ARRAY];
                        int contagemDesenvolvedores = obterArraysFinais(linhaProcessada, desenvolvedoresArray);
                        String[] categoriasArray = new String[LimitesConfigurados.DIMENSAO_ARRAY];
                        int contagemCategorias = obterArraysFinais(linhaProcessada, categoriasArray);
                        String[] generosArray = new String[LimitesConfigurados.DIMENSAO_ARRAY];
                        int contagemGeneros = obterArraysFinais(linhaProcessada, generosArray);
                        String[] tagsArray = new String[LimitesConfigurados.DIMENSAO_ARRAY];
                        int contagemTags = obterArraysFinais(linhaProcessada, tagsArray);

                        DadosJogo registroCompleto = new DadosJogo(idLinha, nomeTitulo, dataLancamento, quantidadeProprietarios, custoJogo,
                                idiomasArray, contagemIdiomas, pontuacaoMeta, avaliacaoMedia, quantidadeAchievements,
                                editoresArray, contagemEditores, desenvolvedoresArray, contagemDesenvolvedores, categoriasArray, contagemCategorias,
                                generosArray, contagemGeneros, tagsArray, contagemTags);

                        eliminarIdentificador(indiceBuscado);
                        estruturaHierarquica.adicionarRegistro(registroCompleto);
                    }
                }

                leitorCsv.close();
                fluxoLeitura.close();

            } catch (Exception erro) {
                System.out.println("Erro ao abrir ou ler o arquivo: " + erro.getMessage());
            }
        }

        return estruturaHierarquica;
    }

    static int compararIdentificador(int idVerificar) {
        for (int posicao = 0; posicao < tamanhoArrayIds; posicao++) {
            if (tamanhoArrayIds > 0 && Integer.parseInt(arrayIds[0]) == idVerificar)
                return 0;
        }
        return -1;
    }

    static void eliminarIdentificador(int posicaoRemocao) {
        if (posicaoRemocao == 0 && tamanhoArrayIds > 0) {
            for (int deslocamento = posicaoRemocao; deslocamento < tamanhoArrayIds - 1; deslocamento++) {
                arrayIds[deslocamento] = arrayIds[deslocamento + 1];
            }
            arrayIds[tamanhoArrayIds - 1] = null;
            tamanhoArrayIds--;
        }
    }

    static int obterIdentificador(String conteudo) {
        int numeroIdentificador = 0;
        while (posicaoLeitura < conteudo.length() && Character.isDigit(conteudo.charAt(posicaoLeitura))) {
            numeroIdentificador = numeroIdentificador * 10 + (conteudo.charAt(posicaoLeitura) - '0');
            posicaoLeitura++;
        }
        return numeroIdentificador;
    }

    static String obterNome(String conteudo) {
        String textoNome = "";
        while (conteudo.charAt(posicaoLeitura) != ',' && posicaoLeitura < conteudo.length()) {
            posicaoLeitura++;
        }
        posicaoLeitura++;
        
        if (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) == '"') {
            posicaoLeitura++;
            while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != '"') {
                textoNome += conteudo.charAt(posicaoLeitura);
                posicaoLeitura++;
            }
            if (posicaoLeitura < conteudo.length())
                posicaoLeitura++;
        } else {
            while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
                textoNome += conteudo.charAt(posicaoLeitura);
                posicaoLeitura++;
            }
        }
        return textoNome;
    }

    static String obterDataLancamento(String conteudo) {
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != '"') {
            posicaoLeitura++;
        }
        if (posicaoLeitura < conteudo.length())
            posicaoLeitura++;

        String diaTexto = "", mesTexto = "", anoTexto = "";
        
        for (int i = 0; posicaoLeitura < conteudo.length() && i < 3; i++) {
            mesTexto += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        mesTexto = mesTexto.trim();
        switch (mesTexto) {
            case "Jan": mesTexto = "01"; break;
            case "Feb": mesTexto = "02"; break;
            case "Mar": mesTexto = "03"; break;
            case "Apr": mesTexto = "04"; break;
            case "May": mesTexto = "05"; break;
            case "Jun": mesTexto = "06"; break;
            case "Jul": mesTexto = "07"; break;
            case "Aug": mesTexto = "08"; break;
            case "Sep": mesTexto = "09"; break;
            case "Oct": mesTexto = "10"; break;
            case "Nov": mesTexto = "11"; break;
            case "Dec": mesTexto = "12"; break;
            default: break;
        }
        
        while (posicaoLeitura < conteudo.length() && !Character.isDigit(conteudo.charAt(posicaoLeitura)) && conteudo.charAt(posicaoLeitura) != ',') {
            posicaoLeitura++;
        }
        
        while (posicaoLeitura < conteudo.length() && Character.isDigit(conteudo.charAt(posicaoLeitura))) {
            diaTexto += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        
        while (posicaoLeitura < conteudo.length() && !Character.isDigit(conteudo.charAt(posicaoLeitura))) {
            posicaoLeitura++;
        }
        
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != '"') {
            anoTexto += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        if (diaTexto.isEmpty())
            diaTexto = "01";
        if (mesTexto.isEmpty())
            mesTexto = "01";
        if (anoTexto.isEmpty())
            anoTexto = "0000";
        return diaTexto + "/" + mesTexto + "/" + anoTexto;
    }

    static int obterProprietariosEstimados(String conteudo) {
        int numeroProprietarios = 0;
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            posicaoLeitura++;
        }
        posicaoLeitura++;
        StringBuilder construtorNumero = new StringBuilder();
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            if (Character.isDigit(conteudo.charAt(posicaoLeitura))) {
                construtorNumero.append(conteudo.charAt(posicaoLeitura));
            }
            posicaoLeitura++;
        }
        try {
            numeroProprietarios = Integer.parseInt(construtorNumero.toString());
        } catch (NumberFormatException erro) {
            numeroProprietarios = 0;
        }
        return numeroProprietarios;
    }

    static float obterPreco(String conteudo) {
        String textoPreco = "";
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',' && conteudo.charAt(posicaoLeitura) != 'F'
                && !Character.isDigit(conteudo.charAt(posicaoLeitura))) {
            posicaoLeitura++;
        }
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            textoPreco += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        textoPreco = textoPreco.trim();
        if (textoPreco.isEmpty() || textoPreco.toLowerCase().contains("free to play")) {
            return 0.0f;
        }
        textoPreco = textoPreco.replaceAll("[^0-9.]", "");
        try {
            return Float.parseFloat(textoPreco);
        } catch (NumberFormatException erro) {
            return 0.0f;
        }
    }

    static int obterIdiomasSuportados(String conteudo, String[] arrayIdiomas) {
        int totalIdiomas = 0;
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ']' && totalIdiomas < arrayIdiomas.length) {
            String textoLingua = "";
            while (posicaoLeitura < conteudo.length() && !Character.isAlphabetic(conteudo.charAt(posicaoLeitura))) {
                posicaoLeitura++;
            }
            while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',' && conteudo.charAt(posicaoLeitura) != ']') {
                if (conteudo.charAt(posicaoLeitura) != '"') {
                    textoLingua += conteudo.charAt(posicaoLeitura);
                }
                posicaoLeitura++;
            }
            arrayIdiomas[totalIdiomas++] = textoLingua.trim();
        }
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            posicaoLeitura++;
        }
        if (posicaoLeitura < conteudo.length())
            posicaoLeitura++;
        return totalIdiomas;
    }

    static int obterPontuacaoMetacritic(String conteudo) {
        String textoNota = "";
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            posicaoLeitura++;
        }
        posicaoLeitura++;
        while (posicaoLeitura < conteudo.length() && Character.isDigit(conteudo.charAt(posicaoLeitura))) {
            textoNota += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        if (textoNota.isEmpty())
            return -1;
        else
            return Integer.parseInt(textoNota);
    }

    static float obterAvaliacaoUsuarios(String conteudo) {
        String textoAvaliacao = "";
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            posicaoLeitura++;
        }
        posicaoLeitura++;
        while (posicaoLeitura < conteudo.length() && (Character.isDigit(conteudo.charAt(posicaoLeitura)) || conteudo.charAt(posicaoLeitura) == '.')) {
            textoAvaliacao += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        if (textoAvaliacao.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(textoAvaliacao);
    }

    static int obterTotalConquistas(String conteudo) {
        String textoConquistas = "";
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
            posicaoLeitura++;
        }
        posicaoLeitura++;
        while (posicaoLeitura < conteudo.length() && (Character.isDigit(conteudo.charAt(posicaoLeitura)) || conteudo.charAt(posicaoLeitura) == '.')) {
            textoConquistas += conteudo.charAt(posicaoLeitura);
            posicaoLeitura++;
        }
        if (textoConquistas.isEmpty())
            return 0;
        else
            return Integer.parseInt(textoConquistas);
    }

    static int obterArraysFinais(String conteudo, String[] arrayCategoria) {
        int totalElementos = 0;
        while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != '"') {
            posicaoLeitura++;
        }

        if (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) == '"') {
            posicaoLeitura++;
            while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != '"' && totalElementos < arrayCategoria.length) {
                String segmentoTexto = "";
                while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',' && conteudo.charAt(posicaoLeitura) != '"') {
                    segmentoTexto += conteudo.charAt(posicaoLeitura);
                    posicaoLeitura++;
                }
                arrayCategoria[totalElementos++] = segmentoTexto.trim();
                if (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) == ',') {
                    posicaoLeitura++;
                }
            }
            if (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) == '"') {
                posicaoLeitura++;
            }
        } else {
            if (totalElementos < arrayCategoria.length) {
                String segmentoTexto = "";
                while (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) != ',') {
                    segmentoTexto += conteudo.charAt(posicaoLeitura);
                    posicaoLeitura++;
                }
                arrayCategoria[totalElementos++] = segmentoTexto;
            }
        }
        if (posicaoLeitura < conteudo.length() && conteudo.charAt(posicaoLeitura) == ',') {
            posicaoLeitura++;
        }
        return totalElementos;
    }
}