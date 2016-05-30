
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMList;
/**
 *
 * @author Henry Salinas
 */
public class ListaAlmacenamiento
  extends List
  implements CommandListener, Runnable
{
  private final Command cmdSeleccionar = new Command("Actualizar", 4, 1);
  private final Command cmdSalir = new Command("Salir", 7, 1);
  private final Contactos midlet;

  public ListaAlmacenamiento(Contactos midlet)
  {
    super("Seleciones la lista de almacenamiento ", 3);
    String[] lists = PIM.getInstance().listPIMLists(1);
    for (int i = 0; i < lists.length; i++) {
      append(lists[i], null);
    }
    setSelectCommand(this.cmdSeleccionar);
    addCommand(this.cmdSalir);
    setCommandListener(this);
    this.midlet = midlet;
  }

  public void commandAction(Command cmd, Displayable d)
  {
    if (cmd == this.cmdSalir)
    {
      this.midlet.salir();
    }
    else if (cmd == this.cmdSeleccionar)
    {
      Form frmCarga = new Form("Cargando lista contactos");
      frmCarga.append("Por favor Espere ... ");
      Display.getDisplay(this.midlet).setCurrent(frmCarga);
      new Thread(this).start();
    }
  }

  public void run()
  {
    String nombreLista = getString(getSelectedIndex());
    try
    {
      PIMList list = PIM.getInstance().openPIMList(1, 3, nombreLista);

      Displayable screen = new ListaContactos(this.midlet, list);

      Display.getDisplay(this.midlet).setCurrent(screen);
    }
    catch (Exception e)
    {
      this.midlet.reportarError(e, this);
    }
  }
}
