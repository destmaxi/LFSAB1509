import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.User;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void addScoreTest() {
        User user = new User(new GravityRun());
        //assertTrue(user.setUsername("JM"));
        //assertEquals("JM", user.getUsername());
        user.setIndexSelected(1);
        user.setFirstTimeTrue();
        user.setBeginnerScoreList(new ArrayList<>());
        user.setIntermediateScoreList(new ArrayList<>());
        user.setExpertScoreList(new ArrayList<>());
        assertEquals(0, user.getIntermediateScoreList().size());
        assertTrue(user.addScore(42));
        assertEquals(0, user.getBeginnerScoreList().size());
        assertEquals(0, user.getExpertScoreList().size());
        assertFalse(user.addScore(17));
        ArrayList<Integer> arrayList1 = new ArrayList<>();
        arrayList1.add(42);
        arrayList1.add(17);
        assertEquals(arrayList1, user.getIntermediateScoreList());
        assertTrue(user.addScore(69));
        assertFalse(user.addScore(50));
        assertFalse(user.addScore(60));
        ArrayList<Integer> arrayList2 = new ArrayList<>();
        arrayList2.add(69);
        arrayList2.add(60);
        arrayList2.add(50);
        //arrayList2.addAll(arrayList1);
        assertEquals(3, user.getIntermediateScoreList().size());
        assertEquals(arrayList2, user.getIntermediateScoreList());
        assertFalse(user.addScore(42));
        assertEquals(3, user.getIntermediateScoreList().size());
        assertTrue(user.addScore(81));
        assertEquals(81, (int) user.getIntermediateScoreList().get(0));
        ArrayList<Integer> arrayList3 = new ArrayList<>();
        arrayList3.add(81);
        arrayList3.add(69);
        arrayList3.add(60);
        //arrayList3.addAll(arrayList2);
        assertEquals(arrayList3, user.getIntermediateScoreList());
        assertFalse(user.addScore(81));
        assertEquals(3, user.getIntermediateScoreList().size());
        assertTrue(user.addScore(100));
        ArrayList<Integer> arrayList4 = new ArrayList<>();
        arrayList4.add(100);
        arrayList4.add(81);
        arrayList4.add(69);
        //arrayList4.addAll(arrayList3);
        assertEquals(arrayList4, user.getIntermediateScoreList());
        assertFalse(user.addScore(81));
        assertEquals(arrayList4, user.getIntermediateScoreList());
        assertEquals(0, user.getBeginnerScoreList().size());
        assertEquals(0, user.getExpertScoreList().size());
        assertNotSame(user.getBeginnerScoreList(), user.getExpertScoreList());
    }

}
