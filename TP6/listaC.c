#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>

#define TAMANHO_MAX_LINHA 4096
#define TAMANHO_MAX_CAMPO 512
#define CAPACIDADE_ARRAY 50
#define LIMITE_IDENTIFICADORES 100

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

// Nó da lista encadeada dupla
typedef struct Nodo
{
    struct Nodo *proximo;
    struct Nodo *anterior;
    Jogo *jogo;
} Nodo;

// Lista encadeada dupla
typedef struct
{
    Nodo *inicio;
    Nodo *fim;
} ListaEncadeada;

// Variáveis globais
char **identificadoresEntrada;
int totalIdentificadores = 0;

// Protótipos - Utilidades de String
char *obterProximoCampo(char *linha, int *posicao);
char **dividirTexto(const char *texto, char separador, int *quantidade);
char *removerEspacos(char *texto);
char *converterData(char *textoData);
void exibirArrayTexto(char **array, int quantidade);

// Protótipos - Manipulação de Jogo
void carregarJogoDoTexto(Jogo *jogo, char *linha);
void exibirJogo(Jogo *jogo);
void liberarMemoriaJogo(Jogo *jogo);

// Protótipos - Operações da Lista
ListaEncadeada *inicializarLista();
void adicionarNoComeco(ListaEncadeada *lista, Jogo *jogo);
void adicionarNoFinal(ListaEncadeada *lista, Jogo *jogo);
void adicionarNaPosicao(ListaEncadeada *lista, Jogo *jogo, int posicao);
Jogo *extrairDoComeco(ListaEncadeada *lista);
Jogo *extrairDoFinal(ListaEncadeada *lista);
Jogo *extrairDaPosicao(ListaEncadeada *lista, int posicao);
int contarElementos(ListaEncadeada *lista);
void mostrarListaCompleta(ListaEncadeada *lista);
void destruirLista(ListaEncadeada *lista);

// Implementação - Utilidades de String

char *removerEspacos(char *texto)
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

char *obterProximoCampo(char *linha, int *posicao)
{
    char *campo = (char *)malloc(sizeof(char) * TAMANHO_MAX_CAMPO);
    int indice = 0;
    bool dentroAspas = false;

    if (linha[*posicao] == '"')
    {
        dentroAspas = true;
        (*posicao)++;
    }

    while (linha[*posicao] != '\0')
    {
        if (dentroAspas)
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

char **dividirTexto(const char *texto, char separador, int *quantidade)
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
            strcpy(resultado[idxResultado], removerEspacos(buffer));
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

char *converterData(char *textoData)
{
    char *dataFormatada = (char *)malloc(sizeof(char) * 12);
    char mesTexto[4] = {0};
    char dia[3] = "01";
    char ano[5] = "0000";

    sscanf(textoData, "%s", mesTexto);

    char *numeroMes = "01";
    if (strcmp(mesTexto, "Jan") == 0)
        numeroMes = "01";
    else if (strcmp(mesTexto, "Feb") == 0)
        numeroMes = "02";
    else if (strcmp(mesTexto, "Mar") == 0)
        numeroMes = "03";
    else if (strcmp(mesTexto, "Apr") == 0)
        numeroMes = "04";
    else if (strcmp(mesTexto, "May") == 0)
        numeroMes = "05";
    else if (strcmp(mesTexto, "Jun") == 0)
        numeroMes = "06";
    else if (strcmp(mesTexto, "Jul") == 0)
        numeroMes = "07";
    else if (strcmp(mesTexto, "Aug") == 0)
        numeroMes = "08";
    else if (strcmp(mesTexto, "Sep") == 0)
        numeroMes = "09";
    else if (strcmp(mesTexto, "Oct") == 0)
        numeroMes = "10";
    else if (strcmp(mesTexto, "Nov") == 0)
        numeroMes = "11";
    else if (strcmp(mesTexto, "Dec") == 0)
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

void exibirArrayTexto(char **array, int quantidade)
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

void carregarJogoDoTexto(Jogo *jogo, char *linha)
{
    int posicao = 0;

    jogo->identificador = atoi(obterProximoCampo(linha, &posicao));
    jogo->titulo = obterProximoCampo(linha, &posicao);
    jogo->dataLancamento = converterData(obterProximoCampo(linha, &posicao));
    jogo->proprietariosEstimados = atoi(obterProximoCampo(linha, &posicao));

    char *textoPreco = obterProximoCampo(linha, &posicao);
    jogo->valor = (strcmp(textoPreco, "Free to Play") == 0 || strlen(textoPreco) == 0) ? 0.0f : atof(textoPreco);
    free(textoPreco);

    char *textoIdiomas = obterProximoCampo(linha, &posicao);
    textoIdiomas[strcspn(textoIdiomas, "]")] = 0;
    memmove(textoIdiomas, textoIdiomas + 1, strlen(textoIdiomas));
    for (int i = 0; textoIdiomas[i]; i++)
        if (textoIdiomas[i] == '\'')
            textoIdiomas[i] = ' ';
    jogo->idiomasSuportados = dividirTexto(textoIdiomas, ',', &jogo->quantidadeIdiomas);
    free(textoIdiomas);

    jogo->notaMetacritic = atoi(obterProximoCampo(linha, &posicao));
    jogo->avaliacaoUsuarios = atof(obterProximoCampo(linha, &posicao));
    jogo->conquistas = atoi(obterProximoCampo(linha, &posicao));

    jogo->editoras = dividirTexto(obterProximoCampo(linha, &posicao), ',', &jogo->quantidadeEditoras);
    jogo->desenvolvedores = dividirTexto(obterProximoCampo(linha, &posicao), ',', &jogo->quantidadeDesenvolvedores);
    jogo->categorias = dividirTexto(obterProximoCampo(linha, &posicao), ',', &jogo->quantidadeCategorias);
    jogo->generos = dividirTexto(obterProximoCampo(linha, &posicao), ',', &jogo->quantidadeGeneros);
    jogo->etiquetas = dividirTexto(obterProximoCampo(linha, &posicao), ',', &jogo->quantidadeEtiquetas);
}

void exibirJogo(Jogo *jogo)
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
    exibirArrayTexto(jogo->idiomasSuportados, jogo->quantidadeIdiomas);
    printf(" ## %d ## %.1f ## %d ## ",
           jogo->notaMetacritic,
           jogo->avaliacaoUsuarios,
           jogo->conquistas);
    exibirArrayTexto(jogo->editoras, jogo->quantidadeEditoras);
    printf(" ## ");
    exibirArrayTexto(jogo->desenvolvedores, jogo->quantidadeDesenvolvedores);
    printf(" ## ");
    exibirArrayTexto(jogo->categorias, jogo->quantidadeCategorias);
    printf(" ## ");
    exibirArrayTexto(jogo->generos, jogo->quantidadeGeneros);
    printf(" ## ");
    exibirArrayTexto(jogo->etiquetas, jogo->quantidadeEtiquetas);
    printf(" ##\n");
}

void liberarMemoriaJogo(Jogo *jogo)
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

// Implementação - Operações da Lista

ListaEncadeada *inicializarLista()
{
    ListaEncadeada *lista = (ListaEncadeada *)malloc(sizeof(ListaEncadeada));
    lista->inicio = NULL;
    lista->fim = NULL;
    return lista;
}

int contarElementos(ListaEncadeada *lista)
{
    int tamanho = 0;
    Nodo *atual = lista->inicio;
    while (atual != NULL)
    {
        tamanho++;
        atual = atual->proximo;
    }
    return tamanho;
}

void adicionarNoComeco(ListaEncadeada *lista, Jogo *jogo)
{
    Nodo *novoNodo = (Nodo *)malloc(sizeof(Nodo));
    novoNodo->jogo = jogo;
    novoNodo->anterior = NULL;
    novoNodo->proximo = lista->inicio;

    if (lista->inicio != NULL)
    {
        lista->inicio->anterior = novoNodo;
    }
    else
    {
        lista->fim = novoNodo;
    }
    lista->inicio = novoNodo;
}

void adicionarNoFinal(ListaEncadeada *lista, Jogo *jogo)
{
    Nodo *novoNodo = (Nodo *)malloc(sizeof(Nodo));
    novoNodo->jogo = jogo;
    novoNodo->proximo = NULL;
    novoNodo->anterior = lista->fim;

    if (lista->fim != NULL)
    {
        lista->fim->proximo = novoNodo;
    }
    else
    {
        lista->inicio = novoNodo;
    }
    lista->fim = novoNodo;
}

void adicionarNaPosicao(ListaEncadeada *lista, Jogo *jogo, int posicao)
{
    if (posicao < 0 || posicao > contarElementos(lista))
        return;

    if (posicao == 0)
    {
        adicionarNoComeco(lista, jogo);
        return;
    }
    if (posicao == contarElementos(lista))
    {
        adicionarNoFinal(lista, jogo);
        return;
    }

    Nodo *atual = lista->inicio;
    for (int i = 0; i < posicao; i++)
    {
        atual = atual->proximo;
    }

    Nodo *novoNodo = (Nodo *)malloc(sizeof(Nodo));
    novoNodo->jogo = jogo;
    novoNodo->anterior = atual->anterior;
    novoNodo->proximo = atual;
    atual->anterior->proximo = novoNodo;
    atual->anterior = novoNodo;
}

Jogo *extrairDoComeco(ListaEncadeada *lista)
{
    if (lista->inicio == NULL)
        return NULL;

    Nodo *nodoRemovido = lista->inicio;
    Jogo *jogo = nodoRemovido->jogo;

    lista->inicio = nodoRemovido->proximo;
    if (lista->inicio != NULL)
    {
        lista->inicio->anterior = NULL;
    }
    else
    {
        lista->fim = NULL;
    }

    free(nodoRemovido);
    return jogo;
}

Jogo *extrairDoFinal(ListaEncadeada *lista)
{
    if (lista->fim == NULL)
        return NULL;

    Nodo *nodoRemovido = lista->fim;
    Jogo *jogo = nodoRemovido->jogo;

    lista->fim = nodoRemovido->anterior;
    if (lista->fim != NULL)
    {
        lista->fim->proximo = NULL;
    }
    else
    {
        lista->inicio = NULL;
    }

    free(nodoRemovido);
    return jogo;
}

Jogo *extrairDaPosicao(ListaEncadeada *lista, int posicao)
{
    if (posicao < 0 || posicao >= contarElementos(lista))
        return NULL;

    if (posicao == 0)
        return extrairDoComeco(lista);
    if (posicao == contarElementos(lista) - 1)
        return extrairDoFinal(lista);

    Nodo *atual = lista->inicio;
    for (int i = 0; i < posicao; i++)
    {
        atual = atual->proximo;
    }

    Jogo *jogo = atual->jogo;
    atual->anterior->proximo = atual->proximo;
    atual->proximo->anterior = atual->anterior;

    free(atual);
    return jogo;
}

void mostrarListaCompleta(ListaEncadeada *lista)
{
    Nodo *atual = lista->inicio;
    int indice = 0;
    while (atual != NULL)
    {
        printf("[%d] ", indice++);
        exibirJogo(atual->jogo);
        atual = atual->proximo;
    }
}

void destruirLista(ListaEncadeada *lista)
{
    Nodo *atual = lista->inicio;
    while (atual != NULL)
    {
        Nodo *proximo = atual->proximo;
        liberarMemoriaJogo(atual->jogo);
        free(atual->jogo);
        free(atual);
        atual = proximo;
    }
    free(lista);
}

// Função Principal
int main()
{
    char bufferLinha[TAMANHO_MAX_LINHA];
    const char *caminhoArquivo = "/tmp/games.csv";

    // Alocar memória para identificadores
    identificadoresEntrada = (char **)malloc(sizeof(char *) * LIMITE_IDENTIFICADORES);
    for (int i = 0; i < LIMITE_IDENTIFICADORES; i++)
    {
        identificadoresEntrada[i] = (char *)malloc(sizeof(char) * TAMANHO_MAX_CAMPO);
    }

    // Ler identificadores da entrada
    char entrada[TAMANHO_MAX_CAMPO];
    while (fgets(entrada, TAMANHO_MAX_CAMPO, stdin) != NULL)
    {
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0)
            break;
        strcpy(identificadoresEntrada[totalIdentificadores++], entrada);
    }

    // Carregar todos os jogos do arquivo
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
        carregarJogoDoTexto(&todosJogos[idx], bufferLinha);
        idx++;
    }
    fclose(arquivo);

    // Criar lista e inserir jogos baseados nos identificadores
    ListaEncadeada *listaJogos = inicializarLista();

    for (idx = 0; idx < totalIdentificadores; idx++)
    {
        int idAlvo = atoi(identificadoresEntrada[idx]);
        for (int j = 0; j < totalJogos; j++)
        {
            if (todosJogos[j].identificador == idAlvo)
            {
                Jogo *novoJogo = (Jogo *)malloc(sizeof(Jogo));
                *novoJogo = todosJogos[j];
                novoJogo->titulo = strdup(todosJogos[j].titulo);
                novoJogo->dataLancamento = strdup(todosJogos[j].dataLancamento);
                adicionarNoFinal(listaJogos, novoJogo);
                break;
            }
        }
    }

    // Processar operações
    int quantidadeOperacoes;
    scanf("%d", &quantidadeOperacoes);
    getchar();

    for (idx = 0; idx < quantidadeOperacoes; idx++)
    {
        fgets(entrada, TAMANHO_MAX_CAMPO, stdin);
        entrada[strcspn(entrada, "\n")] = 0;

        char codigoOperacao[3] = {0};
        strncpy(codigoOperacao, entrada, 2);

        if (strcmp(codigoOperacao, "II") == 0)
        {
            int idAlvo = atoi(entrada + 3);
            for (int j = 0; j < totalJogos; j++)
            {
                if (todosJogos[j].identificador == idAlvo)
                {
                    Jogo *novoJogo = (Jogo *)malloc(sizeof(Jogo));
                    *novoJogo = todosJogos[j];
                    novoJogo->titulo = strdup(todosJogos[j].titulo);
                    novoJogo->dataLancamento = strdup(todosJogos[j].dataLancamento);
                    adicionarNoComeco(listaJogos, novoJogo);
                    break;
                }
            }
        }
        else if (strcmp(codigoOperacao, "IF") == 0)
        {
            int idAlvo = atoi(entrada + 3);
            for (int j = 0; j < totalJogos; j++)
            {
                if (todosJogos[j].identificador == idAlvo)
                {
                    Jogo *novoJogo = (Jogo *)malloc(sizeof(Jogo));
                    *novoJogo = todosJogos[j];
                    novoJogo->titulo = strdup(todosJogos[j].titulo);
                    novoJogo->dataLancamento = strdup(todosJogos[j].dataLancamento);
                    adicionarNoFinal(listaJogos, novoJogo);
                    break;
                }
            }
        }
        else if (strcmp(codigoOperacao, "I*") == 0)
        {
            char *token = strtok(entrada + 3, " ");
            int pos = atoi(token);
            token = strtok(NULL, " ");
            int idAlvo = atoi(token);

            for (int j = 0; j < totalJogos; j++)
            {
                if (todosJogos[j].identificador == idAlvo)
                {
                    Jogo *novoJogo = (Jogo *)malloc(sizeof(Jogo));
                    *novoJogo = todosJogos[j];
                    novoJogo->titulo = strdup(todosJogos[j].titulo);
                    novoJogo->dataLancamento = strdup(todosJogos[j].dataLancamento);
                    adicionarNaPosicao(listaJogos, novoJogo, pos);
                    break;
                }
            }
        }
        else if (strcmp(codigoOperacao, "RI") == 0)
        {
            Jogo *removido = extrairDoComeco(listaJogos);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->titulo);
                liberarMemoriaJogo(removido);
                free(removido);
            }
        }
        else if (strcmp(codigoOperacao, "RF") == 0)
        {
            Jogo *removido = extrairDoFinal(listaJogos);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->titulo);
                liberarMemoriaJogo(removido);
                free(removido);
            }
        }
        else if (strcmp(codigoOperacao, "R*") == 0)
        {
            int pos = atoi(entrada + 3);
            Jogo *removido = extrairDaPosicao(listaJogos, pos);
            if (removido != NULL)
            {
                printf("(R) %s\n", removido->titulo);
                liberarMemoriaJogo(removido);
                free(removido);
            }
        }
    }

    // Imprimir lista final
    mostrarListaCompleta(listaJogos);

    // Liberar memória
    destruirLista(listaJogos);

    for (idx = 0; idx < totalJogos; idx++)
    {
        liberarMemoriaJogo(&todosJogos[idx]);
    }
    free(todosJogos);

    for (idx = 0; idx < LIMITE_IDENTIFICADORES; idx++)
    {
        free(identificadoresEntrada[idx]);
    }
    free(identificadoresEntrada);

    return 0;
}