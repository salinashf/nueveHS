/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Henry Salinas
 */
public class Soporte extends Form  implements CommandListener {

    private final Contactos midlet;
    private final Command cmdSalir = new Command("Salir", 7, 1);

    public Soporte(Contactos midlet) {
        super("No Compatible");
        this.midlet = midlet;
        Cargar();
    }

    public void Cargar() {
        append("El teléfono no soporta acceso a los contactos desde esta aplicación");
        addCommand(this.cmdSalir);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this.cmdSalir) {
            this.midlet.salir();
        }
    }
}
