import com.deco2800.potatoes.entities.trees.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.deco2800.potatoes.entities.TimeEvent;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpgradeStatsTest {
    List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
    List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
    UpgradeStats test = new UpgradeStats(10,1000,8f,5000,normalEvents, constructionEvents, "real_tree");
    @Test
    public void getEventsTest() {
        assertNotNull(test.getNormalEventsCopy());
        assertNotNull(test.getConstructionEventsCopy());
        assertNotNull( test.getNormalEventsReference());
        assertNotNull(test.getConstructionEventsReference());
    }
    @Test
    public void getHpTest() {
        assertEquals("incorrectreturn value hp" ,test.getHp(), 10);
    }
    @Test
    public void getRangeTest() {
        assertEquals("incorrectreturn value range" ,test.getRange(), 8,0.01);
    }
    @Test
    public void getSpeedTest() {
        assertEquals("incorrectreturn value speed" ,test.getSpeed(), 1000);
    }
    @Test
    public void getTextureTest() {
        assertEquals("incorrectreturn value texture" ,test.getTexture(), "real_tree");
    }
    @Test
    public void getTimeTest() {
        assertEquals("incorrectreturn value speed" ,test.getConstructionTime(), 5000);
    }
}