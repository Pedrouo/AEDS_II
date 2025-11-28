import java.io.*;
import java.util.Scanner;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mergesort {

    private static final String MATRICULA = "876881";

    private static long movements = 0;
    private static long comparisons = 0;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("/tmp/games.csv"));
        br.readLine();
        Game[] lista = new Game[60000];
        int listaCount = 0;
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] fields = CsvParser.parseLine(linha);
            if (fields == null || fields.length < 14) continue;
            lista[listaCount++] = Game.fromCsvFields(fields);
        }
        br.close();

        int[] ids = new int[listaCount];
        int[] counts = new int[listaCount];
        int uniqueCount = 0;
        
        for (int i = 0; i < listaCount; i++) {
            int currentId = lista[i].id;
            boolean found = false;
            for (int j = 0; j < uniqueCount; j++) {
                if (ids[j] == currentId) {
                    counts[j]++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                ids[uniqueCount] = currentId;
                counts[uniqueCount] = 1;
                uniqueCount++;
            }
        }
        
        for (int i = 0; i < uniqueCount; i++) {
            if (counts[i] > 1) {
                System.err.println("Duplicado AppID=" + ids[i] + " vezes=" + counts[i]);
            }
        }

        Game[] arr = new Game[listaCount];
        int[] seenIds = new int[listaCount];
        int seenCount = 0;
        int arrCount = 0;
        
        for (int i = 0; i < listaCount; i++) {
            boolean found = false;
            for (int j = 0; j < seenCount; j++) {
                if (seenIds[j] == lista[i].id) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                arr[arrCount++] = lista[i];
                seenIds[seenCount++] = lista[i].id;
            }
        }

        Game[] arrById = new Game[arrCount];
        for (int i = 0; i < arrCount; i++) {
            arrById[i] = arr[i];
        }
        ordenarPorId(arrById, 0, arrCount - 1);

        Scanner sc = new Scanner(System.in);
        Game pesquisaTemp[] = new Game[100];
        int pesquisaAux = 0;
        while (sc.hasNextLine()) {
            String buscaId = sc.nextLine().trim();
            if (buscaId.equals("FIM")) break;
            if (buscaId.isEmpty()) continue;
            try {
                int idBusca = Integer.parseInt(buscaId);
                int idx = buscarPorId(arrById, idBusca, arrCount);
                if (idx >= 0) pesquisaTemp[pesquisaAux++] = arrById[idx];
            } catch (NumberFormatException e) { /* ignora */ }
        }
        sc.close();

        Game[] pesquisa = new Game[pesquisaAux];
        for (int i = 0; i < pesquisaAux; i++) {
            pesquisa[i] = pesquisaTemp[i];
        }
        long t0 = System.nanoTime();
        if (pesquisa.length > 1) ordenarMerge(pesquisa);
        long t1 = System.nanoTime();
        long tempoNano = t1 - t0;

        System.out.println("| 5 preços mais caros |");
        int[] usedIds = new int[10];
        int usedIdsCount = 0;
        int printed = 0;

        for (int i = pesquisa.length - 1; i >= 0 && printed < 5; i--) {
            Game g = pesquisa[i];
            if (g == null) continue;
            boolean found = false;
            for (int j = 0; j < usedIdsCount; j++) {
                if (usedIds[j] == g.id) {
                    found = true;
                    break;
                }
            }
            if (found) continue;
            System.out.println(g.toString());
            usedIds[usedIdsCount++] = g.id;
            printed++;
        }
        System.out.println();

        System.out.println("| 5 preços mais baratos |");
        usedIdsCount = 0;
        printed = 0;
        for (int i = 0; i < pesquisa.length && printed < 5; i++) {
            Game g = pesquisa[i];
            if (g == null) continue;
            boolean found = false;
            for (int j = 0; j < usedIdsCount; j++) {
                if (usedIds[j] == g.id) {
                    found = true;
                    break;
                }
            }
            if (found) continue;
            System.out.println(g.toString());
            usedIds[usedIdsCount++] = g.id;
            printed++;
        }

        String logName = MATRICULA + "_mergesort.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(logName))) {
            pw.printf("%s\t%d\t%d\t%d\n", MATRICULA, comparisons, movements, tempoNano);
        }
    }

    private static int buscarPorId(Game[] a, int id, int n) {
        int l = 0, r = n - 1;
        while (l <= r) {
            int m = (l + r) >>> 1;
            if (a[m].id == id) return m;
            if (a[m].id < id) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }

    private static void ordenarPorId(Game[] a, int esq, int dir) {
        if (esq < dir) {
            int indicePivo = particionar(a, esq, dir);
            ordenarPorId(a, esq, indicePivo - 1);
            ordenarPorId(a, indicePivo + 1, dir);
        }
    }
    
    private static int particionar(Game[] a, int esq, int dir) {
        Game pivo = a[dir];
        int i = (esq - 1);
        for (int j = esq; j < dir; j++) {
            if (a[j].id <= pivo.id) {
                i++;
                Game temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        Game temp = a[i + 1];
        a[i + 1] = a[dir];
        a[dir] = temp;
        return (i + 1);
    }

    private static void ordenarMerge(Game[] a) {
        if (a == null || a.length < 2) return;
        Game[] aux = new Game[a.length];
        ordenarMergeRec(a, aux, 0, a.length - 1);
    }

    private static void ordenarMergeRec(Game[] a, Game[] aux, int esq, int dir) {
        if (esq >= dir) return;
        int meio = (esq + dir) / 2;
        ordenarMergeRec(a, aux, esq, meio);
        ordenarMergeRec(a, aux, meio + 1, dir);
        mesclar(a, aux, esq, meio, dir);
    }

    private static void mesclar(Game[] a, Game[] aux, int esq, int meio, int dir) {
        for (int k = esq; k <= dir; k++) {
            aux[k] = a[k];
        }
        
        int i = esq;
        int j = meio + 1;
        int k = esq;
        
        while (i <= meio && j <= dir) {
            comparisons++;
            if (comparar(aux[i], aux[j]) <= 0) {
                a[k++] = aux[i++];
                movements++;
            } else {
                a[k++] = aux[j++];
                movements++;
            }
        }
        
        while (i <= meio) {
            a[k++] = aux[i++];
            movements++;
        }
        
        while (j <= dir) {
            a[k++] = aux[j++];
            movements++;
        }
    }

    private static int comparar(Game x, Game y) {
        if (Float.compare(x.price, y.price) < 0) return -1;
        if (Float.compare(x.price, y.price) > 0) return 1;
        if (x.id < y.id) return -1;
        if (x.id > y.id) return 1;
        return 0;
    }

    private static String formatarLista(String[] array, int count) {
        String result = "[";
        if (array != null) {
            for (int i = 0; i < count; i++) {
                result += array[i];
                if (i < count - 1) {
                    result += ", ";
                }
            }
        }
        result += "]";
        return result;
    }

    public static String gameToString(Game g) {
        return "=> " + g.id + " ## " +
            g.name + " ## " +
            g.releaseDate + " ## " +
            g.estimatedOwners + " ## " +
            g.price + " ## " +
            formatarLista(g.supportedLanguages, g.supportedLanguagesCount) + " ## " +
            g.metacriticScore + " ## " +
            g.userScore + " ## " +
            g.achievements + " ## " +
            formatarLista(g.publishers, g.publishersCount) + " ## " +
            formatarLista(g.developers, g.developersCount) + " ## " +
            formatarLista(g.categories, g.categoriesCount) + " ## " +
            formatarLista(g.genres, g.genresCount) + " ## " +
            formatarLista(g.tags, g.tagsCount) + " ##";
    }

    public static class CsvParser {
        
        public static String[] parseLine(String line) {
            String[] temp = new String[20];
            int count = 0;
            if (line == null) return new String[0];
            StringBuilder cur = new StringBuilder();
            boolean inQuotes = false;
            
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '"') {
                    if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = !inQuotes;
                    }
                }
                else if (c == ',' && !inQuotes) {
                    if (count < temp.length) {
                        temp[count++] = cur.toString();
                    }
                    cur.setLength(0);
                }
                else {
                    cur.append(c);
                }
            }
            
            if (count < temp.length) {
                temp[count++] = cur.toString();
            }
            
            String[] result = new String[count];
            for (int i = 0; i < count; i++) {
                result[i] = temp[i];
            }
            return result;
        }
    }

    public static class Game {
        public int id;
        public String name;
        public String releaseDate;
        public int estimatedOwners;
        public float price;
        public String[] supportedLanguages;
        public int supportedLanguagesCount;
        public int metacriticScore;
        public float userScore;
        public int achievements;
        public String[] publishers;
        public int publishersCount;
        public String[] developers;
        public int developersCount;
        public String[] categories;
        public int categoriesCount;
        public String[] genres;
        public int genresCount;
        public String[] tags;
        public int tagsCount;

        public static Game fromCsvFields(String[] f) {
            Game g = new Game();
            g.id = parseIntDefault(get(f,0), 0);
            g.name = unquote(get(f,1));
            g.releaseDate = normalizeDate(get(f,2));
            g.estimatedOwners = parseIntDefault(cleanNumber(get(f,3)), 0);
            g.price = parsePrice(get(f,4));
            String[] langResult = parseList(get(f,5), true);
            g.supportedLanguages = langResult;
            g.supportedLanguagesCount = langResult.length;
            g.metacriticScore = parseIntOrDefault(get(f,6), -1);
            g.userScore = parseUserScore(get(f,7));
            g.achievements = parseIntOrDefault(get(f,8), 0);
            String[] pubResult = parseList(get(f,9), false);
            g.publishers = pubResult;
            g.publishersCount = pubResult.length;
            String[] devResult = parseList(get(f,10), false);
            g.developers = devResult;
            g.developersCount = devResult.length;
            String[] catResult = parseList(get(f,11), false);
            g.categories = catResult;
            g.categoriesCount = catResult.length;
            String[] genResult = parseList(get(f,12), false);
            g.genres = genResult;
            g.genresCount = genResult.length;
            String[] tagResult = parseList(get(f,13), false);
            g.tags = tagResult;
            g.tagsCount = tagResult.length;
            return g;
        }

        private static String get(String[] f, int idx) {
            if (f == null || idx >= f.length) return "";
            return f[idx] == null ? "" : f[idx].trim();
        }

        private static String unquote(String s) {
            if (s == null) return "";
            s = s.trim();
            if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1, s.length()-1);
            }
            return s;
        }

        private static String cleanNumber(String s) {
            if (s == null) return "";
            s = unquote(s).trim();
            return s.replaceAll("[^0-9]", "");
        }

        private static int parseIntDefault(String s, int def) {
            if (s == null) return def;
            s = s.trim();
            if (s.isEmpty()) return def;
            try { return Integer.parseInt(s); } catch (Exception e) { return def; }
        }

        private static int parseIntOrDefault(String s, int def) {
            s = unquote(s).trim();
            if (s.isEmpty()) return def;
            s = s.replaceAll("[^0-9-]", "");
            if (s.isEmpty()) return def;
            try { return Integer.parseInt(s); } catch (Exception e) { return def; }
        }

        private static float parseUserScore(String s) {
            s = unquote(s).trim().toLowerCase();
            if (s.isEmpty() || s.equals("tbd")) return -1.0f;
            if (s.equals("0") || s.equals("0.0")) return 0.0f;
            s = s.replaceAll("[^0-9,.-]", "");
            s = s.replace(',', '.');
            if (s.isEmpty()) return -1.0f;
            try { return Float.parseFloat(s); } catch (Exception e) { return -1.0f; }
        }

        private static float parsePrice(String s) {
            s = unquote(s).trim();
            if (s.isEmpty()) return 0.0f;
            if (s.equalsIgnoreCase("Free to Play")) return 0.0f;
            String cleaned = s.replaceAll("[^0-9,.-]", "");
            cleaned = cleaned.replace(',', '.');
            if (cleaned.isEmpty()) return 0.0f;
            try { return Float.parseFloat(cleaned); } catch (Exception e) { return 0.0f; }
        }

        private static String[] parseList(String s, boolean preserveSingleQuotes) {
            s = unquote(s).trim();
            String[] temp = new String[100];
            int count = 0;
            
            if (s.isEmpty()) return new String[0];
            if (s.startsWith("[") && s.endsWith("]") && s.length() >= 2) {
                s = s.substring(1, s.length()-1);
            }
            StringBuilder cur = new StringBuilder();
            boolean inSingle = false;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '\'') {
                    inSingle = !inSingle;
                    continue;
                }
                if (c == ',' && !inSingle) {
                    String item = cur.toString().trim();
                    item = stripQuotes(item);
                    if (!item.isEmpty() && count < temp.length) {
                        temp[count++] = item;
                    }
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
            String last = cur.toString().trim();
            last = stripQuotes(last);
            if (!last.isEmpty() && count < temp.length) {
                temp[count++] = last;
            }
            
            String[] result = new String[count];
            for (int i = 0; i < count; i++) {
                result[i] = temp[i];
            }
            return result;
        }

        private static String stripQuotes(String s) {
            if (s == null) return "";
            s = s.trim();
            if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length()-1);
            if (s.length() >= 2 && s.startsWith("'") && s.endsWith("'")) s = s.substring(1, s.length()-1);
            return s.trim();
        }

        private static String normalizeDate(String raw) {
            raw = unquote(raw).trim();
            if (raw.isEmpty()) return "";
            String[] patterns = {
                "MMM d, uuuu", "MMMM d, uuuu", "MMM dd, uuuu", "MMMM dd, uuuu",
                "d MMM uuuu", "d MMM, uuuu", "uuuu-MM-dd", "MM/dd/uuuu", "dd/MM/uuuu", "uuuu"
            };
            for (String p : patterns) {
                try {
                    if (p.equals("uuuu")) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d{4})").matcher(raw);
                        if (m.find()) {
                            int y = Integer.parseInt(m.group(1));
                            return String.format("01/01/%04d", y);
                        } else continue;
                    } else {
                        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern(p, Locale.ENGLISH);
                        java.time.LocalDate ld = java.time.LocalDate.parse(raw, fmt);
                        return ld.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu"));
                    }
                } catch (Exception ex) {}
            }

            String[] monthNames = {"jan", "january", "feb", "february", "mar", "march", 
                                   "apr", "april", "may", "jun", "june", "jul", "july",
                                   "aug", "august", "sep", "sept", "september", 
                                   "oct", "october", "nov", "november", "dec", "december"};
            int[] monthValues = {1, 1, 2, 2, 3, 3, 4, 4, 5, 6, 6, 7, 7, 
                                8, 8, 9, 9, 9, 10, 10, 11, 11, 12, 12};

            String low = raw.toLowerCase();
            int year = -1, month = 1, day = 1;

            Matcher my = Pattern.compile("(\\d{4})").matcher(raw);
            if (my.find()) year = Integer.parseInt(my.group(1));

            for (int i = 0; i < monthNames.length; i++) {
                if (low.contains(monthNames[i])) {
                    month = monthValues[i];
                    break;
                }
            }

            Matcher md = Pattern.compile("\\b(\\d{1,2})\\b").matcher(raw);
            while (md.find()) {
                int v = Integer.parseInt(md.group(1));
                if (v == year) continue;
                if (v >= 1 && v <= 31) { day = v; break; }
            }

            if (year == -1) return raw;
            return String.format("%02d/%02d/%04d", day, month, year);
        }

        @Override
        public String toString() { return mergesort.gameToString(this); }
    }
}