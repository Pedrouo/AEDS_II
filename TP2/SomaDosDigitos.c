#include <stdio.h>
#include <string.h>

int somaDigitos(char str[], int i) {
    if (i == (int)strlen(str)) {
        return 0;
    }
    // pega cada caracter na posição i, subtrai de '0' para conversão de inteiro, depois soma cada caracter com o próximo
    return (str[i] - '0') + somaDigitos(str, i + 1);
}

int main(void) {
    char linha[1000];

    if (fgets(linha, sizeof(linha), stdin) == NULL) return 0;

    int len = (int)strlen(linha);
    if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
    if (len > 0 && linha[len - 1] == '\r') linha[--len] = '\0';

    while (!(len == 3 && linha[0] == 'F' && linha[1] == 'I' && linha[2] == 'M')) {
        int soma = somaDigitos(linha, 0);
        printf("%d\n", soma);

        if (fgets(linha, sizeof(linha), stdin) == NULL) break;
        len = (int)strlen(linha);
        if (len > 0 && linha[len - 1] == '\n') linha[--len] = '\0';
        if (len > 0 && linha[len - 1] == '\r') linha[--len] = '\0';
    }

    return 0;
}
