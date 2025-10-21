#include <stdio.h>
#include <string.h>
#include <stdbool.h>

bool soVogais(char palavra[]) {
    int tam = (int)strlen(palavra) - 1;
    bool vogais = true;

    for (int i = 0; i <= tam; i++) {
        if (!(palavra[i] == 'a' || palavra[i] == 'A' || palavra[i] == 'e' || palavra[i] == 'E' ||
              palavra[i] == 'i' || palavra[i] == 'I' || palavra[i] == 'o' || palavra[i] == 'O' ||
              palavra[i] == 'u' || palavra[i] == 'U')) {
            vogais = false;
            i = tam; // sair do loop
        }
    }
    return vogais;
} // retorna true/false se a string possui apenas vogais

bool soConsoantes(char palavra[]) {
    int tam = (int)strlen(palavra) - 1;
    bool consoantes = true;

    for (int i = 0; i <= tam; i++) {
        if ((palavra[i] == 'a' || palavra[i] == 'A' || palavra[i] == 'e' || palavra[i] == 'E' ||
             palavra[i] == 'i' || palavra[i] == 'I' || palavra[i] == 'o' || palavra[i] == 'O' ||
             palavra[i] == 'u' || palavra[i] == 'U') ||
            (palavra[i] >= '0' && palavra[i] <= '9') ||
            (palavra[i] == ',' || palavra[i] == '.')) {
            consoantes = false;
            i = tam; // sair do loop
        }
    }
    return consoantes;
} // retorna true/false se a string possui apenas consoantes

bool inteiros(char palavra[]) {
    int tam = (int)strlen(palavra) - 1;
    bool inteiro = true;

    for (int i = 0; i <= tam; i++) {
        if (!(palavra[i] >= '0' && palavra[i] <= '9')) {
            inteiro = false;
            i = tam; // sair do loop
        }
    }
    return inteiro;
}

bool reais(char palavra[]) {
    int contarPontos = 0;
    int tam = (int)strlen(palavra) - 1;
    bool real = true;

    for (int i = 0; i <= tam; i++) {
        if (palavra[i] == ',' || palavra[i] == '.') {
            contarPontos++;
            if (contarPontos > 1) {
                real = false;
                i = tam; // sair do loop
            }
        } else if (!(palavra[i] >= '0' && palavra[i] <= '9')) {
            real = false;
            i = tam; // sair do loop
        }
    }
    return real;
}

int main(void) {
    char linha[1000];

    // lê a primeira linha
    if (fgets(linha, sizeof(linha), stdin) == NULL) return 0;

    // remove \n e \r
    int len = (int)strlen(linha);
    if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
    if (len > 0 && linha[len - 1] == '\r') linha[--len] = '\0';

    while (!(len == 3 && linha[0] == 'F' && linha[1] == 'I' && linha[2] == 'M')) {
        const char *x1 = soVogais(linha) ? "SIM" : "NAO";
        const char *x2 = soConsoantes(linha) ? "SIM" : "NAO";
        const char *x3 = inteiros(linha) ? "SIM" : "NAO";
        const char *x4 = reais(linha) ? "SIM" : "NAO";

        printf("%s %s %s %s\n", x1, x2, x3, x4);

        if (fgets(linha, sizeof(linha), stdin) == NULL) break;
        len = (int)strlen(linha);
        if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
        if (len > 0 && linha[len - 1] == '\r') linha[--len] = '\0';
    }

    return 0;
}
