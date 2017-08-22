import com.deco2800.potatoes.managers.EventManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.entities.*;
import com.deco2800.potatoes.entities.trees.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventManagerTest {
    @Test
    public void initTest() {
        EventManager eventManager = (EventManager) GameManager.get().getManager(EventManager.class);
    }
    @Test
    public void methodTest() {
        Tower tower = new Tower(8, 8, 0);
        TreeProjectileShootEvent testt= new TreeProjectileShootEvent(1000);
        EventManager eventManager = (EventManager) GameManager.get().getManager(EventManager.class);
        eventManager.registerEvent(tower, testt);
        eventManager.tickAll(2);
        eventManager.unregisterEvent(tower, testt);
        TreeProjectileShootEvent testt2= new TreeProjectileShootEvent(1000);
        eventManager.registerEvent(tower, testt);
        eventManager.registerEvent(tower, testt2);
        eventManager.tickAll(2);
        eventManager.unregisterAll(tower);
    }
}