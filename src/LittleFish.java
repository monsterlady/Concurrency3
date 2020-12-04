import java.util.concurrent.TimeUnit;

public class LittleFish extends Thread{

    private final PrinterBuffer printers;

    /**
     * The LittleFish class produces a document and writes is to the PrinterBuffer
     * A LittleFish only needs 1 spot in the buffer
     * @param name LittleFish name
     * @param printers The provided PrinterBuffer
     */
    public LittleFish(String name, PrinterBuffer printers){
        super(name);
        this.printers = printers;
    }

    public void run() {
//        while (true) {
            //Create document
            Document document = new Document();
            //Fake creation time
            try {
                TimeUnit.MILLISECONDS.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //write document to buffer
            printers.lfWrite(document);
//        }
    }
}
