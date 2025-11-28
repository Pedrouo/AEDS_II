#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>
#include <ctype.h>
#include <math.h>

// --- Constantes e Definições ---
#define TAM_BUFFER 4096
#define TAM_CAMPO 512
#define MAX_LISTA 50
#define MAX_INPUT_IDS 100

// --- Estruturas de Dados ---

// Entidade principal (antigo Game)
typedef struct {
    int id_game;
    char *titulo;
    char *data_lancamento;
    int proprietarios;
    float valor;
    char **idiomas;
    int qtd_idiomas;
    int score_meta;
    float score_user;
    int conquistas;
    char **pubs;
    int qtd_pubs;
    char **devs;
    int qtd_devs;
    char **cats;
    int qtd_cats;
    char **gens;
    int qtd_gens;
    char **tags;
    int qtd_tags;
} RegistroJogo;

// Nó da Árvore (antigo NoAVL)
typedef struct Nodo {
    RegistroJogo *info;
    int nivel; // Altura
    struct Nodo *left, *right;
} Nodo;

// Wrapper da Árvore
typedef struct {
    Nodo *topo;
} EstruturaBusca;

// --- Variáveis Globais ---
char **listaBuscaIds;
int totalBuscaIds = 0;

// --- Protótipos Atualizados ---
void carregarLinhaCSV(RegistroJogo *obj, char *linha);
void apagarRegistro(RegistroJogo *obj);
char *extrairToken(char *linha, int *cursor);
char **quebrarTexto(const char *texto, char delimitador, int *outCount);
char *limparEspacos(char *s);
char *ajustarData(char *dt);
void clonarRegistro(RegistroJogo *destino, const RegistroJogo *origem);

EstruturaBusca *initArvore();
int getNivel(Nodo *n);
int calcBalance(Nodo *n);
int maxInt(int x, int y);
Nodo *rotacionarDir(Nodo *y);
Nodo *rotacionarEsq(Nodo *x);
void addNode(EstruturaBusca *estrutura, RegistroJogo *dado);
bool buscarElemento(EstruturaBusca *estrutura, const char *chave, FILE *arquivoLog);
void destruirArvore(EstruturaBusca *estrutura);

// --- Implementação ---

int main() {
    char buffer[TAM_BUFFER];
    const char *caminhoCSV = "/tmp/games.csv";
    
    // Arquivo de Log mantido conforme original
    FILE *arqLog = fopen("876881_av.txt", "w");
    if (!arqLog) {
        perror("Falha ao criar log");
        return 1;
    }

    // Inicialização da lista de IDs
    listaBuscaIds = (char **)malloc(sizeof(char *) * MAX_INPUT_IDS);
    for (int k = 0; k < MAX_INPUT_IDS; k++) {
        listaBuscaIds[k] = (char *)malloc(sizeof(char) * TAM_CAMPO);
    }

    // Leitura da entrada padrão (IDs)
    char entrada[TAM_CAMPO];
    while (fgets(entrada, TAM_CAMPO, stdin) != NULL) {
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;
        strcpy(listaBuscaIds[totalBuscaIds++], entrada);
    }

    // Contagem de linhas do arquivo
    FILE *stream = fopen(caminhoCSV, "r");
    if (!stream) {
        perror("CSV nao encontrado");
        return 1;
    }

    int totalJogos = 0;
    fgets(buffer, TAM_BUFFER, stream); // Ignorar header
    while (fgets(buffer, TAM_BUFFER, stream)) {
        totalJogos++;
    }
    fclose(stream);

    // Carga dos dados em memória
    RegistroJogo *bancoDados = (RegistroJogo *)malloc(sizeof(RegistroJogo) * totalJogos);
    if (!bancoDados) return 1;

    stream = fopen(caminhoCSV, "r");
    fgets(buffer, TAM_BUFFER, stream); // Header
    int idx = 0;
    while (fgets(buffer, TAM_BUFFER, stream)) {
        carregarLinhaCSV(&bancoDados[idx], buffer);
        idx++;
    }
    fclose(stream);

    // Construção da Árvore AVL
    EstruturaBusca *minhaArvore = initArvore();

    for (int k = 0; k < totalBuscaIds; k++) {
        int idAlvo = atoi(listaBuscaIds[k]);
        for (int j = 0; j < totalJogos; j++) {
            if (bancoDados[j].id_game == idAlvo) {
                RegistroJogo *novoItem = (RegistroJogo *)malloc(sizeof(RegistroJogo));
                clonarRegistro(novoItem, &bancoDados[j]);
                addNode(minhaArvore, novoItem);
                break;
            }
        }
    }

    // Processamento das Buscas
    while (fgets(entrada, TAM_CAMPO, stdin)) {
        entrada[strcspn(entrada, "\n")] = 0;
        if (strcmp(entrada, "FIM") == 0) break;

        printf("%s: ", entrada);
        fprintf(arqLog, "%s: ", entrada);

        if (buscarElemento(minhaArvore, entrada, arqLog)) {
            printf("SIM\n");
            fprintf(arqLog, "SIM\n");
        } else {
            printf("NAO\n");
            fprintf(arqLog, "NAO\n");
        }
    }
    
    // Limpeza Geral
    destruirArvore(minhaArvore);

    for (int k = 0; k < totalJogos; k++) {
        apagarRegistro(&bancoDados[k]);
    }
    free(bancoDados);

    for (int k = 0; k < MAX_INPUT_IDS; k++) {
        free(listaBuscaIds[k]);
    }
    free(listaBuscaIds);
    fclose(arqLog);

    return 0;
}

// --- Funções da Árvore AVL ---

EstruturaBusca *initArvore() {
    EstruturaBusca *t = (EstruturaBusca *)malloc(sizeof(EstruturaBusca));
    t->topo = NULL;
    return t;
}

int getNivel(Nodo *n) {
    return (n == NULL) ? 0 : n->nivel;
}

int maxVal(int a, int b) {
    return (a > b) ? a : b;
}

int calcBalance(Nodo *n) {
    if (!n) return 0;
    return getNivel(n->right) - getNivel(n->left);
}

Nodo *criarNo(RegistroJogo *dado) {
    Nodo *novo = (Nodo *)malloc(sizeof(Nodo));
    novo->info = dado;
    novo->left = novo->right = NULL;
    novo->nivel = 1;
    return novo;
}

Nodo *rotacionarDir(Nodo *y) {
    Nodo *x = y->left;
    Nodo *T2 = x->right;

    x->right = y;
    y->left = T2;

    y->nivel = maxVal(getNivel(y->left), getNivel(y->right)) + 1;
    x->nivel = maxVal(getNivel(x->left), getNivel(x->right)) + 1;

    return x;
}

Nodo *rotacionarEsq(Nodo *x) {
    Nodo *y = x->right;
    Nodo *T2 = y->left;

    y->left = x;
    x->right = T2;

    x->nivel = maxVal(getNivel(x->left), getNivel(x->right)) + 1;
    y->nivel = maxVal(getNivel(y->left), getNivel(y->right)) + 1;

    return y;
}

Nodo *inserirRecursivo(Nodo *node, RegistroJogo *dado) {
    if (node == NULL) return criarNo(dado);

    int cmp = strcmp(dado->titulo, node->info->titulo);

    if (cmp < 0)
        node->left = inserirRecursivo(node->left, dado);
    else if (cmp > 0)
        node->right = inserirRecursivo(node->right, dado);
    else {
        // Duplicado: libera e retorna o existente
        apagarRegistro(dado);
        free(dado);
        return node; 
    }

    node->nivel = 1 + maxVal(getNivel(node->left), getNivel(node->right));
    int bal = calcBalance(node);

    // Casos de Rotação
    if (bal < -1 && strcmp(dado->titulo, node->left->info->titulo) < 0)
        return rotacionarDir(node);

    if (bal > 1 && strcmp(dado->titulo, node->right->info->titulo) > 0)
        return rotacionarEsq(node);

    if (bal < -1 && strcmp(dado->titulo, node->left->info->titulo) > 0) {
        node->left = rotacionarEsq(node->left);
        return rotacionarDir(node);
    }

    if (bal > 1 && strcmp(dado->titulo, node->right->info->titulo) < 0) {
        node->right = rotacionarDir(node->right);
        return rotacionarEsq(node);
    }

    return node;
}

void addNode(EstruturaBusca *estrutura, RegistroJogo *dado) {
    estrutura->topo = inserirRecursivo(estrutura->topo, dado);
}

bool buscaRec(Nodo *no, const char *chave, FILE *log) {
    if (!no) return false;

    int res = strcmp(chave, no->info->titulo);

    if (res == 0) {
        return true;
    } else if (res < 0) {
        printf("esq ");
        fprintf(log, "esq ");
        return buscaRec(no->left, chave, log);
    } else {
        printf("dir ");
        fprintf(log, "dir ");
        return buscaRec(no->right, chave, log);
    }
}

bool buscarElemento(EstruturaBusca *estrutura, const char *chave, FILE *arquivoLog) {
    if (estrutura->topo != NULL) {
        printf("raiz ");
        fprintf(arquivoLog, "raiz ");
        return buscaRec(estrutura->topo, chave, arquivoLog);
    }
    return false;
}

void liberarNos(Nodo *no) {
    if (no) {
        liberarNos(no->left);
        liberarNos(no->right);
        apagarRegistro(no->info);
        free(no->info);
        free(no);
    }
}

void destruirArvore(EstruturaBusca *estrutura) {
    liberarNos(estrutura->topo);
    free(estrutura);
}

// --- Funções Auxiliares de Dados e Parsing ---

char **duplicarVetorStrings(char **arr, int qtd) {
    char **novoArr = (char **)malloc(sizeof(char *) * qtd);
    for (int i = 0; i < qtd; i++) {
        novoArr[i] = strdup(arr[i]);
    }
    return novoArr;
}

void clonarRegistro(RegistroJogo *destino, const RegistroJogo *origem) {
    *destino = *origem; // Copia primitivos

    destino->titulo = strdup(origem->titulo);
    destino->data_lancamento = strdup(origem->data_lancamento);

    destino->idiomas = duplicarVetorStrings(origem->idiomas, origem->qtd_idiomas);
    destino->pubs = duplicarVetorStrings(origem->pubs, origem->qtd_pubs);
    destino->devs = duplicarVetorStrings(origem->devs, origem->qtd_devs);
    destino->cats = duplicarVetorStrings(origem->cats, origem->qtd_cats);
    destino->gens = duplicarVetorStrings(origem->gens, origem->qtd_gens);
    destino->tags = duplicarVetorStrings(origem->tags, origem->qtd_tags);
}

void carregarLinhaCSV(RegistroJogo *obj, char *linha) {
    int cursor = 0;

    obj->id_game = atoi(extrairToken(linha, &cursor));
    obj->titulo = extrairToken(linha, &cursor);
    obj->data_lancamento = ajustarData(extrairToken(linha, &cursor));
    obj->proprietarios = atoi(extrairToken(linha, &cursor));

    char *tmpPrice = extrairToken(linha, &cursor);
    obj->valor = (strcmp(tmpPrice, "Free to Play") == 0 || strlen(tmpPrice) == 0) ? 0.0f : atof(tmpPrice);
    free(tmpPrice);

    char *tmpLangs = extrairToken(linha, &cursor);
    tmpLangs[strcspn(tmpLangs, "]")] = 0; // Remove fecha colchete
    memmove(tmpLangs, tmpLangs + 1, strlen(tmpLangs)); // Remove abre colchete
    for (int i = 0; tmpLangs[i]; i++)
        if (tmpLangs[i] == '\'') tmpLangs[i] = ' ';
    
    obj->idiomas = quebrarTexto(tmpLangs, ',', &obj->qtd_idiomas);
    free(tmpLangs);

    obj->score_meta = atoi(extrairToken(linha, &cursor));
    obj->score_user = atof(extrairToken(linha, &cursor));
    obj->conquistas = atoi(extrairToken(linha, &cursor));

    obj->pubs = quebrarTexto(extrairToken(linha, &cursor), ',', &obj->qtd_pubs);
    obj->devs = quebrarTexto(extrairToken(linha, &cursor), ',', &obj->qtd_devs);
    obj->cats = quebrarTexto(extrairToken(linha, &cursor), ',', &obj->qtd_cats);
    obj->gens = quebrarTexto(extrairToken(linha, &cursor), ',', &obj->qtd_gens);
    obj->tags = quebrarTexto(extrairToken(linha, &cursor), ',', &obj->qtd_tags);
}

void apagarRegistro(RegistroJogo *obj) {
    free(obj->titulo);
    free(obj->data_lancamento);
    
    // Macros para liberar vetores poderiam ser usadas, mas loop é mais claro
    for(int i=0; i < obj->qtd_idiomas; i++) free(obj->idiomas[i]); free(obj->idiomas);
    for(int i=0; i < obj->qtd_pubs; i++) free(obj->pubs[i]); free(obj->pubs);
    for(int i=0; i < obj->qtd_devs; i++) free(obj->devs[i]); free(obj->devs);
    for(int i=0; i < obj->qtd_cats; i++) free(obj->cats[i]); free(obj->cats);
    for(int i=0; i < obj->qtd_gens; i++) free(obj->gens[i]); free(obj->gens);
    for(int i=0; i < obj->qtd_tags; i++) free(obj->tags[i]); free(obj->tags);
}

char *extrairToken(char *linha, int *cursor) {
    char *buffer = (char *)malloc(sizeof(char) * TAM_CAMPO);
    int i = 0;
    bool aspas = false;

    if (linha[*cursor] == '"') {
        aspas = true;
        (*cursor)++;
    }

    while (linha[*cursor] != '\0') {
        if (aspas) {
            if (linha[*cursor] == '"') {
                (*cursor)++;
                break;
            }
        } else {
            if (linha[*cursor] == ',') break;
        }
        buffer[i++] = linha[(*cursor)++];
    }

    if (linha[*cursor] == ',') (*cursor)++;

    buffer[i] = '\0';
    return buffer;
}

char **quebrarTexto(const char *texto, char delimitador, int *outCount) {
    int contagem = 0;
    for (int i = 0; texto[i]; i++)
        if (texto[i] == delimitador) contagem++;
    *outCount = contagem + 1;

    char **resultado = (char **)malloc(sizeof(char *) * (*outCount));
    char bufTemp[TAM_CAMPO];
    int idxStr = 0;
    int idxRes = 0;

    for (int i = 0; i <= strlen(texto); i++) {
        if (texto[i] == delimitador || texto[i] == '\0') {
            bufTemp[idxStr] = '\0';
            resultado[idxRes] = (char *)malloc(sizeof(char) * (strlen(bufTemp) + 1));
            strcpy(resultado[idxRes], limparEspacos(bufTemp));
            idxRes++;
            idxStr = 0;
        } else {
            bufTemp[idxStr++] = texto[i];
        }
    }
    return resultado;
}

char *limparEspacos(char *s) {
    while (isspace((unsigned char)*s)) s++;
    if (*s == 0) return s;
    char *fim = s + strlen(s) - 1;
    while (fim > s && isspace((unsigned char)*fim)) fim--;
    fim[1] = '\0';
    return s;
}

char *ajustarData(char *dt) {
    char *novaData = (char *)malloc(sizeof(char) * 12);
    char sMes[4] = {0};
    char sDia[3] = "01";
    char sAno[5] = "0000";

    sscanf(dt, "%s", sMes);

    char *nMes = "01";
    if (strcmp(sMes, "Jan") == 0) nMes = "01";
    else if (strcmp(sMes, "Feb") == 0) nMes = "02";
    else if (strcmp(sMes, "Mar") == 0) nMes = "03";
    else if (strcmp(sMes, "Apr") == 0) nMes = "04";
    else if (strcmp(sMes, "May") == 0) nMes = "05";
    else if (strcmp(sMes, "Jun") == 0) nMes = "06";
    else if (strcmp(sMes, "Jul") == 0) nMes = "07";
    else if (strcmp(sMes, "Aug") == 0) nMes = "08";
    else if (strcmp(sMes, "Sep") == 0) nMes = "09";
    else if (strcmp(sMes, "Oct") == 0) nMes = "10";
    else if (strcmp(sMes, "Nov") == 0) nMes = "11";
    else if (strcmp(sMes, "Dec") == 0) nMes = "12";

    char *p = dt;
    while (*p && !isdigit(*p)) p++;
    if (isdigit(*p)) sscanf(p, "%[^,], %s", sDia, sAno);

    sprintf(novaData, "%s/%s/%s", sDia, nMes, sAno);
    free(dt);
    return novaData;
}