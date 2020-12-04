import java.util.concurrent.TimeUnit;

public class BigBoy extends Thread{

    private final PrinterBuffer printers;
    private final int nrDocuments;

    /**
     * The BigBoy class produces documents and writes them to the PrinterBuffer
     * A BigBoy produces enough documents to fill the entire buffer
     * A BigBoy needs an empty buffer to write
     * @param name BigBoy name
     * @param printers The provided PrinterBuffer
     * @param nrDocuments The number of documents BigBoy creates (equal to the amount of printers)
     */
    public BigBoy(String name, PrinterBuffer printers, int nrDocuments){
        super(name);
        this.printers = printers;
        this.nrDocuments = nrDocuments;
    }

    public void run(){
        //Create new documents
        Document[] documents = new Document[nrDocuments];
        for (int i = 0; i < nrDocuments; i++) {
            documents[i] = new Document();
        }
        //Fake creation time
        try {
            TimeUnit.MILLISECONDS.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //write documents to buffer
        printers.bbWrite(documents);
    }
}
