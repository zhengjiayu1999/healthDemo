public class Test {

    @org.junit.Test
    public void test(){
        int a = 0;
        for (int i = 0; i < 5; i++) {
//            a=a++;
            a=++a;
        }
        System.out.println(a);
    }
}
