package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
  private   final   Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        comparator=c;
    }

    public T max() {
      return max(comparator);
        }

    public T max(Comparator<T> c) {

        if (this==null) {
            return null;
        }
        if (size()==0) {
            return null;
        }
        int max = 0;
        for (int i =1; i < size(); i++) {
            if (c.compare(get(max),get(i))<0){
                max=i;
            }
        }
        return get(max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (((MaxArrayDeque<?>) o).max() != max()) {
            return false;
        }
        return super.equals(o);

    }


}