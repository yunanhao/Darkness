package sample;

import org.junit.Test;

public class Everything {

    @Test
    public void method01() {
        Group<? extends Animal> group = new Group<>();
//        group.add(null);
        Animal a = group.get();
        System.out.println(a);
        Group<? super Animal> group2 = new Group<>();
        group2.add(new Cat());
        Animal b = (Animal) group2.get();
        System.out.println(group2.get().getClass().getName());
    }

    class Animal {

    }

    class Dog extends Animal {
    }

    class Cat extends Animal {
    }

    class Group<T> {
        T t;

        void add(T t) {
            this.t = t;
        }

        T get() {
            return this.t;
        }
    }

}
