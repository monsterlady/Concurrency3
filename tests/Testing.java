import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class Testing {
    private int nrOfElements;
    private int nrOfPrinters;
    private int nrOfLittleFish;
    private int nrOfBigBoys;
    private PrinterBuffer printers;

    //A method to set up and run a our Threads
    private void setup(int nrOfElements, int nrOfPrinters, int nrOfLittleFish, int nrOfBigBoys) {
        this.nrOfElements = nrOfElements;
        this.nrOfPrinters = nrOfPrinters;
        this.nrOfLittleFish = nrOfLittleFish;
        this.nrOfBigBoys = nrOfBigBoys;
        int toPrint = (nrOfLittleFish + (nrOfBigBoys * nrOfElements));


        printers = new PrinterBuffer(nrOfElements, toPrint);

        for( int i=0; i<nrOfPrinters; i++){
            new Printer("Printer"+i, printers).start();
        }
        for( int i=0; i<nrOfLittleFish; i++){
            new LittleFish("LittleFish"+i, printers).start();
        }
        for( int i=0; i<nrOfBigBoys; i++) {
            new BigBoy("BigBoy" + i, printers, nrOfElements).start();
        }
    }

    @Test
    /**
     * Test our system with only bigBoys
     */
    public void onlyBigBoysTest(){
        System.out.println("Running: onlyBigBoysTest" + "\n" +
                "----------------------------------------------------------------------------");
        boolean running = true;
        //setup a system with only bigboys
        setup(2,2, 0, 10);

        while(running){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //New little fish should always be allowed (since we never hit 3 bigboys in a row with LittleFish present)
            assertTrue(printers.newLittleFishAllowed);

            if(!printers.printersRunning){
                running = false;
            }
        }

        //At the end of our test no BB should be present in our system
        assertEquals(0, printers.bigBoyPresent);
        //bigInRowCounter should be 10 since we had 10 BB in a row
        assertEquals(10, printers.bigInRowCounter);
    }

    @Test
    /**
     * Test our system with only LittleFish
     */
    public void onlyLittleFishTest(){
        System.out.println("Running: onlyLittleFishTest" + "\n" +
                "----------------------------------------------------------------------------");
        boolean running = true;
        //setup a system with only bigboys
        setup(2,2, 20, 0);

        while(running){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertTrue(printers.newLittleFishAllowed);

            if(!printers.printersRunning){
                running = false;
            }
        }

        //At the end of our test no LF should be present in our system
        assertEquals(0, printers.littleFishEntered);
    }

    @Test
    /**
     * Test our system with a combination of LittleFish and less than 3 BigBoys.
     */
    public void combLessThan3bbTest(){
        System.out.println("Running: combLessThan3bbTest" + "\n" +
                "----------------------------------------------------------------------------");
        boolean running = true;
        //setup a system with only bigboys
        setup(2,2, 15, 2);

        while(running){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //New Littlefish should never be false --> never 3 BB in a row
            assertTrue(printers.newLittleFishAllowed);

            if(!printers.printersRunning){
                running = false;
            }
        }

        //At the end of our test no LF should be present in our system
        assertEquals(0, printers.littleFishEntered);
        //At the end of our test no BB should be present in our system
        assertEquals(0, printers.bigBoyPresent);
        //bigInRowCounter should be 2 since we had 2 BB in a row
        assertEquals(2, printers.bigInRowCounter);
    }

    @Test
    /**
     * Test our system with a combination of LittleFish and more than 3 BigBoys.
     */
    public void combMoreThan3bbTest(){
        System.out.println("Running: combMoreThan3bbTest" + "\n" +
                "----------------------------------------------------------------------------");
        boolean running = true;
        //setup a system with only bigboys
        setup(2,2, 15, 5);

        while(running){
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //There should never be more than 3 bb in a row
            assertFalse(printers.bigInRowCounter > 3);
            //When there are 3 BB in a row no new LittleFish should be allowed
            if(printers.bigInRowCounter == 3){
                assertFalse(printers.newLittleFishAllowed);
            }

            //When there is 1 BB in a row new LittleFish should be allowed (occurs after reset and at the start)
            if(printers.bigInRowCounter == 1){
                assertTrue(printers.newLittleFishAllowed);
            }

            if(!printers.printersRunning){
                running = false;
            }
        }

        //At the end of our test no LF should be present in our system
        assertEquals(0, printers.littleFishEntered);
        //At the end of our test no BB should be present in our system
        assertEquals(0, printers.bigBoyPresent);
        //bigInRowCounter should be 2 since we had 3 BB in a row and then 2 more
        assertEquals(2, printers.bigInRowCounter);
    }
}
