package execute;
import java.util.StringTokenizer;

public class cell {
    public int x, y;
    public String id;
    cell(int xx, int yy){
        x = xx;
        y = yy;
        id = xx +","+yy;
    }
    cell(String idd){
        StringTokenizer st = new StringTokenizer(idd, ",");
        x = Integer.parseInt(st.nextToken());
        y = Integer.parseInt(st.nextToken());
        id = x +","+y;
    }
}
