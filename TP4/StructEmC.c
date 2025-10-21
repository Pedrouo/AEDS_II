#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

#define MAX_BUFFER 4096
#define MAX_TOKEN 512

typedef struct {
    int appId;
    char* titulo;
    char* dataLancamento;
    int proprietariosEstimados;
    float valor;
    char** idiomas;
    int totalIdiomas;
    int pontuacaoMetacritic;
    float avaliacaoUsuario;
    int conquistas;
    char** editoras;
    int totalEditoras;
    char** desenvolvedores;
    int totalDesenvolvedores;
    char** categorias;
    int totalCategorias;
    char** generos;
    int totalGeneros;
    char** etiquetas;
    int totalEtiquetas;
} Game;

char* extrairToken(char* str, int* idx);
char* limparEspacosEmBranco(char* str);
char** separarPorDelimitador(char* conteudo, char separador, int* quantidade);
char* converterFormatoData(char* dataOriginal);
void imprimirListaCampos(char** lista, int tamanho);
void mostrarDadosJogo(Game* jogo);
void processarLinhaCSV(Game* jogo, char* linhaCompleta);
void desalocarMemoriaJogo(Game* jogo);

int main() {
    const char* arquivoCSV = "/tmp/games.csv";
    FILE* fp = fopen(arquivoCSV, "r");
    if (!fp) { perror("Erro ao abrir o arquivo"); return 1; }

    char linha[MAX_BUFFER];
    int contadorJogos = 0;

    fgets(linha, MAX_BUFFER, fp);
    while (fgets(linha, MAX_BUFFER, fp)) contadorJogos++;
    fclose(fp);

    Game* listaJogos = (Game*) malloc(sizeof(Game) * contadorJogos);
    if (!listaJogos) { printf("Falha na alocação de memória.\n"); return 1; }

    fp = fopen(arquivoCSV, "r");
    fgets(linha, MAX_BUFFER, fp);
    int posicaoAtual = 0;
    while (fgets(linha, MAX_BUFFER, fp)) {
        processarLinhaCSV(&listaJogos[posicaoAtual++], linha);
    }
    fclose(fp);

    char inputUsuario[MAX_TOKEN];
    while (fgets(inputUsuario, MAX_TOKEN, stdin)) {
        inputUsuario[strcspn(inputUsuario, "\n")] = 0;
        if (strcmp(inputUsuario, "FIM") == 0) break;

        int idProcurado = atoi(inputUsuario);
        for (int i = 0; i < contadorJogos; i++) {
            if (listaJogos[i].appId == idProcurado) {
                mostrarDadosJogo(&listaJogos[i]);
                break;
            }
        }
    }

    for (int i = 0; i < contadorJogos; i++) desalocarMemoriaJogo(&listaJogos[i]);
    free(listaJogos);
    return 0;
}

char* extrairToken(char* str, int* idx) {
    char* tokenExtraido = malloc(MAX_TOKEN);
    int contadorChar = 0;
    bool dentroAspas = false;

    if (str[*idx] == '"') { dentroAspas = true; (*idx)++; }

    while (str[*idx] && (dentroAspas || str[*idx] != ',')) {
        if (dentroAspas && str[*idx] == '"') { (*idx)++; break; }
        tokenExtraido[contadorChar++] = str[(*idx)++];
    }

    if (str[*idx] == ',') (*idx)++;
    tokenExtraido[contadorChar] = '\0';
    return tokenExtraido;
}

char* limparEspacosEmBranco(char* str) {
    while (isspace((unsigned char)*str)) str++;

    char* novaString = malloc(strlen(str) + 1);
    strcpy(novaString, str);

    char* ponteiroFinal = novaString + strlen(novaString) - 1;
    while (ponteiroFinal > novaString && isspace((unsigned char)*ponteiroFinal)) *ponteiroFinal-- = '\0';

    return novaString;
}

char** separarPorDelimitador(char* conteudo, char separador, int* quantidade) {
    int contadorSeparadores = 0;
    for (int i = 0; conteudo[i]; i++) if (conteudo[i] == separador) contadorSeparadores++;
    *quantidade = contadorSeparadores + 1;

    char** arrayPartes = malloc(sizeof(char*) * (*quantidade));
    int indiceInicio = 0, indiceAtual = 0;

    for (int i = 0; ; i++) {
        if (conteudo[i] == separador || conteudo[i] == '\0') {
            int comprimento = i - indiceInicio;
            arrayPartes[indiceAtual] = malloc(comprimento + 1);
            strncpy(arrayPartes[indiceAtual], conteudo + indiceInicio, comprimento);
            arrayPartes[indiceAtual][comprimento] = '\0';

            char* stringLimpa = limparEspacosEmBranco(arrayPartes[indiceAtual]);
            free(arrayPartes[indiceAtual]);
            arrayPartes[indiceAtual] = stringLimpa;

            indiceAtual++;
            indiceInicio = i + 1;
        }
        if (conteudo[i] == '\0') break;
    }
    return arrayPartes;
}

char* converterFormatoData(char* dataOriginal) {
    char* dataConvertida = malloc(12);
    char diaStr[3] = "01", mesAbreviado[4] = {0}, anoStr[5] = "0000";

    sscanf(dataOriginal, "%3s %[^,], %s", mesAbreviado, diaStr, anoStr);

    char* numeroMes = "01";
    if (!strcmp(mesAbreviado,"Feb")) numeroMes="02";
    else if (!strcmp(mesAbreviado,"Mar")) numeroMes="03";
    else if (!strcmp(mesAbreviado,"Apr")) numeroMes="04";
    else if (!strcmp(mesAbreviado,"May")) numeroMes="05";
    else if (!strcmp(mesAbreviado,"Jun")) numeroMes="06";
    else if (!strcmp(mesAbreviado,"Jul")) numeroMes="07";
    else if (!strcmp(mesAbreviado,"Aug")) numeroMes="08";
    else if (!strcmp(mesAbreviado,"Sep")) numeroMes="09";
    else if (!strcmp(mesAbreviado,"Oct")) numeroMes="10";
    else if (!strcmp(mesAbreviado,"Nov")) numeroMes="11";
    else if (!strcmp(mesAbreviado,"Dec")) numeroMes="12";

    sprintf(dataConvertida, "%s/%s/%s", diaStr, numeroMes, anoStr);
    free(dataOriginal);
    return dataConvertida;
}

void processarLinhaCSV(Game* jogo, char* linhaCompleta) {
    int posicaoLeitura = 0;

    jogo->appId = atoi(extrairToken(linhaCompleta, &posicaoLeitura));
    jogo->titulo = extrairToken(linhaCompleta, &posicaoLeitura);
    jogo->dataLancamento = converterFormatoData(extrairToken(linhaCompleta, &posicaoLeitura));
    jogo->proprietariosEstimados = atoi(extrairToken(linhaCompleta, &posicaoLeitura));

    char* stringPreco = extrairToken(linhaCompleta, &posicaoLeitura);
    jogo->valor = (!stringPreco[0] || strcmp(stringPreco,"Free to Play")==0) ? 0.0f : atof(stringPreco);
    free(stringPreco);

    char* campoIdiomas = extrairToken(linhaCompleta, &posicaoLeitura);
    if (campoIdiomas[0] == '[') memmove(campoIdiomas, campoIdiomas + 1, strlen(campoIdiomas));
    campoIdiomas[strcspn(campoIdiomas, "]")] = '\0';
    for (int i = 0; campoIdiomas[i]; i++) if (campoIdiomas[i] == '\'') campoIdiomas[i] = ' ';
    jogo->idiomas = separarPorDelimitador(campoIdiomas, ',', &jogo->totalIdiomas);
    free(campoIdiomas);

    jogo->pontuacaoMetacritic = atoi(extrairToken(linhaCompleta, &posicaoLeitura));
    jogo->avaliacaoUsuario = atof(extrairToken(linhaCompleta, &posicaoLeitura));
    jogo->conquistas = atoi(extrairToken(linhaCompleta, &posicaoLeitura));

    jogo->editoras = separarPorDelimitador(extrairToken(linhaCompleta, &posicaoLeitura), ',', &jogo->totalEditoras);
    jogo->desenvolvedores = separarPorDelimitador(extrairToken(linhaCompleta, &posicaoLeitura), ',', &jogo->totalDesenvolvedores);
    jogo->categorias = separarPorDelimitador(extrairToken(linhaCompleta, &posicaoLeitura), ',', &jogo->totalCategorias);
    jogo->generos = separarPorDelimitador(extrairToken(linhaCompleta, &posicaoLeitura), ',', &jogo->totalGeneros);
    jogo->etiquetas = separarPorDelimitador(extrairToken(linhaCompleta, &posicaoLeitura), ',', &jogo->totalEtiquetas);
}

void imprimirListaCampos(char** lista, int tamanho) {
    printf("[");
    for (int i = 0; i < tamanho; i++) {
        printf("%s", lista[i]);
        if (i < tamanho - 1) printf(", ");
    }
    printf("]");
}

void mostrarDadosJogo(Game* jogo) {
    char dataFormatadaExibicao[12];
    strcpy(dataFormatadaExibicao, jogo->dataLancamento);
    if (dataFormatadaExibicao[1] == '/') {
        memmove(dataFormatadaExibicao + 1, dataFormatadaExibicao, strlen(dataFormatadaExibicao) + 1);
        dataFormatadaExibicao[0] = '0';
    }

    printf("=> %d ## %s ## %s ## %d ## %.2f ## ",
           jogo->appId, jogo->titulo, dataFormatadaExibicao, jogo->proprietariosEstimados, jogo->valor);
    imprimirListaCampos(jogo->idiomas, jogo->totalIdiomas);
    printf(" ## %d ## %.1f ## %d ## ",
           jogo->pontuacaoMetacritic ? jogo->pontuacaoMetacritic : -1,
           jogo->avaliacaoUsuario ? jogo->avaliacaoUsuario : -1.0f,
           jogo->conquistas);
    imprimirListaCampos(jogo->editoras, jogo->totalEditoras); printf(" ## ");
    imprimirListaCampos(jogo->desenvolvedores, jogo->totalDesenvolvedores); printf(" ## ");
    imprimirListaCampos(jogo->categorias, jogo->totalCategorias); printf(" ## ");
    imprimirListaCampos(jogo->generos, jogo->totalGeneros); printf(" ## ");
    imprimirListaCampos(jogo->etiquetas, jogo->totalEtiquetas); printf(" ##\n");
}

void desalocarMemoriaJogo(Game* jogo) {
    free(jogo->titulo);
    free(jogo->dataLancamento);
    for (int i = 0; i < jogo->totalIdiomas; i++) free(jogo->idiomas[i]);
    free(jogo->idiomas);
    for (int i = 0; i < jogo->totalEditoras; i++) free(jogo->editoras[i]);
    free(jogo->editoras);
    for (int i = 0; i < jogo->totalDesenvolvedores; i++) free(jogo->desenvolvedores[i]);
    free(jogo->desenvolvedores);
    for (int i = 0; i < jogo->totalCategorias; i++) free(jogo->categorias[i]);
    free(jogo->categorias);
    for (int i = 0; i < jogo->totalGeneros; i++) free(jogo->generos[i]);
    free(jogo->generos);
    for (int i = 0; i < jogo->totalEtiquetas; i++) free(jogo->etiquetas[i]);
    free(jogo->etiquetas);
}