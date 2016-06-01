package org.fablabsantiago.smartcities.app.lebikee;

public class Patrimonio
{
    private String nombre;
    private int idDrawable;

    public Patrimonio(String nombre, int idDrawable) {
        this.nombre = nombre;
        this.idDrawable = idDrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public int getId() {
        return nombre.hashCode();
    }

    public static Patrimonio[] ITEMS = {
            new Patrimonio("Jaguar F-Type 2015", R.drawable.patrimonio1),
            new Patrimonio("Mercedes AMG-GT", R.drawable.patrimonio2),
            new Patrimonio("Mazda MX-5", R.drawable.patrimonio2),
            new Patrimonio("Porsche 911 GTS", R.drawable.patrimonio1),
            new Patrimonio("BMW Serie 6", R.drawable.patrimonio1),
            new Patrimonio("Casa Matriz", R.drawable.patrimonio2)
    };

    /**
     * Obtiene item basado en su identificador
     *
     * @param id identificador
     * @return Coche
     */
    public static Patrimonio getItem(int id) {
        for (Patrimonio item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}