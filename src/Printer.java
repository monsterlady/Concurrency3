public class Printer extends Thread{

    private final PrinterBuffer printers;
    private boolean running = true;

    public Printer(String name, PrinterBuffer printers){
        super(name);
        this.printers = printers;
    }

    public void run(){
        while (running) {
            //Document is not sout printed in printer but in PrinterBuffer (this provided cleaner console information)
            //Consume the data from the buffer and increase the amount of printed documents
            Document document = printers.read();
            printers.increasePrinted();
            try {
                sleep(1200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Checks if the printer should be turned off
            if(!printers.printersRunning){
                running = false;
            }
        }
        System.out.println(this.getName() + "Stopped Printing");
    }
}
