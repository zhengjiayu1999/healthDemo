import java.text.SimpleDateFormat;
import java.util.*;

public class Test {

    @org.junit.Test
    public void test(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.YEAR,-18);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(calendar.getTime());
        System.out.println(format1);
        calendar.add(Calendar.YEAR,18-30);

        String format2 = format.format(calendar.getTime());
        System.out.println(format2);



//        for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
//            String key = stringIntegerEntry.getKey();
//            if(key.equals("null")){
//                newmap.put("未设置",stringIntegerEntry.getValue());
//            }else if(key.equals("1")){
//                newmap.put("男",stringIntegerEntry.getValue());
//            }else {
//                newmap.put("女",stringIntegerEntry.getValue());
//            }
//        }
//        for (Map.Entry<String, Integer> stringIntegerEntry : newmap.entrySet()) {
//            System.out.println(stringIntegerEntry.getKey()+"/"+stringIntegerEntry.getValue());
//        }
     }
}
