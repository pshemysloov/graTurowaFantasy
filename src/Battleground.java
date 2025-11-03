import java.util.ArrayList;
import java.util.List;

public class Battleground {
    private Player player;
    private List<Enemy[]> enemyWaves = new ArrayList<Enemy[]>();
    private int currentWave;

    public void addEnemyWave(){
        // logika dodawania fal przeciwników do listy
    }

    public Actor getNextActor(){
        // logika wybierania kolejnego aktora do wykonania akcji
        // bazująca na wartości actionValue każdego z przeciwników i gracza
        return null;
    }
}
