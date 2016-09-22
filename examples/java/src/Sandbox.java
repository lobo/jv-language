public class Sandbox {

    public static void f1(int p1, boolean p2, String p3) {
        int p4 = 9876;
        System.out.println("Hello f1");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println("Bye f1");
    }

    public static void main(String[] args) {
        f1(123456, false, "hola");
    }
}
