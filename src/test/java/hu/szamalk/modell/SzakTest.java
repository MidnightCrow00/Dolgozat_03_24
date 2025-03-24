package hu.szamalk.modell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class SzakTest {
    Szak szak;
    @BeforeEach
    void ini() {
        szak = new Szak("egy szak neve");
        Assertions.assertTrue(szak.getTargyak().size()>0);
    }
    @Test
    void testSzakTargyNevek(){
        for (Tantargy tantargy : szak) {
            Assertions.assertTrue(tantargy.getNev().length() > 3);
        }
    }

    @Test
    void testGetTargyak(){
        List<Tantargy> targyak = szak.getTargyak();
        int eredeti = targyak.size();
        targyak.add(new Tantargy());
        Assertions.assertTrue(eredeti == szak.getTargyak().size());
    }

    @Test
    void testAzonosnevTanar(){
        System.out.println("Egy szaknak nem lehet több azonos tanárja.");
        Szak szak = new Szak("");
        Assertions.assertTrue(szak.getTanar1().length() == 1, "Hiba: azonos szerzők.");

    }
    @Test
    void testKreditErteke0es5(){
        System.out.println(" 0 < Kredit < 5, akkor NemMegfeleloKreditException érkezik.");
        String hiba = "Nem érkezett vagy nem a megfelelő Exception érkezett.";
        Assertions.assertThrows(NemMegfeleloKreditException.class, () -> new Szak(""), hiba);
        Szak szak = new Szak("");
        Assertions.assertThrows(NemMegfeleloKreditException.class, () -> szak.setKredit(6), hiba);
    }
}