public class Main {
    public static void main(String[] args){
        new Main().startEnv();
    }

    private void startEnv(){
        int NROFELEMENTS = 30;
        int NROFPRINTERS = 30;
        int NROFLITTLEFISH = 100;
        int NROFBIGBOYS = 20;

        //Calculates the amount of documents that should be printed
        int toPrint = (NROFLITTLEFISH + (NROFBIGBOYS * NROFELEMENTS));
        PrinterBuffer printers = new PrinterBuffer(NROFELEMENTS, toPrint);

        for( int i=0; i<NROFPRINTERS; i++){
            new Printer("Printer"+i, printers).start();
        }
        for( int i=0; i<NROFLITTLEFISH; i++){
            new LittleFish("LittleFish"+i, printers).start();
        }
        for( int i=0; i<NROFBIGBOYS; i++){
            new BigBoy("BigBoy"+i, printers, NROFELEMENTS).start();
        }
    }


}
