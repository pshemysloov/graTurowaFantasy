package Core;

@Author(name = "Mateusz Biskup")
public abstract class StatusEffect {
    public String name;
    public int duration; // Liczba tur

    public StatusEffect(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    // Wywoływane na początku tury posiadacza efektu
    public abstract void onTurnStart(Actor owner);

    // Wywoływane przy nakładaniu (np. zmiana statystyk)
    public void onApply(Actor owner) {}

    // Wywoływane przy wygaśnięciu (np. przywrócenie statystyk)
    public void onRemove(Actor owner) {}
}