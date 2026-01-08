package net.borneolink.rest.out;

public class Passphrase {

    private static String[] names = {
            "Terminator", "Slicer","Ninja",
            "cow", "Robot", "littleGirl",
            "market", "industry", "miner",
            "sonOfEarth", "ironman", "thor"
    };

    public String WordPass(){

        String nameTO = "";
        int w;
        for(w=0; w <= 5; w++){
            String name = names[(int) (Math.random() * names.length)];
            nameTO += name + ',';
        }
        return nameTO;
    }


}
