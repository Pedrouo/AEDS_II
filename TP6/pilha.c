#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

#define TAMANHO_MAX_LINHA 4096
#define TAMANHO_MAX_CAMPO 512
#define CAPACIDADE_ARRAY 50

// Estrutura para dados do jogo
typedef struct
{
    int identificador;
    char *titulo;
    char *dataLancamento;
    int proprietariosEstimados;
    float valor;
    char **idiomasSuportados;
    int quantidadeIdiomas;
    int notaMetacritic;
    float avaliacaoUsuarios;
    int conquistas;
    char **editoras;
    int quantidadeEditoras;
    char **desenvolvedores;
    int quantidadeDesenvolvedores;
    char **categorias;
    int quantidadeCategorias;
    char **generos;
    int quantidadeGeneros;
    char **etiquetas;
    int quantidadeEtiquetas;
} Jogo;

// Elemento da pilha
typedef struct Elemento
{
    Jogo *jogo;
    struct Elemento *proximo;
} Elemento;

// Estrutura da pilha
typedef struct
{
    Elemento *topo;
    int tamanho;
} Pilha;

// Protótipos - Utilidades de String
char *extrairCampoCSV(char *linha, int *posicao);
char **fragmentarTexto(const char *texto, char separador, int *quantidade);
char *limparEspacos(char *texto);
char *transformarData(char *textoData);
void imprimirArrayString(char **array, int quantidade);

// Protótipos - Manipulação de Jogo
void interpretarLinhaCsv(Jogo *jogo, char *linha);
void mostrarJogo(Jogo *jogo);
void desalocarJogo(Jogo *jogo);

// Protótipos - Operações da Pilha
Pilha *construirPilha();
void empilhar(Pilha *pilha, Jogo *jogo);
Jogo *desempilhar(Pilha *pilha);
bool verificarVazia(Pilha *pilha);
void destruirPilha(Pilha *pilha);
void exibirPilhaOrdenada(Pilha *pilha);

// Implementação - Utilidades de String

char *limparEspacos(char *texto)
{
    char *final;
    while (isspace((unsigned char)*texto))
        texto++;
    if (*texto == 0)
        return texto;
    final = texto + strlen(texto) - 1;
    while (final > texto && isspace((unsigned char)*final))
        final--;
    final[1] = '\0';
    return texto;
}

char *extrairCampoCSV(char *linha, int *posicao)
{
    char *campo = (char *)malloc(sizeof(char) * TAMANHO_MAX_CAMPO);
    int indice = 0;
    bool entreAspas = false;

    if (linha[*posicao] == '"')
    {
        entreAspas = true;
        (*posicao)++;
    }

    while (linha[*posicao] != '\0')
    {
        if (entreAspas)
        {
            if (linha[*posicao] == '"')
            {
                (*posicao)++;
                break;
            }
        }
        else
        {
            if (linha[*posicao] == ',')
            {
                break;
            }
        }
        campo[indice++] = linha[(*posicao)++];
    }

    if (linha[*posicao] == ',')
    {
        (*posicao)++;
    }

    campo[indice] = '\0';
    return campo;
}

char **fragmentarTexto(const char *texto, char separador, int *quantidade)
{
    int contagemInicial = 0;
    for (int i = 0; texto[i]; i++)
        if (texto[i] == separador)
            contagemInicial++;
    *quantidade = contagemInicial + 1;

    char **resultado = (char **)malloc(sizeof(char *) * (*quantidade));
    char buffer[TAMANHO_MAX_CAMPO];
    int idxTexto = 0;
    int idxResultado = 0;

    for (int i = 0; i <= strlen(texto); i++)
    {
        if (texto[i] == separador || texto[i] == '\0')
        {
            buffer[idxTexto] = '\0';
            resultado[idxResultado] = (char *)malloc(sizeof(char) * (strlen(buffer) + 1));
            strcpy(resultado[idxResultado], limparEspacos(buffer));
            idxResultado++;
            idxTexto = 0;
        }
        else
        {
            buffer[idxTexto++] = texto[i];
        }
    }
    return resultado;
}

char *transformarData(char *textoData)
{
    char *dataFormatada = (char *)malloc(sizeof(char) * 12);
    char mesAbreviado[4] = {0};
    char dia[3] = "01";
    char ano[5] = "0000";

    sscanf(textoData, "%s", mesAbreviado);

    char *numeroMes = "01";
    if (strcmp(mesAbreviado, "Jan") == 0)
        numeroMes = "01";
    else if (strcmp(mesAbreviado, "Feb") == 0)
        numeroMes = "02";
    else if (strcmp(mesAbreviado, "Mar") == 0)
        numeroMes = "03";
    else if (strcmp(mesAbreviado, "Apr") == 0)
        numeroMes = "04";
    else if (strcmp(mesAbreviado, "May") == 0)
        numeroMes = "05";
    else if (strcmp(mesAbreviado, "Jun") == 0)
        numeroMes = "06";
    else if (strcmp(mesAbreviado, "Jul") == 0)
        numeroMes = "07";
    else if (strcmp(mesAbreviado, "Aug") == 0)
        numeroMes = "08";
    else if (strcmp(mesAbreviado, "Sep") == 0)
        numeroMes = "09";
    else if (strcmp(mesAbreviado, "Oct") == 0)
        numeroMes = "10";
    else if (strcmp(mesAbreviado, "Nov") == 0)
        numeroMes = "11";
    else if (strcmp(mesAbreviado, "Dec") == 0)
        numeroMes = "12";

    char *ponteiro = textoData;
    while (*ponteiro && !isdigit(*ponteiro))
        ponteiro++;
    if (isdigit(*ponteiro))
        sscanf(ponteiro, "%[^,], %s", dia, ano);

    sprintf(dataFormatada, "%s/%s/%s", dia, numeroMes, ano);
    free(textoData);
    return dataFormatada;
}

void imprimirArrayString(char **array, int quantidade)
{
    printf("[");
    for (int i = 0; i < quantidade; i++)
    {
        printf("%s", array[i]);
        if (i < quantidade - 1)
        {
            printf(", ");
        }
    }
    printf("]");
}

// Implementação - Manipulação de Jogo

void interpretarLinhaCsv(Jogo *jogo, char *linha)
{
    int posicao = 0;

    jogo->identificador = atoi(extrairCampoCSV(linha, &posicao));
    jogo->titulo = extrairCampoCSV(linha, &posicao);
    jogo->dataLancamento = transformarData(extrairCampoCSV(linha, &posicao));
    jogo->proprietariosEstimados = atoi(extrairCampoCSV(linha, &posicao));

    char *textoPreco = extrairCampoCSV(linha, &posicao);
    jogo->valor = (strcmp(textoPreco, "Free to Play") == 0 || strlen(textoPreco) == 0) ? 0.0f : atof(textoPreco);
    free(textoPreco);

    char *textoIdiomas = extrairCampoCSV(linha, &posicao);
    textoIdiomas[strcspn(textoIdiomas, "]")] = 0;
    memmove(textoIdiomas, textoIdiomas + 1, strlen(textoIdiomas));
    for (int i = 0; textoIdiomas[i]; i++)
        if (textoIdiomas[i] == '\'')
            textoIdiomas[i] = ' ';
    jogo->idiomasSuportados = fragmentarTexto(textoIdiomas, ',', &jogo->quantidadeIdiomas);
    free(textoIdiomas);

    jogo->notaMetacritic = atoi(extrairCampoCSV(linha, &posicao));
    jogo->avaliacaoUsuarios = atof(extrairCampoCSV(linha, &posicao));
    jogo->conquistas = atoi(extrairCampoCSV(linha, &posicao));

    jogo->editoras = fragmentarTexto(extrairCampoCSV(linha, &posicao), ',', &jogo->quantidadeEditoras);
    jogo->desenvolvedores = fragmentarTexto(extrairCampoCSV(linha, &posicao), ',', &jogo->quantidadeDesenvolvedores);
    jogo->categorias = fragmentarTexto(extrairCampoCSV(linha, &posicao), ',', &jogo->quantidadeCategorias);
    jogo->generos = fragmentarTexto(extrairCampoCSV(linha, &posicao), ',', &jogo->quantidadeGeneros);
    jogo->etiquetas = fragmentarTexto(extrairCampoCSV(linha, &posicao), ',', &jogo->quantidadeEtiquetas);
}

void mostrarJogo(Jogo *jogo)
{
    char dataFormatada[12];
    strcpy(dataFormatada, jogo->dataLancamento);
    if (dataFormatada[1] == '/')
    {
        memmove(dataFormatada + 1, dataFormatada, strlen(dataFormatada) + 1);
        dataFormatada[0] = '0';
    }

    printf("=> %d ## %s ## %s ## %d ## %.2f ## ",
           jogo->identificador, jogo->titulo, dataFormatada, jogo->proprietariosEstimados, jogo->valor);
    imprimirArrayString(jogo->idiomasSuportados, jogo->quantidadeIdiomas);
    printf(" ## %d ## %.1f ## %d ## ",
           jogo->notaMetacritic,
           jogo->avaliacaoUsuarios,
           jogo->conquistas);
    imprimirArrayString(jogo->editoras, jogo->quantidadeEditoras);
    printf(" ## ");
    imprimirArrayString(jogo->desenvolvedores, jogo->quantidadeDesenvolvedores);
    printf(" ## ");
    imprimirArrayString(jogo->categorias, jogo->quantidadeCategorias);
    printf(" ## ");
    imprimirArrayString(jogo->generos, jogo->quantidadeGeneros);
    printf(" ## ");
    imprimirArrayString(jogo->etiquetas, jogo->quantidadeEtiquetas);
    printf(" ##\n");
}

void desalocarJogo(Jogo *jogo)
{
    free(jogo->titulo);
    free(jogo->dataLancamento);
    for (int i = 0; i < jogo->quantidadeIdiomas; i++)
        free(jogo->idiomasSuportados[i]);
    free(jogo->idiomasSuportados);
    for (int i = 0; i < jogo->quantidadeEditoras; i++)
        free(jogo->editoras[i]);
    free(jogo->editoras);
    for (int i = 0; i < jogo->quantidadeDesenvolvedores; i++)
        free(jogo->desenvolvedores[i]);
    free(jogo->desenvolvedores);
    for (int i = 0; i < jogo->quantidadeCategorias; i++)
        free(jogo->categorias[i]);
    free(jogo->categorias);
    for (int i = 0; i < jogo->quantidadeGeneros; i++)
        free(jogo->generos[i]);
    free(jogo->generos);
    for (int i = 0; i < jogo->quantidadeEtiquetas; i++)
        free(jogo->etiquetas[i]);
    free(jogo->etiquetas);
}

// Implementação - Operações da Pilha

Pilha *construirPilha()
{
    Pilha *pilha = (Pilha *)malloc(sizeof(Pilha));
    pilha->topo = NULL;
    pilha->tamanho = 0;
    return pilha;
}

void empilhar(Pilha *pilha, Jogo *jogo)
{
    Elemento *novoElemento = (Elemento *)malloc(sizeof(Elemento));
    novoElemento->jogo = jogo;
    novoElemento->proximo = pilha->topo;
    pilha->topo = novoElemento;
    pilha->tamanho++;
}

Jogo *desempilhar(Pilha *pilha)
{
    if (verificarVazia(pilha))
    {
        return NULL;
    }

    Elemento *temporario = pilha->topo;
    Jogo *jogo = temporario->jogo;
    pilha->topo = temporario->proximo;
    free(temporario);
    pilha->tamanho--;

    return jogo;
}

bool verificarVazia(Pilha *pilha)
{
    return pilha->topo == NULL;
}

void destruirPilha(Pilha *pilha)
{
    while (!verificarVazia(pilha))
    {
        desempilhar(pilha);
    }
    free(pilha);
}

void exibirPilhaOrdenada(Pilha *pilha)
{
    if (verificarVazia(pilha))
    {
        return;
    }

    Pilha *pilhaTemporaria = construirPilha();
    Elemento *atual = pilha->topo;

    while (atual != NULL)
    {
        empilhar(pilhaTemporaria, atual->jogo);
        atual = atual->proximo;
    }

    atual = pilhaTemporaria->topo;
    int indice = 0;
    while (atual != NULL)
    {
        printf("[%d] ", indice++);
        mostrarJogo(atual->jogo);
        atual = atual->proximo;
    }

    destruirPilha(pilhaTemporaria);
}

// Função Principal
int main()
{
    char bufferLinha[TAMANHO_MAX_LINHA];
    const char *caminhoArquivo = "/tmp/games.csv";

    FILE *arquivo = fopen(caminhoArquivo, "r");
    if (arquivo == NULL)
    {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    int totalJogos = 0;
    fgets(bufferLinha, TAMANHO_MAX_LINHA, arquivo);
    while (fgets(bufferLinha, TAMANHO_MAX_LINHA, arquivo) != NULL)
    {
        totalJogos++;
    }
    fclose(arquivo);

    Jogo *todosJogos = (Jogo *)malloc(sizeof(Jogo) * totalJogos);
    if (todosJogos == NULL)
    {
        printf("Erro de alocação de memória\n");
        return 1;
    }

    arquivo = fopen(caminhoArquivo, "r");
    if (arquivo == NULL)
    {
        perror("Erro ao reabrir o arquivo");
        free(todosJogos);
        return 1;
    }

    fgets(bufferLinha, TAMANHO_MAX_LINHA, arquivo);
    int idx = 0;
    while (fgets(bufferLinha, TAMANHO_MAX_LINHA, arquivo) != NULL)
    {
        interpretarLinhaCsv(&todosJogos[idx], bufferLinha);
        idx++;
    }
    fclose(arquivo);

    Pilha *pilhaJogos = construirPilha();

    char entrada[TAMANHO_MAX_CAMPO];

    // Fase 1: Inserir jogos na pilha até ler "FIM"
    while (fgets(entrada, TAMANHO_MAX_CAMPO, stdin) != NULL)
    {
        entrada[strcspn(entrada, "\n")] = 0;

        if (strcmp(entrada, "FIM") == 0)
        {
            break;
        }

        int idAlvo = atoi(entrada);
        for (idx = 0; idx < totalJogos; idx++)
        {
            if (todosJogos[idx].identificador == idAlvo)
            {
                empilhar(pilhaJogos, &todosJogos[idx]);
                break;
            }
        }
    }

    // Fase 2: Processar comandos (I para inserir, R para remover)
    while (fgets(entrada, TAMANHO_MAX_CAMPO, stdin) != NULL)
    {
        entrada[strcspn(entrada, "\n")] = 0;

        if (strlen(entrada) == 0)
            continue;

        if (entrada[0] == 'I')
        {
            int idAlvo = atoi(entrada + 2);
            for (idx = 0; idx < totalJogos; idx++)
            {
                if (todosJogos[idx].identificador == idAlvo)
                {
                    empilhar(pilhaJogos, &todosJogos[idx]);
                    break;
                }
            }
        }
        else if (entrada[0] == 'R')
        {
            Jogo *removido = desempilhar(pilhaJogos);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->titulo);
            }
        }
    }

    exibirPilhaOrdenada(pilhaJogos);

    destruirPilha(pilhaJogos);

    for (idx = 0; idx < totalJogos; idx++)
    {
        desalocarJogo(&todosJogos[idx]);
    }
    free(todosJogos);

    return 0;
}