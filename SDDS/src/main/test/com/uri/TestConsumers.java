package com.uri;

import junit.framework.TestCase;

import com.sdds.Consumer;
import com.sdds.Manager;
import com.sdds.util.Logger;

public class TestConsumers extends TestCase {
    private boolean consumed = false;

    public void testProduceConsume() throws InterruptedException {
        consumed = false;
        Manager mgr = Manager.getInstance();
        mgr.getConsumerManager().addConsumer(new Consumer<TestObject>(){
            @Override
            public void consume(TestObject object) {
                Logger.info(TestConsumers.this, "consumed: " + object.getName());
                consumed = true;
            }});
        TestObject testObject = new TestObject();
        testObject.setName("testObject");
        mgr.produce(testObject);
        Thread.sleep(500);        
        assertTrue(consumed);
        consumed = false;
    }

}
