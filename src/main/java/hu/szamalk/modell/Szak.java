package hu.szamalk.modell;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Szak implements Comparator<Szak>{
    private String nev,tanar1,tanar2,csak_vizsga;
    private int kredit,felev;
    private transient UUID azonosito;
    ArrayList<String> targyak;
    private static final String FAJLBAMENT = "targyak.dat";
    
    public Szak(String sor){
        String[] adatok = sor.split(";");
        String nev = adatok[0];
        int kredit = Integer.parseInt(adatok[1]);
        String tanar1 = adatok[3];
        String tanar2 = adatok[4];
        int felev = Integer.parseInt(adatok[5]);
        String csak_vizsga = adatok[6];
        this.nev = nev;
        this.tanar1 = tanar1;
        this.tanar2 = tanar2;
        this.csak_vizsga = csak_vizsga;
        this.kredit = kredit;
        this.felev = felev;
        ujIdGeneralas();
        this.targyak = new ArrayList<>();
    }
    private void beolvasas() throws IOException {
        String fn = "tantargyak.txt";
        List<String> sorok = Files.readAllLines(Path.of(fn));
        sorok.forEach(s -> {
            Szak szak = new Szak(s);
            targyak.add(String.valueOf(szak));
        });
    }

    private void ujIdGeneralas() {
        azonosito = UUID.randomUUID();
    }
    public static KreditComparator rendezKredit(){
        return new KreditComparator();
    }
    public static NevComparator rendezNev(){
        return new NevComparator();
    }

    private void szakKiirasa(){
        targyak = new ArrayList<>();
        targyak.add("Matematika");
        try(ObjectOutputStream objKi = new ObjectOutputStream(new FileOutputStream(FAJLBAMENT))){
            objKi.writeObject(targyak);
            System.out.println("1 könyv kiírva: " + targyak);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void szakBeolvasasa(){
        try(ObjectInputStream objBe = new ObjectInputStream(new FileInputStream(FAJLBAMENT))){
            Szak sz = (Szak) objBe.readObject();
            sz.ujIdGeneralas();
            System.out.println("A beolvasott szak állapota:");
            System.out.println(sz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


    public void statisztika(){
        String fn = "statisztika.txt";
        String tartalom = tartalom();
        try {
            Files.write(Path.of(fn), tartalom.getBytes());
        } catch (IOException e) {
            System.err.println("Hiba a fájl ÍRÁSA közben!" + e.getMessage());
        }
        osszesKredit();
        minMaxKredit();
        targyakatTanitok();
    }

    private String tartalom() {
        return "különböző tárgy:"+kulonbozoTargyak()+"\n vizsga tárgyak:"+vizsgaTargyak();
    }

    private void mapKiiras(Map<Integer, Integer> map) {
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int k = entry.getKey();
            int v = entry.getValue();
            System.out.printf("[%d] = %d db\n",k,v);
        }
    }

    private Map<Integer, Integer> vizsgaTargyak() {
        Map<Integer, Integer> mm = new HashMap<>();
        targyak.forEach(targyak -> {
            int kulcs = Integer.parseInt(targyak);
            if(mm.containsKey(kulcs)){
                int db = mm.get(kulcs);
                mm.put(kulcs, ++db);
            }else{
                mm.put(kulcs, 1);
            }
        });
        return mm;
    }
   public List<Tantargy> getTargyakNevSzerint() {
       Arrays.sort(targyak, rendezNev());
       return List.of(targyak);
    }

    public List<Tantargy> getTargyakKreditSzerint(){
        Arrays.sort(targyak, rendezKredit());
        return List.of(targyak);
    }

    private Map<String, String> targyakatTanitok() {
        Map<String, Integer> mm = new HashMap<>();
        targyak.forEach(targyak -> {
            List<String> tanitok = getTanar1();
            tanitok.forEach(sor ->{
                String[] tomb = sor.split(";");
                for (String kulcs : tomb) { //a kulcs a szerzo
                    if(mm.containsKey(kulcs)){
                        int db = mm.get(kulcs);
                        mm.put(kulcs, ++db);
                    }else{
                        mm.put(kulcs, 1);
                    }
                }
            });
        });
        return mm;

    }

    private void mapKiiras2(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            System.out.printf("[%s] = %d db\n",k,v);
        }
    }

    private void minMaxKredit() {
        System.out.println(Collections.min(getKredit()));
        System.out.println(Collections.max(getKredit()));
    }

    private void osszesKredit() {
        int osszes = 0;
        for (Szak szak : targyak) {
            osszes += szak.getKredit();
        }
        return osszes;
    }

    private Set<Integer> kulonbozoTargyak() {
        Set<Integer> kulonbozo = new HashSet<>();
        targyak.forEach(targyak -> kulonbozo.add(getTargyak()));
        return kulonbozo;
    }


    public String getNev() {
        return nev;
    }

    public String getTanar1() {
        return tanar1;
    }

    public String getTanar2() {
        return tanar2;
    }

    public String getCsak_vizsga() {
        return csak_vizsga;
    }

    public int getKredit() {
        return kredit;
    }

    public int getFelev() {
        return felev;
    }

    public UUID getAzonosito() {
        return azonosito;
    }

    public List<Tantargy> getTargyak() {
        return new ArrayList<>();
    }

    public void setKredit(int kredit) {
        if(kredit <= 5 || kredit >= 0){
            throw new NemMegfeleloKreditException("Minimum 0, maximum 5!");
        }
        this.kredit = kredit;
    }

    @Override
    public String toString() {
        return "Szak{" +
                "nev='" + nev + '\'' +
                ", azonosito=" + azonosito +
                '}';
    }

    @Override
    public int compare(Szak o1, Szak o2) {
        return 0;
    }
}
