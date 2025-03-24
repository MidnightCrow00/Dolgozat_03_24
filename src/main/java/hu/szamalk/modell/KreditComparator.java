package hu.szamalk.modell;

import java.text.Collator;
import java.util.Comparator;

public class KreditComparator implements Comparator<Szak> {
    @Override
    public int compare(Szak egyik, Szak masik) {
        return egyik.getKredit()-masik.getKredit();
    }
}
