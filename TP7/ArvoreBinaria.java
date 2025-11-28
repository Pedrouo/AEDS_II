import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class ConstantesLimites {
    public static final int LIMITE_JOGOS = 500;
    public static final int TAMANHO_ARRAY_INTERNO = 50;
    public static final int QUANTIDADE_IDS = 100;
}

class RegistroJogo {
    int identificador;
    String titulo;
    String dataLancamento;
    int proprietariosEstimados;
    float valorPreco;
    String[] idiomasSuportados;
    int quantidadeIdiomas;
    int pontuacaoMetacritic;
    float avaliacaoUsuario;
    int conquistas;
    String[] editoras;
    int quantidadeEditoras;
    String[] desenvolvedoras;
    int quantidadeDesenvolvedoras;
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
        this.valorPreco = 0.0f;
        this.idiomasSuportados = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
        this.quantidadeIdiomas = 0;
        this.pontuacaoMetacritic = -1;
        this.avaliacaoUsuario = -1.0f;
        this.conquistas = 0;
        this.editoras = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
        this.quantidadeEditoras = 0;
        this.desenvolvedoras = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
        this.quantidadeDesenvolvedoras = 0;
        this.categorias = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
        this.quantidadeCategorias = 0;
        this.generos = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
        this.quantidadeGeneros = 0;
        this.etiquetas = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
        this.quantidadeEtiquetas = 0;
    }

    RegistroJogo(int identificador, String titulo, String dataLancamento, int proprietariosEstimados, float valorPreco,
            String[] idiomasSuportados, int quantidadeIdiomas, int pontuacaoMetacritic, float avaliacaoUsuario,
            int conquistas,
            String[] editoras, int quantidadeEditoras, String[] desenvolvedoras, int quantidadeDesenvolvedoras,
            String[] categorias, int quantidadeCategorias, String[] generos, int quantidadeGeneros, String[] etiquetas, int quantidadeEtiquetas) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.dataLancamento = dataLancamento;
        this.proprietariosEstimados = proprietariosEstimados;
        this.valorPreco = valorPreco;

        this.idiomasSuportados = idiomasSuportados;
        this.quantidadeIdiomas = quantidadeIdiomas;
        this.editoras = editoras;
        this.quantidadeEditoras = quantidadeEditoras;
        this.desenvolvedoras = desenvolvedoras;
        this.quantidadeDesenvolvedoras = quantidadeDesenvolvedoras;
        this.categorias = categorias;
        this.quantidadeCategorias = quantidadeCategorias;
        this.generos = generos;
        this.quantidadeGeneros = quantidadeGeneros;
        this.etiquetas = etiquetas;
        this.quantidadeEtiquetas = quantidadeEtiquetas;

        this.pontuacaoMetacritic = pontuacaoMetacritic;
        this.avaliacaoUsuario = avaliacaoUsuario;
        this.conquistas = conquistas;
    }
}

class ElementoNo {
    RegistroJogo registro;
    ElementoNo subarvoreEsquerda, subarvoreDireita;

    ElementoNo(RegistroJogo registro) {
        this.registro = registro;
        subarvoreEsquerda = subarvoreDireita = null;
    }
}

class EstruturaArvoreBinaria {
    private ElementoNo noPrincipal;

    public void adicionarElemento(RegistroJogo registro) throws Exception {
        noPrincipal = adicionarElemento(registro, noPrincipal);
    }

    public ElementoNo adicionarElemento(RegistroJogo registro, ElementoNo noAtual) throws Exception {
        if (noAtual == null) {
            noAtual = new ElementoNo(registro);
        } else if (registro.titulo.equals(noAtual.registro.titulo)) {
            throw new Exception("Elemento já existe na árvore");
        } else if (registro.titulo.compareTo(noAtual.registro.titulo) < 0) {
            noAtual.subarvoreEsquerda = adicionarElemento(registro, noAtual.subarvoreEsquerda);
        } else if (registro.titulo.compareTo(noAtual.registro.titulo) > 0) {
            noAtual.subarvoreDireita = adicionarElemento(registro, noAtual.subarvoreDireita);
        } else {
            throw new Exception("Erro ao inserir na árvore!");
        }
        return noAtual;
    }

    public void buscarElemento(String termoBusca) throws Exception {
        if (noPrincipal != null) {
            ArvoreBinaria.escritorLog.print(termoBusca + ": =>raiz  ");
            System.out.print(termoBusca + ": =>raiz  ");
        }
        buscarElemento(termoBusca, noPrincipal);
    }

    public void buscarElemento(String termoBusca, ElementoNo noAtual) throws Exception {
        if (noAtual == null) {
            ArvoreBinaria.escritorLog.print(termoBusca + "NAO");
            System.out.println("NAO");
        } else if (termoBusca.equals(noAtual.registro.titulo)) {
            ArvoreBinaria.escritorLog.print(termoBusca + "SIM");
            System.out.println("SIM");
        } else if (termoBusca.compareTo(noAtual.registro.titulo) < 0) {
            ArvoreBinaria.escritorLog.print(termoBusca + "esq ");
            System.out.print("esq ");
            buscarElemento(termoBusca, noAtual.subarvoreEsquerda);
        } else {
            ArvoreBinaria.escritorLog.print(termoBusca + "esq ");
            System.out.print("dir ");
            buscarElemento(termoBusca, noAtual.subarvoreDireita);
        }
    }
}

public class ArvoreBinaria {
    public static Scanner leitorEntrada;
    public static PrintWriter escritorLog;

    public static void main(String[] args) {
        leitorEntrada = new Scanner(System.in);
        
        try {
            escritorLog = new PrintWriter(new FileWriter("876881_arvoreBinaria.txt"));
        } catch (IOException excecao) {
            System.err.println("Erro ao criar o arquivo de log: " + excecao.getMessage());
            return;
        }
        
        String linhaEntrada = leitorEntrada.nextLine();
        String listaIdentificadores[] = new String[2000];
        int tamanhoLista = 0;
        
        for (; !linhaEntrada.equals("FIM"); tamanhoLista++) {
            listaIdentificadores[tamanhoLista] = linhaEntrada;
            linhaEntrada = leitorEntrada.nextLine();
        }
        
        EstruturaArvoreBinaria estruturaArvore = ProcessadorJogos.construirArvore(listaIdentificadores, tamanhoLista);
        
        linhaEntrada = leitorEntrada.nextLine();
        while (!linhaEntrada.equals("FIM")) {
            try {
                estruturaArvore.buscarElemento(linhaEntrada);
            } catch (Exception excecao) {
                System.err.println("Erro: " + excecao.getMessage());
            }
            linhaEntrada = leitorEntrada.nextLine();
        }
        
        leitorEntrada.close();
        escritorLog.close();
    }
}

class ProcessadorJogos {
    public static Scanner leitorArquivo;
    static int indiceLeitura = 0;
    static String[] arrayIdentificadores;
    static int dimensaoArray;

    static EstruturaArvoreBinaria construirArvore(String[] vetorIds, int tamanhoVetor) {
        EstruturaArvoreBinaria estruturaArvore = new EstruturaArvoreBinaria();

        arrayIdentificadores = vetorIds;
        dimensaoArray = tamanhoVetor;

        for (int indiceProcessamento = 0; indiceProcessamento < tamanhoVetor; indiceProcessamento++) {
            int posicaoEncontrada = -1;

            try {
                java.io.File arquivoDados = new java.io.File("/tmp/games.csv");
                if (!arquivoDados.exists()) {
                    System.out.println("Arquivo 'games.csv' não encontrado!");
                    return estruturaArvore;
                }

                InputStream fluxoEntrada = new FileInputStream(arquivoDados);
                Scanner scannerArquivo = new Scanner(fluxoEntrada);

                if (scannerArquivo.hasNextLine())
                    scannerArquivo.nextLine();

                while (scannerArquivo.hasNextLine() && posicaoEncontrada == -1) {
                    String linhaAtual = scannerArquivo.nextLine();
                    indiceLeitura = 0;

                    int idAtual = extrairIdentificador(linhaAtual);
                    posicaoEncontrada = verificarIdCorrespondente(idAtual);

                    if (posicaoEncontrada != -1) {
                        String nomeJogo = extrairNome(linhaAtual);
                        String dataPublicacao = extrairData(linhaAtual);
                        int proprietarios = extrairProprietarios(linhaAtual);
                        float preco = extrairValor(linhaAtual);

                        String[] linguas = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
                        int totalLinguas = extrairIdiomas(linhaAtual, linguas);
                        int notaMetacritic = extrairNotaMetacritic(linhaAtual);
                        float notaUsuarios = extrairNotaUsuarios(linhaAtual);
                        int totalConquistas = extrairConquistas(linhaAtual);

                        String[] publicadoras = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
                        int totalPublicadoras = extrairArrayFinal(linhaAtual, publicadoras);
                        String[] criadoras = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
                        int totalCriadoras = extrairArrayFinal(linhaAtual, criadoras);
                        String[] classificacoes = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
                        int totalClassificacoes = extrairArrayFinal(linhaAtual, classificacoes);
                        String[] tiposGenero = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
                        int totalGeneros = extrairArrayFinal(linhaAtual, tiposGenero);
                        String[] marcadores = new String[ConstantesLimites.TAMANHO_ARRAY_INTERNO];
                        int totalMarcadores = extrairArrayFinal(linhaAtual, marcadores);

                        RegistroJogo jogoProcessado = new RegistroJogo(idAtual, nomeJogo, dataPublicacao, proprietarios, preco,
                                linguas, totalLinguas, notaMetacritic, notaUsuarios, totalConquistas,
                                publicadoras, totalPublicadoras, criadoras, totalCriadoras, classificacoes, totalClassificacoes,
                                tiposGenero, totalGeneros, marcadores, totalMarcadores);
                        removerIdProcessado(posicaoEncontrada);
                        estruturaArvore.adicionarElemento(jogoProcessado);
                    }
                }

                scannerArquivo.close();
                fluxoEntrada.close();

            } catch (Exception excecao) {
                System.out.println("Erro ao abrir ou ler o arquivo: " + excecao.getMessage());
            }
        }

        return estruturaArvore;
    }

    static int verificarIdCorrespondente(int identificador) {
        for (int posicao = 0; posicao < dimensaoArray; posicao++) {
            if (Integer.parseInt(arrayIdentificadores[0]) == identificador)
                return 0;
        }
        return -1;
    }

    static void removerIdProcessado(int posicaoRemover) {
        if (posicaoRemover >= 0 && posicaoRemover < dimensaoArray) {
            for (int deslocamento = posicaoRemover; deslocamento < dimensaoArray - 1; deslocamento++) {
                arrayIdentificadores[deslocamento] = arrayIdentificadores[deslocamento + 1];
            }
            arrayIdentificadores[dimensaoArray - 1] = null;
            dimensaoArray--;
        }
    }

    static int extrairIdentificador(String conteudoLinha) {
        int numeroId = 0;
        while (indiceLeitura < conteudoLinha.length() && Character.isDigit(conteudoLinha.charAt(indiceLeitura))) {
            numeroId = numeroId * 10 + (conteudoLinha.charAt(indiceLeitura) - '0');
            indiceLeitura++;
        }
        return numeroId;
    }

    static String extrairNome(String conteudoLinha) {
        String nomeExtraido = "";
        while (conteudoLinha.charAt(indiceLeitura) != ',' && indiceLeitura < conteudoLinha.length()) {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (conteudoLinha.charAt(indiceLeitura) != ',' && indiceLeitura < conteudoLinha.length()) {
            nomeExtraido += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        return nomeExtraido;
    }

    static String extrairData(String conteudoLinha) {
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != '"') {
            indiceLeitura++;
        }
        indiceLeitura++;
        String diaStr = "", mesStr = "", anoStr = "";
        
        for (int contador = 0; indiceLeitura < conteudoLinha.length() && contador < 3; contador++) {
            mesStr += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        mesStr = mesStr.trim();
        switch (mesStr) {
            case "Jan": mesStr = "01"; break;
            case "Feb": mesStr = "02"; break;
            case "Mar": mesStr = "03"; break;
            case "Apr": mesStr = "04"; break;
            case "May": mesStr = "05"; break;
            case "Jun": mesStr = "06"; break;
            case "Jul": mesStr = "07"; break;
            case "Aug": mesStr = "08"; break;
            case "Sep": mesStr = "09"; break;
            case "Oct": mesStr = "10"; break;
            case "Nov": mesStr = "11"; break;
            case "Dec": mesStr = "12"; break;
            default: break;
        }
        
        while (indiceLeitura < conteudoLinha.length() && !Character.isDigit(conteudoLinha.charAt(indiceLeitura)) && conteudoLinha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        
        while (indiceLeitura < conteudoLinha.length() && Character.isDigit(conteudoLinha.charAt(indiceLeitura))) {
            diaStr += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        
        while (indiceLeitura < conteudoLinha.length() && !Character.isDigit(conteudoLinha.charAt(indiceLeitura))) {
            indiceLeitura++;
        }
        
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != '"') {
            anoStr += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (diaStr.isEmpty())
            diaStr = "01";
        if (mesStr.isEmpty())
            mesStr = "01";
        if (anoStr.isEmpty())
            anoStr = "0000";
        return diaStr + "/" + mesStr + "/" + anoStr;
    }

    static int extrairProprietarios(String conteudoLinha) {
        int numeroProprietarios = 0;
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',') {
            numeroProprietarios = numeroProprietarios * 10 + (conteudoLinha.charAt(indiceLeitura) - '0');
            indiceLeitura++;
        }
        return numeroProprietarios;
    }

    static float extrairValor(String conteudoLinha) {
        String valorString = "";
        while (indiceLeitura < conteudoLinha.length() && !Character.isDigit(conteudoLinha.charAt(indiceLeitura)) && conteudoLinha.charAt(indiceLeitura) != 'F') {
            indiceLeitura++;
        }
        while (indiceLeitura < conteudoLinha.length() && (Character.isDigit(conteudoLinha.charAt(indiceLeitura)) || conteudoLinha.charAt(indiceLeitura) == '.')) {
            valorString += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        valorString = valorString.trim();
        if (valorString.isEmpty() || valorString.equalsIgnoreCase("Free to play")) {
            return 0.0f;
        }
        valorString = valorString.replaceAll("[^0-9.]", "");
        try {
            return Float.parseFloat(valorString);
        } catch (NumberFormatException excecao) {
            return 0.0f;
        }
    }

    static int extrairIdiomas(String conteudoLinha, String[] arrayIdiomas) {
        int quantidadeIdiomas = 0;
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ']' && quantidadeIdiomas < arrayIdiomas.length) {
            String idiomaAtual = "";
            while (indiceLeitura < conteudoLinha.length() && !Character.isAlphabetic(conteudoLinha.charAt(indiceLeitura))) {
                indiceLeitura++;
            }
            while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',' && conteudoLinha.charAt(indiceLeitura) != ']') {
                if (Character.isAlphabetic(conteudoLinha.charAt(indiceLeitura)) || conteudoLinha.charAt(indiceLeitura) == ' '
                        || conteudoLinha.charAt(indiceLeitura) == '-') {
                    idiomaAtual += conteudoLinha.charAt(indiceLeitura);
                }
                indiceLeitura++;
            }
            arrayIdiomas[quantidadeIdiomas++] = idiomaAtual;
        }
        return quantidadeIdiomas;
    }

    static int extrairNotaMetacritic(String conteudoLinha) {
        String notaString = "";
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < conteudoLinha.length() && Character.isDigit(conteudoLinha.charAt(indiceLeitura))) {
            notaString += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (notaString.isEmpty())
            return -1;
        else
            return Integer.parseInt(notaString);
    }

    static float extrairNotaUsuarios(String conteudoLinha) {
        String avaliacaoString = "";
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < conteudoLinha.length() && (Character.isDigit(conteudoLinha.charAt(indiceLeitura)) || conteudoLinha.charAt(indiceLeitura) == '.')) {
            avaliacaoString += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (avaliacaoString.isEmpty())
            return -1.0f;
        else
            return Float.parseFloat(avaliacaoString);
    }

    static int extrairConquistas(String conteudoLinha) {
        String conquistasString = "";
        while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',') {
            indiceLeitura++;
        }
        indiceLeitura++;
        while (indiceLeitura < conteudoLinha.length() && (Character.isDigit(conteudoLinha.charAt(indiceLeitura)) || conteudoLinha.charAt(indiceLeitura) == '.')) {
            conquistasString += conteudoLinha.charAt(indiceLeitura);
            indiceLeitura++;
        }
        if (conquistasString.isEmpty())
            return -1;
        else
            return Integer.parseInt(conquistasString);
    }

    static int extrairArrayFinal(String conteudoLinha, String[] arrayDestino) {
        int totalElementos = 0;
        boolean temAspas = false;
        while (indiceLeitura < conteudoLinha.length() && !Character.isAlphabetic(conteudoLinha.charAt(indiceLeitura))
                && !Character.isDigit(conteudoLinha.charAt(indiceLeitura))) {
            if (conteudoLinha.charAt(indiceLeitura) == '"')
                temAspas = true;
            indiceLeitura++;
        }
        if (temAspas) {
            while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != '"' && totalElementos < arrayDestino.length) {
                String fragmento = "";
                while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',' && conteudoLinha.charAt(indiceLeitura) != '"') {
                    fragmento += conteudoLinha.charAt(indiceLeitura);
                    indiceLeitura++;
                }
                while (indiceLeitura < conteudoLinha.length() && !Character.isAlphabetic(conteudoLinha.charAt(indiceLeitura))
                        && !Character.isDigit(conteudoLinha.charAt(indiceLeitura))
                        && conteudoLinha.charAt(indiceLeitura) != '"') {
                    indiceLeitura++;
                }
                arrayDestino[totalElementos++] = fragmento;
            }
            indiceLeitura++;
        } else {
            if (totalElementos < arrayDestino.length) {
                String fragmento = "";
                while (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) != ',') {
                    fragmento += conteudoLinha.charAt(indiceLeitura);
                    indiceLeitura++;
                }
                arrayDestino[totalElementos++] = fragmento;
            }
        }
        
        if (indiceLeitura < conteudoLinha.length() && conteudoLinha.charAt(indiceLeitura) == ',') {
            indiceLeitura++;
        }
        return totalElementos;
    }
}