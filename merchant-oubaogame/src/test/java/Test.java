import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

public class Test {

    public static void main(String[] args) {
        List list = DoubleStream.of(1.0, 2.0, 3.0)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(list.size());
    }
}
