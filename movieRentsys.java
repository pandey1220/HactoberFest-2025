import java.util.*;

class MovieRentingSystem {

    static class Copy {
        int shop, movie, price;
        Copy(int s, int m, int p) { shop = s; movie = m; price = p; }
    }

    static final Comparator<Copy> ORDER = (a, b) -> {
        if (a.price != b.price) return a.price - b.price;
        if (a.shop  != b.shop)  return a.shop  - b.shop;
        return a.movie - b.movie;
    };

    Map<Integer, TreeSet<Copy>> avail = new HashMap<>();
    TreeSet<Copy> rented = new TreeSet<>(ORDER);
    Map<Long, Copy> pair = new HashMap<>();

    static long key(int s, int m) {
        return ((long) s << 32) ^ (m & 0xffffffffL);
    }

    public MovieRentingSystem(int n, int[][] data) {
        for (int[] d : data) {
            Copy c = new Copy(d[0], d[1], d[2]);
            pair.put(key(d[0], d[1]), c);
            avail.computeIfAbsent(d[1], k -> new TreeSet<>(ORDER)).add(c);
        }
    }

    public List<Integer> search(int m) {
        List<Integer> out = new ArrayList<>();
        TreeSet<Copy> set = avail.get(m);
        if (set == null) return out;
        for (Copy c : set) {
            if (out.size() == 5) break;
            out.add(c.shop);
        }
        return out;
    }

    public void rent(int s, int m) {
        Copy c = pair.get(key(s, m));
        if (c == null) return;
        avail.get(m).remove(c);
        rented.add(c);
    }

    public void drop(int s, int m) {
        Copy c = pair.get(key(s, m));
        if (c == null) return;
        rented.remove(c);
        avail.computeIfAbsent(m, k -> new TreeSet<>(ORDER)).add(c);
    }

    public List<List<Integer>> report() {
        List<List<Integer>> out = new ArrayList<>();
        for (Copy c : rented) {
            if (out.size() == 5) break;
            out.add(Arrays.asList(c.shop, c.movie));
        }
        return out;
    }
}
