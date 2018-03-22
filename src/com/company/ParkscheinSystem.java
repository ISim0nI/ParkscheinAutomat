package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ParkscheinSystem extends ParkscheinDatenbank {

    private Scanner scan;


    /**
     * überpruft eingabe der richtigen münzen und parameter.
     * fuhrt entsprechende methode bei den parametern aus Parkdauer korrigeiren , parkschein Anfordern oder weiter
     * das eingeworfene geld entgegennehmen.
     */
    public void muenzeAddiert() {

        while (true) {
            System.out.println("--------------------------------");
            System.out.print("Muenzen Einwerfen für Parkschein: ");
            scan = new Scanner(System.in);


            try {
                int a = scan.nextInt();
                if (a == -3  || a==-2||a==-1 || a == 10 || a == 20 || a == 50 || a == 100 || a == 200) {
                    if (a == -2||a==-1) {
                        parkdauerKorrektur(getParkdauer(),a);

                    }

                    if (a == -3) {

                        parkscheinAnforderung(inEuro(getGesammtMuenze()), parkdauerMin(getParkdauer()), dateTime(getParkdauer()), getRestgeld());
                        break;
                    }
                    if (a == 10 || a == 20 || a == 50 || a == 100 || a == 200) {
                        int add = getGesammtMuenze();
                        add += a;
                        setGesammtMuenze(add);
                        setRestgeld(0);

                        parkdauerAnhandGuthaben(getGesammtMuenze());
                        bildAusgabe(inEuro(getGesammtMuenze()), parkdauerMin(getParkdauer()), dateTime(getParkdauer()), inEuro(getRestgeld()));

                    }
                } else {
                    throw new ParkscheinException("Nur 10,20,50,100,200 cent Betraege!");
                }
            } catch (ParkscheinException e) {

            } catch (Exception e) {
                System.err.println("Nur 10,20,50,100,200 cent Betraege!");
            }


        }

    }


    /**
     * @param Gebuehr       wie viel guthaben drin ist;
     * @param parkdauer    wie lange parken mit dem guthaben;
     * @param parkzeitEnde wann die parkdauer zuende ist;
     * @param Restgeld     wieviel restgeld er bekommt;
     */
    public void bildAusgabe(String Gebuehr, String parkdauer, String parkzeitEnde, String Restgeld) {
        System.out.println();
        System.out.println("Parkdauer:" + parkdauer + "\t Parkzeitende:" + parkzeitEnde + "\nGebühr:" + Gebuehr + "\t     Restgeld:" + Restgeld + "\n");
        System.out.println("Mit -1 Pardauer +10 min, -2 Parkdaer -10 min, -3 Parkschein Ausdrucken\n");

    }


    /**
     * $
     *
     * @param guthaben nimmt guthaben entgegen und setzt Parkdauer.
     * @return gibt guthaben zurück.
     */

    public int parkdauerAnhandGuthaben(int guthaben) {

        setParkdauer(guthaben);
        return guthaben;
    }

    /**
     * Methode bekommt Parkdauer in Min un rechnet diese in Stunden und Minuten aus.
     *
     * @param Parkdauer parkdauer in min.
     * @return String der Stunden und Minuten enthält.
     */
    public String parkdauerMin(int Parkdauer) {

        if (Parkdauer >= 60) {
            double minuten = Parkdauer % 60;
            double stunden = Parkdauer / 60;
            String beides = String.valueOf((int) stunden) + " Stunden " + String.valueOf((int) minuten) + " Minuten";
            setParkdauer(Parkdauer);

            return beides;

        }
        String minuten = String.valueOf(Parkdauer) + " Minuten";
        setParkdauer(Parkdauer);
        return minuten;
    }


    /**
     * Bekommt Parkdauer und veränder sie um eine einheit(+10||-10 min) und setzt das restgeld.
     * @param Parkdauer aktuelle Parkdauer.
     * @param eingabewert eingabewert um parkdauer zu erhöhen || erniedrigen.
     */
    public void parkdauerKorrektur(int Parkdauer,int eingabewert) {

        int restgeld = getRestgeld();

        if (eingabewert==-1 && Parkdauer+10 <= getGesammtMuenze()){
            setParkdauer(Parkdauer+10);
            restgeld -=10;
            setRestgeld(restgeld);
            bildAusgabe(inEuro(getGesammtMuenze()), parkdauerMin(getParkdauer()), dateTime(getParkdauer()), inEuro(getRestgeld()));

        }else if (eingabewert==-1) System.out.println("Nicht genug Geld!");

        if (eingabewert==-2&& Parkdauer-10 >= 0){
            setParkdauer(Parkdauer-10);
            restgeld +=10;
            setRestgeld(restgeld);
            bildAusgabe(inEuro(getGesammtMuenze()), parkdauerMin(getParkdauer()), dateTime(getParkdauer()), inEuro(getRestgeld()));

        }else if(eingabewert==-2) System.out.println("Weniger als 0 minuten geht nicht!");


        System.out.println("Korrektur Abgeschlossen");
    }


    /**
     * Nimmt Parkdauer entgegen und addiert sie auf die aktuelle Zeit.
     *
     * @param minutenPlus Parkdauer.
     */
    public String dateTime(int minutenPlus) {
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");

        String zeit = dt.plusMinutes(minutenPlus).format(df);

        return zeit;
    }

    /**
     * Methode gib visuell den Parkschein aus.
     *
     * @param Gebühr       Wieveil Geld eingezahlt.
     * @param parkdauer    wie lange ich Parken darf.
     * @param parkzeitEnde wann meine Parkzeit beendet ist.
     * @param Restgeld     wieviel Restgeld ich bekommen wuerde.
     */
    public void parkscheinAnforderung(String Gebühr, String parkdauer, String parkzeitEnde, int Restgeld) {
        System.out.println("\t|--------------------------------");
        System.out.println("\t|         Parkschein");
        System.out.println("\t|Parkdauer: " + parkdauer + "           ");
        System.out.println("\t|Gebühr:" + Gebühr);
        System.out.println("\t|Parkzeitende: " + parkzeitEnde);
        System.out.println("\t|Restgeld: " + inEuro(Restgeld));
        System.out.println("\t|--------------------------------");
    }

    /**
     * Wandelt den betrag in Euro um.
     * @param geldbetrag Geld in cent
     * @return gibt einen String in in Euro aus.
     */
    public String inEuro(int geldbetrag){
        if (geldbetrag>=100){
            double euro = geldbetrag;
            double euro2= euro/100;
            String euroCent = euro2+"€";
            return euroCent;
        }

        String unter1Euro = "0,"+geldbetrag+"€";
        return unter1Euro;

    }


}
