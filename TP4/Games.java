import java.io.*;
import java.util.*;

class Game {
    private int appId;
    private String titulo;
    private String dataLancamento;
    private int proprietariosEstimados;
    private float valor;
    private String[] idiomas;
    private int totalIdiomas;
    private int pontuacaoMetacritic;
    private float avaliacaoUsuario;
    private int conquistas;
    private String[] editoras;
    private int totalEditoras;
    private String[] desenvolvedores;
    private int totalDesenvolvedores;
    private String[] categorias;
    private int totalCategorias;
    private String[] generos;
    private int totalGeneros;
    private String[] etiquetas;
    private int totalEtiquetas;

    public Game() {
        this.appId = 0;
        this.titulo = "";
        this.dataLancamento = "";
        this.proprietariosEstimados = 0;
        this.valor = 0.0f;
        this.idiomas = null;
        this.totalIdiomas = 0;
        this.pontuacaoMetacritic = 0;
        this.avaliacaoUsuario = 0.0f;
        this.conquistas = 0;
        this.editoras = null;
        this.totalEditoras = 0;
        this.desenvolvedores = null;
        this.totalDesenvolvedores = 0;
        this.categorias = null;
        this.totalCategorias = 0;
        this.generos = null;
        this.totalGeneros = 0;
        this.etiquetas = null;
        this.totalEtiquetas = 0;
    }

    public int getAppId() { return appId; }
    public void setAppId(int appId) { this.appId = appId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDataLancamento() { return dataLancamento; }
    public void setDataLancamento(String dataLancamento) { this.dataLancamento = dataLancamento; }

    public int getProprietariosEstimados() { return proprietariosEstimados; }
    public void setProprietariosEstimados(int proprietariosEstimados) { this.proprietariosEstimados = proprietariosEstimados; }

    public float getValor() { return valor; }
    public void setValor(float valor) { this.valor = valor; }

    public String[] getIdiomas() { return idiomas; }
    public void setIdiomas(String[] idiomas) { this.idiomas = idiomas; }

    public int getTotalIdiomas() { return totalIdiomas; }
    public void setTotalIdiomas(int totalIdiomas) { this.totalIdiomas = totalIdiomas; }

    public int getPontuacaoMetacritic() { return pontuacaoMetacritic; }
    public void setPontuacaoMetacritic(int pontuacaoMetacritic) { this.pontuacaoMetacritic = pontuacaoMetacritic; }

    public float getAvaliacaoUsuario() { return avaliacaoUsuario; }
    public void setAvaliacaoUsuario(float avaliacaoUsuario) { this.avaliacaoUsuario = avaliacaoUsuario; }

    public int getConquistas() { return conquistas; }
    public void setConquistas(int conquistas) { this.conquistas = conquistas; }

    public String[] getEditoras() { return editoras; }
    public void setEditoras(String[] editoras) { this.editoras = editoras; }

    public int getTotalEditoras() { return totalEditoras; }
    public void setTotalEditoras(int totalEditoras) { this.totalEditoras = totalEditoras; }

    public String[] getDesenvolvedores() { return desenvolvedores; }
    public void setDesenvolvedores(String[] desenvolvedores) { this.desenvolvedores = desenvolvedores; }

    public int getTotalDesenvolvedores() { return totalDesenvolvedores; }
    public void setTotalDesenvolvedores(int totalDesenvolvedores) { this.totalDesenvolvedores = totalDesenvolvedores; }

    public String[] getCategorias() { return categorias; }
    public void setCategorias(String[] categorias) { this.categorias = categorias; }

    public int getTotalCategorias() { return totalCategorias; }
    public void setTotalCategorias(int totalCategorias) { this.totalCategorias = totalCategorias; }

    public String[] getGeneros() { return generos; }
    public void setGeneros(String[] generos) { this.generos = generos; }

    public int getTotalGeneros() { return totalGeneros; }
    public void setTotalGeneros(int totalGeneros) { this.totalGeneros = totalGeneros; }

    public String[] getEtiquetas() { return etiquetas; }
    public void setEtiquetas(String[] etiquetas) { this.etiquetas = etiquetas; }

    public int getTotalEtiquetas() { return totalEtiquetas; }
    public void setTotalEtiquetas(int totalEtiquetas) { this.totalEtiquetas = totalEtiquetas; }

    public void mostrarDadosJogo() {
        String dataFormatadaExibicao = this.dataLancamento;
        if (dataFormatadaExibicao.length() > 1 && dataFormatadaExibicao.charAt(1) == '/') {
            dataFormatadaExibicao = "0" + dataFormatadaExibicao;
        }

        System.out.printf("=> %d ## %s ## %s ## %d ## %.2f ## ",
                this.appId, this.titulo, dataFormatadaExibicao, this.proprietariosEstimados, this.valor);
        imprimirListaCampos(this.idiomas, this.totalIdiomas);
        System.out.printf(" ## %d ## %.1f ## %d ## ",
                this.pontuacaoMetacritic != 0 ? this.pontuacaoMetacritic : -1,
                this.avaliacaoUsuario != 0.0f ? this.avaliacaoUsuario : -1.0f,
                this.conquistas);
        imprimirListaCampos(this.editoras, this.totalEditoras);
        System.out.print(" ## ");
        imprimirListaCampos(this.desenvolvedores, this.totalDesenvolvedores);
        System.out.print(" ## ");
        imprimirListaCampos(this.categorias, this.totalCategorias);
        System.out.print(" ## ");
        imprimirListaCampos(this.generos, this.totalGeneros);
        System.out.print(" ## ");
        imprimirListaCampos(this.etiquetas, this.totalEtiquetas);
        System.out.println(" ##");
    }

    private void imprimirListaCampos(String[] lista, int tamanho) {
        System.out.print("[");
        for (int i = 0; i < tamanho; i++) {
            System.out.print(lista[i]);
            if (i < tamanho - 1) System.out.print(", ");
        }
        System.out.print("]");
    }

    public void processarLinhaCSV(String linhaCompleta) {
        int posicaoLeitura = 0;

        this.appId = Integer.parseInt(extrairToken(linhaCompleta, posicaoLeitura));
        posicaoLeitura = proximaPosicao;
        
        this.titulo = extrairToken(linhaCompleta, posicaoLeitura);
        posicaoLeitura = proximaPosicao;
        
        this.dataLancamento = converterFormatoData(extrairToken(linhaCompleta, posicaoLeitura));
        posicaoLeitura = proximaPosicao;
        
        this.proprietariosEstimados = Integer.parseInt(extrairToken(linhaCompleta, posicaoLeitura));
        posicaoLeitura = proximaPosicao;

        String stringPreco = extrairToken(linhaCompleta, posicaoLeitura);
        posicaoLeitura = proximaPosicao;
        this.valor = (stringPreco.isEmpty() || stringPreco.equals("Free to Play")) ? 0.0f : Float.parseFloat(stringPreco);

        String campoIdiomas = extrairToken(linhaCompleta, posicaoLeitura);
        posicaoLeitura = proximaPosicao;
        if (campoIdiomas.startsWith("[")) {
            campoIdiomas = campoIdiomas.substring(1);
        }
        if (campoIdiomas.contains("]")) {
            campoIdiomas = campoIdiomas.substring(0, campoIdiomas.indexOf("]"));
        }
        campoIdiomas = campoIdiomas.replace("'", " ");
        this.idiomas = separarPorDelimitador(campoIdiomas, ',');
        this.totalIdiomas = this.idiomas.length;

        this.pontuacaoMetacritic = Integer.parseInt(extrairToken(linhaCompleta, posicaoLeitura));
        posicaoLeitura = proximaPosicao;
        
        this.avaliacaoUsuario = Float.parseFloat(extrairToken(linhaCompleta, posicaoLeitura));
        posicaoLeitura = proximaPosicao;
        
        this.conquistas = Integer.parseInt(extrairToken(linhaCompleta, posicaoLeitura));
        posicaoLeitura = proximaPosicao;

        this.editoras = separarPorDelimitador(extrairToken(linhaCompleta, posicaoLeitura), ',');
        this.totalEditoras = this.editoras.length;
        posicaoLeitura = proximaPosicao;

        this.desenvolvedores = separarPorDelimitador(extrairToken(linhaCompleta, posicaoLeitura), ',');
        this.totalDesenvolvedores = this.desenvolvedores.length;
        posicaoLeitura = proximaPosicao;

        this.categorias = separarPorDelimitador(extrairToken(linhaCompleta, posicaoLeitura), ',');
        this.totalCategorias = this.categorias.length;
        posicaoLeitura = proximaPosicao;

        this.generos = separarPorDelimitador(extrairToken(linhaCompleta, posicaoLeitura), ',');
        this.totalGeneros = this.generos.length;
        posicaoLeitura = proximaPosicao;

        this.etiquetas = separarPorDelimitador(extrairToken(linhaCompleta, posicaoLeitura), ',');
        this.totalEtiquetas = this.etiquetas.length;
    }

    private int proximaPosicao;

    private String extrairToken(String str, int idx) {
        StringBuilder tokenExtraido = new StringBuilder();
        boolean dentroAspas = false;

        if (idx < str.length() && str.charAt(idx) == '"') {
            dentroAspas = true;
            idx++;
        }

        while (idx < str.length() && (dentroAspas || str.charAt(idx) != ',')) {
            if (dentroAspas && str.charAt(idx) == '"') {
                idx++;
                break;
            }
            tokenExtraido.append(str.charAt(idx));
            idx++;
        }

        if (idx < str.length() && str.charAt(idx) == ',') {
            idx++;
        }

        proximaPosicao = idx;
        return tokenExtraido.toString();
    }

    private String[] separarPorDelimitador(String conteudo, char separador) {
        int contadorSeparadores = 0;
        for (int i = 0; i < conteudo.length(); i++) {
            if (conteudo.charAt(i) == separador) contadorSeparadores++;
        }
        int quantidade = contadorSeparadores + 1;

        String[] arrayPartes = new String[quantidade];
        int indiceInicio = 0;
        int indiceAtual = 0;

        for (int i = 0; i <= conteudo.length(); i++) {
            if (i == conteudo.length() || conteudo.charAt(i) == separador) {
                String parte = conteudo.substring(indiceInicio, i);
                arrayPartes[indiceAtual] = limparEspacosEmBranco(parte);
                indiceAtual++;
                indiceInicio = i + 1;
            }
        }
        return arrayPartes;
    }

    private String limparEspacosEmBranco(String str) {
        return str.trim();
    }

    private String converterFormatoData(String dataOriginal) {
        String diaStr = "01";
        String mesAbreviado = "";
        String anoStr = "0000";

        String[] partes = dataOriginal.split(" ");
        if (partes.length >= 3) {
            mesAbreviado = partes[0];
            diaStr = partes[1].replace(",", "");
            anoStr = partes[2];
        }

        String numeroMes = "01";
        switch (mesAbreviado) {
            case "Feb": numeroMes = "02"; break;
            case "Mar": numeroMes = "03"; break;
            case "Apr": numeroMes = "04"; break;
            case "May": numeroMes = "05"; break;
            case "Jun": numeroMes = "06"; break;
            case "Jul": numeroMes = "07"; break;
            case "Aug": numeroMes = "08"; break;
            case "Sep": numeroMes = "09"; break;
            case "Oct": numeroMes = "10"; break;
            case "Nov": numeroMes = "11"; break;
            case "Dec": numeroMes = "12"; break;
        }

        return diaStr + "/" + numeroMes + "/" + anoStr;
    }
}

public class Games {
    public static void main(String[] args) {
        String arquivoCSV = "/tmp/games.csv";
        ArrayList<Game> listaJogos = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivoCSV));
            String linha;
            
            br.readLine(); // pula cabeçalho
            
            while ((linha = br.readLine()) != null) {
                Game jogo = new Game();
                jogo.processarLinhaCSV(linha);
                listaJogos.add(jogo);
            }
            br.close();

        } catch (IOException e) {
            System.err.println("Erro ao abrir o arquivo");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        String inputUsuario;
        
        while (scanner.hasNextLine()) {
            inputUsuario = scanner.nextLine();
            if (inputUsuario.equals("FIM")) break;

            int idProcurado = Integer.parseInt(inputUsuario);
            for (Game jogo : listaJogos) {
                if (jogo.getAppId() == idProcurado) {
                    jogo.mostrarDadosJogo();
                    break;
                }
            }
        }
        scanner.close();
    }
}