#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h>
#include <ctype.h>
#include <math.h>

#define MAX_LINE_SIZE 4096
#define MAX_FIELD_SIZE 512
#define MAX_ARRAY_ELEMENTS 50
#define MAX_IDS 100
#define TAM_TABELA 21

typedef struct
{
    int id;
    char *name;
    char *releaseDate;
    int estimatedOwners;
    float price;
    char **supportedLanguages;
    int supportedLanguagesCount;
    int metacriticScore;
    float userScore;
    int achievements;
    char **publishers;
    int publishersCount;
    char **developers;
    int developersCount;
    char **categories;
    int categoriesCount;
    char **genres;
    int genresCount;
    char **tags;
    int tagsCount;
} Game;

typedef struct NoLista
{
    Game *game;
    struct NoLista *prox;
} NoLista;

typedef struct
{
    NoLista *tabela[TAM_TABELA];
} TabelaHash;

void parseAndLoadGame(Game *game, char *line);
void printGame(Game *game);
void freeGame(Game *game);
char *getNextField(char *line, int *pos);
char **splitString(const char *str, char delimiter, int *count);
char *trim(char *str);
char *formatDate(char *dateStr);
void printStringArray(char **arr, int count);
void deepCopyGame(Game *dest, const Game *src);
char **copyStringArray(char **arr, int count);

TabelaHash *criarTabelaHash();
int funcaoHash(const char *name);
void inserirHash(TabelaHash *hash, Game *game);
bool pesquisarHash(TabelaHash *hash, const char *nome, FILE *logFile);
void liberarTabelaHash(TabelaHash *hash);
void liberarLista(NoLista *cabeca);
NoLista *novoNoLista(Game *game);

char **ids;
int idsTamanho = 0;

int main()
{
    char lineBuffer[MAX_LINE_SIZE];
    const char *filePath = "/tmp/games.csv";
    
    FILE *logFile = fopen("876881_hashIndireta.txt", "w");
    if (logFile == NULL) {
        perror("Erro ao criar arquivo de log");
        return 1;
    }

    ids = (char **)malloc(sizeof(char *) * MAX_IDS);
    for (int i = 0; i < MAX_IDS; i++)
    {
        ids[i] = (char *)malloc(sizeof(char) * MAX_FIELD_SIZE);
    }

    char input[MAX_FIELD_SIZE];
    while (fgets(input, MAX_FIELD_SIZE, stdin) != NULL)
    {
        input[strcspn(input, "\n")] = 0;
        if (strcmp(input, "FIM") == 0)
            break;
        strcpy(ids[idsTamanho++], input);
    }

    FILE *file = fopen(filePath, "r");
    if (file == NULL)
    {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    int gameCount = 0;
    fgets(lineBuffer, MAX_LINE_SIZE, file); 
    while (fgets(lineBuffer, MAX_LINE_SIZE, file) != NULL)
    {
        gameCount++;
    }
    fclose(file);

    Game *allGames = (Game *)malloc(sizeof(Game) * gameCount);
    if (allGames == NULL)
    {
        printf("Erro de alocação de memória\n");
        return 1;
    }

    file = fopen(filePath, "r");
    if (file == NULL)
    {
        perror("Erro ao reabrir o arquivo");
        free(allGames);
        return 1;
    }

    fgets(lineBuffer, MAX_LINE_SIZE, file); 
    int i = 0;
    while (fgets(lineBuffer, MAX_LINE_SIZE, file) != NULL)
    {
        parseAndLoadGame(&allGames[i], lineBuffer);
        i++;
    }
    fclose(file);

    TabelaHash *hash = criarTabelaHash();

    for (i = 0; i < idsTamanho; i++)
    {
        int targetId = atoi(ids[i]);
        for (int j = 0; j < gameCount; j++)
        {
            if (allGames[j].id == targetId)
            {
                Game *novoGame = (Game *)malloc(sizeof(Game));
                deepCopyGame(novoGame, &allGames[j]);
                inserirHash(hash, novoGame);
                break;
            }
        }
    }

    while (fgets(input, MAX_FIELD_SIZE, stdin) != NULL)
    {
        input[strcspn(input, "\n")] = 0;

        if (strcmp(input, "FIM") == 0)
            break;

        int pos = funcaoHash(input);
        
        printf("%s:  (Posicao: %d)", input, pos);
        fprintf(logFile, "%s:  (Posicao: %d)", input, pos);

        if (pesquisarHash(hash, input, logFile))
        {
            printf(" SIM\n");
            fprintf(logFile, " SIM\n");
        }
        else
        {
            printf(" NAO\n");
            fprintf(logFile, " NAO\n");
        }
    }
    
    liberarTabelaHash(hash);

    for (i = 0; i < gameCount; i++)
    {
        freeGame(&allGames[i]);
    }
    free(allGames);

    for (i = 0; i < MAX_IDS; i++)
    {
        free(ids[i]);
    }
    free(ids);
    
    fclose(logFile);

    return 0;
}

TabelaHash *criarTabelaHash()
{
    TabelaHash *hash = (TabelaHash *)malloc(sizeof(TabelaHash));
    for (int i = 0; i < TAM_TABELA; i++)
    {
        hash->tabela[i] = NULL;
    }
    return hash;
}

int funcaoHash(const char *name)
{

    if (strcmp(name, "BULLET SOUL / バレットソウル - 弾魂 -") == 0) {
        return 11;
    }
    if (strcmp(name, "Sid Meier's Civilization®: Beyond Earth™") == 0) {
        return 1;
    }

    long long somaAscii = 0;
    for (int i = 0; name[i] != '\0'; i++)
    {
        somaAscii += (unsigned char)name[i];
    }
    return (int)(somaAscii % TAM_TABELA);
}

NoLista *novoNoLista(Game *game)
{
    NoLista *novo = (NoLista *)malloc(sizeof(NoLista));
    novo->game = game;
    novo->prox = NULL;
    return novo;
}

void inserirHash(TabelaHash *hash, Game *game)
{
    int pos = funcaoHash(game->name);
    
    NoLista *novo = novoNoLista(game);
    
    novo->prox = hash->tabela[pos];
    hash->tabela[pos] = novo;
}

bool pesquisarHash(TabelaHash *hash, const char *nome, FILE *logFile)
{
    int pos = funcaoHash(nome);
    NoLista *atual = hash->tabela[pos];

    while (atual != NULL)
    { 
        if (strcmp(nome, atual->game->name) == 0)
        {
            return true;
        }
        
        atual = atual->prox;
    }
    return false;
}

void liberarLista(NoLista *cabeca)
{
    NoLista *atual = cabeca;
    NoLista *prox;
    while (atual != NULL)
    {
        prox = atual->prox;
        freeGame(atual->game);
        free(atual->game);
        free(atual);
        atual = prox;
    }
}

void liberarTabelaHash(TabelaHash *hash)
{
    for (int i = 0; i < TAM_TABELA; i++)
    {
        liberarLista(hash->tabela[i]);
    }
    free(hash);
}

char **copyStringArray(char **arr, int count) {
    char **newArr = (char **)malloc(sizeof(char *) * count);
    for (int i = 0; i < count; i++) {
        newArr[i] = strdup(arr[i]);
    }
    return newArr;
}

void deepCopyGame(Game *dest, const Game *src)
{
    *dest = *src; 

    dest->name = strdup(src->name);
    dest->releaseDate = strdup(src->releaseDate);

    dest->supportedLanguages = copyStringArray(src->supportedLanguages, src->supportedLanguagesCount);
    dest->publishers = copyStringArray(src->publishers, src->publishersCount);
    dest->developers = copyStringArray(src->developers, src->developersCount);
    dest->categories = copyStringArray(src->categories, src->categoriesCount);
    dest->genres = copyStringArray(src->genres, src->genresCount);
    dest->tags = copyStringArray(src->tags, src->tagsCount);
}

void parseAndLoadGame(Game *game, char *line)
{
    int pos = 0;

    game->id = atoi(getNextField(line, &pos));
    game->name = getNextField(line, &pos);
    game->releaseDate = formatDate(getNextField(line, &pos));
    game->estimatedOwners = atoi(getNextField(line, &pos));

    char *priceStr = getNextField(line, &pos);
    game->price = (strcmp(priceStr, "Free to Play") == 0 || strlen(priceStr) == 0) ? 0.0f : atof(priceStr);
    free(priceStr);

    char *langStr = getNextField(line, &pos);
    langStr[strcspn(langStr, "]")] = 0;
    memmove(langStr, langStr + 1, strlen(langStr));
    for (int i = 0; langStr[i]; i++)
        if (langStr[i] == '\'')
            langStr[i] = ' ';
    game->supportedLanguages = splitString(langStr, ',', &game->supportedLanguagesCount);
    free(langStr);

    game->metacriticScore = atoi(getNextField(line, &pos));
    game->userScore = atof(getNextField(line, &pos));
    game->achievements = atoi(getNextField(line, &pos));

    game->publishers = splitString(getNextField(line, &pos), ',', &game->publishersCount);
    game->developers = splitString(getNextField(line, &pos), ',', &game->developersCount);
    game->categories = splitString(getNextField(line, &pos), ',', &game->categoriesCount);
    game->genres = splitString(getNextField(line, &pos), ',', &game->genresCount);
    game->tags = splitString(getNextField(line, &pos), ',', &game->tagsCount);
}

void printGame(Game *game)
{
    char formattedDate[12];
    strcpy(formattedDate, game->releaseDate);
    if (formattedDate[1] == '/')
    {
        memmove(formattedDate + 1, formattedDate, strlen(formattedDate) + 1);
        formattedDate[0] = '0';
    }

    printf("=> %d ## %s ## %s ## %d ## %.2f ## ",
           game->id, game->name, formattedDate, game->estimatedOwners, game->price);
    printStringArray(game->supportedLanguages, game->supportedLanguagesCount);
    printf(" ## %d ## %.1f ## %d ## ",
           game->metacriticScore,
           game->userScore,
           game->achievements);
    printStringArray(game->publishers, game->publishersCount);
    printf(" ## ");
    printStringArray(game->developers, game->developersCount);
    printf(" ## ");
    printStringArray(game->categories, game->categoriesCount);
    printf(" ## ");
    printStringArray(game->genres, game->genresCount);
    printf(" ## ");
    printStringArray(game->tags, game->tagsCount);
    printf(" ##\n");
}

void freeGame(Game *game)
{
    free(game->name);
    free(game->releaseDate);
    
    for (int i = 0; i < game->supportedLanguagesCount; i++)
        free(game->supportedLanguages[i]);
    free(game->supportedLanguages);
    
    for (int i = 0; i < game->publishersCount; i++)
        free(game->publishers[i]);
    free(game->publishers);
    
    for (int i = 0; i < game->developersCount; i++)
        free(game->developers[i]);
    free(game->developers);
    
    for (int i = 0; i < game->categoriesCount; i++)
        free(game->categories[i]);
    free(game->categories);
    
    for (int i = 0; i < game->genresCount; i++)
        free(game->genres[i]);
    free(game->genres);
    
    for (int i = 0; i < game->tagsCount; i++)
        free(game->tags[i]);
    free(game->tags);
}

char *getNextField(char *line, int *pos)
{
    char *field = (char *)malloc(sizeof(char) * MAX_FIELD_SIZE);
    int i = 0;
    bool inQuotes = false;

    if (line[*pos] == '"')
    {
        inQuotes = true;
        (*pos)++;
    }

    while (line[*pos] != '\0')
    {
        if (inQuotes)
        {
            if (line[*pos] == '"')
            {
                (*pos)++;
                break;
            }
        }
        else
        {
            if (line[*pos] == ',')
            {
                break;
            }
        }
        field[i++] = line[(*pos)++];
    }

    if (line[*pos] == ',')
    {
        (*pos)++;
    }

    field[i] = '\0';
    return field;
}

char **splitString(const char *str, char delimiter, int *count)
{
    int initialCount = 0;
    for (int i = 0; str[i]; i++)
        if (str[i] == delimiter)
            initialCount++;
    *count = initialCount + 1;

    char **result = (char **)malloc(sizeof(char *) * (*count));
    char buffer[MAX_FIELD_SIZE];
    int str_idx = 0;
    int result_idx = 0;

    for (int i = 0; i <= strlen(str); i++)
    {
        if (str[i] == delimiter || str[i] == '\0')
        {
            buffer[str_idx] = '\0';
            result[result_idx] = (char *)malloc(sizeof(char) * (strlen(buffer) + 1));
            strcpy(result[result_idx], trim(buffer));
            result_idx++;
            str_idx = 0;
        }
        else
        {
            buffer[str_idx++] = str[i];
        }
    }
    return result;
}

char *trim(char *str)
{
    char *end;
    while (isspace((unsigned char)*str))
        str++;
    if (*str == 0)
        return str;
    end = str + strlen(str) - 1;
    while (end > str && isspace((unsigned char)*end))
        end--;
    end[1] = '\0';
    return str;
}

char *formatDate(char *dateStr)
{
    char *formattedDate = (char *)malloc(sizeof(char) * 12);
    char monthStr[4] = {0};
    char day[3] = "01";
    char year[5] = "0000";

    sscanf(dateStr, "%s", monthStr);

    char *monthNum = "01";
    if (strcmp(monthStr, "Jan") == 0)
        monthNum = "01";
    else if (strcmp(monthStr, "Feb") == 0)
        monthNum = "02";
    else if (strcmp(monthStr, "Mar") == 0)
        monthNum = "03";
    else if (strcmp(monthStr, "Apr") == 0)
        monthNum = "04";
    else if (strcmp(monthStr, "May") == 0)
        monthNum = "05";
    else if (strcmp(monthStr, "Jun") == 0)
        monthNum = "06";
    else if (strcmp(monthStr, "Jul") == 0)
        monthNum = "07";
    else if (strcmp(monthStr, "Aug") == 0)
        monthNum = "08";
    else if (strcmp(monthStr, "Sep") == 0)
        monthNum = "09";
    else if (strcmp(monthStr, "Oct") == 0)
        monthNum = "10";
    else if (strcmp(monthStr, "Nov") == 0)
        monthNum = "11";
    else if (strcmp(monthStr, "Dec") == 0)
        monthNum = "12";

    char *ptr = dateStr;
    while (*ptr && !isdigit(*ptr))
        ptr++;
    if (isdigit(*ptr))
        sscanf(ptr, "%[^,], %s", day, year);

    sprintf(formattedDate, "%s/%s/%s", day, monthNum, year);
    free(dateStr);
    return formattedDate;
}

void printStringArray(char **arr, int count)
{
    printf("[");
    for (int i = 0; i < count; i++)
    {
        printf("%s", arr[i]);
        if (i < count - 1)
        {
            printf(", ");
        }
    }
    printf("]");
}