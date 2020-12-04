import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterBuffer {
    //Buffer capacity, pointers and variable setup
    private final Document[] printers;
    private final int capacity;
    public int writePointer;
    public int readPointer;
    public int printersUsed;
    public int bigBoyPresent;
    public int bigInRowCounter;
    public boolean newLittleFishAllowed;
    public int littleFishEntered;
    private int totalDocuments;
    private int printed;
    public boolean printersRunning;

    //Lock initiation and condition setup
    public final Lock lock = new ReentrantLock();
    //Conditions for general reading and writing
    public final Condition printerAvailable = lock.newCondition();
    public final Condition documentAvailable = lock.newCondition();
    //Awaits when LF wait for BB
    public final Condition bigBoyInQueue = lock.newCondition();
    //Awaits when 3 BB in a row
    public final Condition bigBoyInRow = lock.newCondition();
    //Awaits when littleFish can't enter the print shop
    public final Condition littleFishQueue = lock.newCondition();

    /**
     * The class PrinterBuffer is the cyclic buffer between our Producers (LF and BB) and our Consumers (printers)
     * @param capacity The amount of elements of the buffer (amount of printers)
     * @param totalDocuments The amount of documents that should be printed
     */
    PrinterBuffer(int capacity, int totalDocuments) {
        this.capacity = capacity;
        this.printers = new Document[capacity];
        this.totalDocuments = totalDocuments;
        printersUsed = 0;
        bigBoyPresent = 0;
        bigInRowCounter = 0;
        newLittleFishAllowed = true;
        littleFishEntered = 0;
        printersRunning = true;
    }

    /**
     *Writes the given Document to the buffer
     *lfWrite is only called by SmallFry, thus needing only one spot in the buffer
     * @param document : The document that needs to be written to the buffer
     */
    public void lfWrite(Document document){
        lock.lock();

        //If no new fish are allowed and there are BB present this lf waits outside the printshop
        if(!newLittleFishAllowed && bigBoyPresent > 0){
            try {
                littleFishQueue.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Increases the amoutn fo LF that have entered the printshop
        littleFishEntered++;

        //If there is a printer available occupy it if the bigBoys allow it
        while (printersUsed == capacity) {
            try {
                //If all printer are in use --> await() printerAvailable condition (Lock remains in place)
                printerAvailable.await();

                //If a BB is in queue and <3 in a row --> await() bigBoyInQueue condition (Lock remains in place)
                if (bigBoyPresent > 0 && bigInRowCounter < 3) {
                    bigBoyInQueue.await();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        //Write our document to the buffer
        writeToBuffer(document);

        littleFishEntered--;

        //Ensures that new littlefish can enter by signalling the bigboy.
        //If their is no bb to do so, signals the other LF
        if(littleFishEntered == 0 && !newLittleFishAllowed){
            if(bigBoyPresent > 0){
                bigInRowCounter = 0;
                bigBoyInRow.signalAll();
            } else {
                bigInRowCounter = 0;
                newLittleFishAllowed = true;
                littleFishQueue.signalAll();
            }
        }
        //unlock the lock
        lock.unlock();
    }

    /**
     * Writes the given Documents to the buffer
     * bbWrite is called by BigBoy, thus needing and filling the entire buffer
     * @param documents : The documents that need to be written to the buffer
     */
    public void bbWrite(Document[] documents){
        lock.lock();
        bigBoyPresent++;

        //If all printer are in use --> await() our condition (Lock remains in place)
        while (printersUsed != 0){
            try {
                //Wait for printerAvailable signal
                printerAvailable.await();

                //When we have 3 BB in a row
                if(bigInRowCounter > 2 && littleFishEntered > 0){
                    //Await the SmallFry Signal
                    bigBoyInRow.await();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Write our documents to the buffer and unlock the lock
        for(Document document: documents){
            writeToBuffer(document);
        }

        bigBoyPresent--;
        bigInRowCounter++;

        //After all entered LittleFish are done writing, new LittleFish can enter
        if(littleFishEntered == 0 && !newLittleFishAllowed){
            newLittleFishAllowed = true;
            littleFishQueue.signalAll();
        }

        //If no Bigboys are present, waiting LittleFish can wake up
        if(bigBoyPresent == 0) {
            bigBoyInQueue.signalAll();
        }

        //If 3 BigBoys wrote in a row, and there are LittleFish waiting. Signall LitteFish
        if(bigInRowCounter > 2 && littleFishEntered > 0) {
            newLittleFishAllowed = false;
            bigBoyInQueue.signalAll();
        }

        lock.unlock();
    }

    /**
     * Reads the readpointer positions of the PrinterBuffer and gives back a Document
     * @returns Document
     */
    public Document read(){
        lock.lock();
        while(printersUsed == 0){
            if(printersRunning) {
                try {
                    documentAvailable.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }

        //Get the correct readPointer and read the document
        Document document = printers[readPointer];
        printersUsed--;
        readPointer++;

        if(readPointer == capacity){
            readPointer = 0;
        }

        //Print the message
        System.out.println(Thread.currentThread().getName() + ": printing --> " + document.getData()
                + " | LF = " + littleFishEntered + " | BB = " + bigBoyPresent + " | InRow = " + bigInRowCounter);

        //Signal that a printer is available and unlock the lock
        printerAvailable.signalAll();
        lock.unlock();
        return document;
    }

    private void writeToBuffer(Document document){
        //Write our document to the writePointer position in our buffer and increase the amount of printers used
        printers[writePointer] = document;
        printersUsed++;
        writePointer ++;

        System.out.println(Thread.currentThread().getName() + ": Writing --> " + document.getData()
                + " | LF = " + littleFishEntered + " | BB = " + bigBoyPresent + " | InRow = " + bigInRowCounter);

        //Reset our write pointer (cyclic)
        if(writePointer == capacity){
            writePointer = 0;
        }

        //Signal the printers
        documentAvailable.signalAll();
    }

    /**
     * Increased the amount of printed documents by 1
     * Turns off the printers when all documents have been printed (all customers handled)
     */
    public void increasePrinted(){
        printed++;
        if(printed >= totalDocuments){
            printersRunning = false;
        }
    }
}
