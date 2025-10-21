#include <stdio.h>
#include <stdbool.h>
#include <string.h>


bool ehPalindromo(char texto[], int i, int j) {
    if (i >= j) return true;           // caso base: cruzou/igualou índices
    if (texto[i] != texto[j]) return false; // falha se extremos diferem
    return ehPalindromo(texto, i + 1, j - 1);
}

int main(){
    char texto[1000];

    scanf(" %999[^\r\n]", texto);

    int tam = (int)strlen(texto);

    while (strcmp(texto, "FIM") != 0) {    // para quando for exatamente "FIM"
        if (ehPalindromo(texto, 0, tam - 1)) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }

       scanf(" %999[^\r\n]", texto);

        tam = (int)strlen(texto);      // recalcula tamanho para a próxima checagem
    }

    return 0;
}
