#include <stdio.h>
#include <string.h>
#include <stdbool.h>

static bool soVogais_rec(const char *palavra, int i) {
    int tam = (int)strlen(palavra);
    if (i < tam) {
        char c = palavra[i];
        if (!(c == 'a' || c == 'A' || c == 'e' || c == 'E' ||
              c == 'i' || c == 'I' || c == 'o' || c == 'O' ||
              c == 'u' || c == 'U')) {
            return false;
        }
        return soVogais_rec(palavra, i + 1);
    } else {
        return true;
    }
}
static bool soVogais(const char *palavra) {
    return soVogais_rec(palavra, 0);
}

static bool soConsoantes_rec(const char *palavra, int i) {
    int tam = (int)strlen(palavra);
    if (i < tam) {
        char c = palavra[i];

        // se for vogal -> falso
        if (c == 'a' || c == 'A' || c == 'e' || c == 'E' ||
            c == 'i' || c == 'I' || c == 'o' || c == 'O' ||
            c == 'u' || c == 'U') {
            return false;
        }
        // se for dígito -> falso
        if (c >= '0' && c <= '9') {
            return false;
        }

        return soConsoantes_rec(palavra, i + 1);
    } else {
        return true;
    }
}
static bool soConsoantes(const char *palavra) {
    return soConsoantes_rec(palavra, 0);
}


static bool inteiros_rec(const char *palavra, int i) {
    int tam = (int)strlen(palavra);
    if (i < tam) {
        char c = palavra[i];
        if (!(c >= '0' && c <= '9')) {
            return false;
        }
        return inteiros_rec(palavra, i + 1);
    } else {
        return true;
    }
}
static bool inteiros(const char *palavra) {
    return inteiros_rec(palavra, 0);
}


static bool reais_rec(const char *palavra, int i, int contarPontos) {
    int tam = (int)strlen(palavra);
    if (i < tam) {
        char c = palavra[i];

        if (c == '.' || c == ',') {
            contarPontos++;
            if (contarPontos > 1) return false;
        } else if (c < '0' || c > '9') {
            return false;
        }
        return reais_rec(palavra, i + 1, contarPontos);
    } else {
        return true;
    }
}
static bool reais(const char *palavra) {
    return reais_rec(palavra, 0, 0);
}

int main(void) {
    char linha[1024];

    while (fgets(linha, sizeof linha, stdin)) {
        // remove '\n' se houver
        linha[strcspn(linha, "\n")] = '\0';

        if (strlen(linha) == 3 &&
            linha[0] == 'F' && linha[1] == 'I' && linha[2] == 'M') {
            break;
        }

        const char *x1 = soVogais(linha)      ? "SIM" : "NAO";
        const char *x2 = soConsoantes(linha)  ? "SIM" : "NAO";
        const char *x3 = inteiros(linha)      ? "SIM" : "NAO";
        const char *x4 = reais(linha)         ? "SIM" : "NAO";

        printf("%s %s %s %s\n", x1, x2, x3, x4);
    }

    return 0;
}
