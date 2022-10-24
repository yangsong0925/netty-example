import java.util.*;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/10/17 9:30
 */
public class Test {


    public static void main(String[] args) {
//        Map<Basket, Boolean> basketMap = new HashMap();
//        int[] fruits = {3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4};
//        for (int i = 0; i < fruits.length; i++) {
//            int fruit = fruits[i];
//            basketMap.forEach((basket, aBoolean) -> {
//                if (aBoolean) {
//                    basketMap.put(basket, basket.add(fruit));
//                }
//            });
//            Basket basket = new Basket();
//            basketMap.put(basket, basket.add(fruit));
//        }
//        System.out.println(basketMap.keySet().stream().mapToInt(Basket::getCount).max().getAsInt());

        char[] chars = {'A', 'B', 'C'};
        char[] fruits = {'E', 'B', 'B', 'A', 'N', 'C', 'F'};
        int left, right = 0;
        while (right < fruits.length) {
            char fruit = fruits[right];




        }
    }

    static class Basket {

        private Set<Integer> typeList = new HashSet<>();

        private Integer count = 0;

        public Boolean add(int type) {
            if (typeList.size() < 2) {
                typeList.add(type);
                count++;
                return Boolean.TRUE;
            }
            if (typeList.contains(type)) {
                count++;
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        public Integer getCount() {
            return count;
        }

    }

}
