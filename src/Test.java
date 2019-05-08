import GraphicalInterface.GraIntMain;
import LockerSystem.DeliveryMan;
import LockerSystem.Package;
import Management.PickupPoint;
import LockerSystem.BoxType.*;

import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        Package Pacco1 = new Package("Pacco1", 10, 20, 15);
        Package Pacco2 = new Package("Pacco2", 95, 80, 10);
        Package Pacco3 = new Package("Pacco3", 25, 20, 15);
        Package Pacco4 = new Package("Pacco4", 50, 30, 15);
        Package Pacco5 = new Package("Pacco5", 54, 54, 41.5);

        PickupPoint pp = new PickupPoint("PUNTODIRITIRO1", 7, 5, 3);

        try {
            pp.addPackage(Pacco1);
            pp.addPackage(Pacco2);
            pp.addPackage(Pacco3);
            pp.addPackage(Pacco4);
            pp.addPackage(Pacco5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GraIntMain gui = new GraIntMain(pp);

        pp.addObserver(gui);

        ArrayList<Box> listaBox = pp.getBoxList();

        for (Box boxInLista : listaBox) {
            System.out.println(boxInLista);
        }


        DeliveryMan fattorino = new DeliveryMan("Michele");

        Package PaccoMichele1 = new Package("Michele1", 20, 20, 20);
        Package PaccoMichele2 = new Package("Michele2", 40, 30, 35);

        fattorino.addPackage(PaccoMichele1);
        fattorino.addPackage(PaccoMichele2);

        pp.generateDeliveryManID();
        String idFattorino = pp.getDeliveryManID();
        System.out.println(idFattorino);

        pp.sortPackages(fattorino, "AAAA");
        pp.sortPackages(fattorino, idFattorino);

        listaBox = pp.getBoxList();

        for (Box boxInLista : listaBox) {
            System.out.println(boxInLista);
        }
    }
}