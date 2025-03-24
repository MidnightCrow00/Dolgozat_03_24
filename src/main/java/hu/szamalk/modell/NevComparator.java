package hu.szamalk.modell;

import java.text.Collator;
import java.util.Comparator;

public class NevComparator implements Comparator<Szak> {

    @Override
    public int compare(Szak egyik, Szak masik) {
        Collator c = Collator.getInstance();
        return c.compare(egyik.getNev(), masik.getNev());
    }
}
